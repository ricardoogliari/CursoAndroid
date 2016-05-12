package curso.mpgo.com.cursoandroid;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.List;

import curso.mpgo.com.cursoandroid.database.CirculoDbHelper;
import curso.mpgo.com.cursoandroid.database.PosicaoDbHelper;
import curso.mpgo.com.cursoandroid.util.Conectividade;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap;

    private GoogleApiClient mGoogleApiClient;

    private LocationRequest mLocationRequest;

    private ClusterManager mClusterManager;

    private PosicaoDbHelper dbPositionHelper;
    private CirculoDbHelper dbCircleHelper;

    private List<Posicao> posicoes;

    private ImageView imgIconList;

    private ListaFragment listaFragment;

    private TextView txtOffline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        imgIconList = (ImageView) findViewById(R.id.imgIconList);
        txtOffline = (TextView) findViewById(R.id.actMapsTxtOffline);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        listaFragment = (ListaFragment)getSupportFragmentManager().findFragmentById(R.id.listaFragment);

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        dbPositionHelper = new PosicaoDbHelper(this);
        dbCircleHelper = new CirculoDbHelper(this);

        IntentFilter filtro = new IntentFilter("mudar.estado.conectividade.tela");
        registerReceiver(receiverConnectivity, filtro);

        IntentFilter filtroSMS = new IntentFilter("recebeu.sms.com.marcador");
        registerReceiver(receiverSMS, filtroSMS);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        //mMap.clear();
        //mMap.addMarker(new MarkerOptions().position(latLng).title("Estou aqui"));
        //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        unregisterReceiver(receiverConnectivity);
        unregisterReceiver(receiverSMS);
    }

    private BroadcastReceiver receiverSMS = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String type = intent.getStringExtra("tipo");
            double lat = Double.parseDouble(intent.getStringExtra("latitude"));
            double lng = Double.parseDouble(intent.getStringExtra("longitude"));
            switch (type){
                case "1":
                    MarkerOptions markerOptions = new MarkerOptions()
                            .position(new LatLng(lat, lng))
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    mMap.addMarker(markerOptions);
                    //poi
                    break;
                case "2":
                    //circle
                    CircleOptions circleOptions = new CircleOptions()
                            .center(new LatLng(lat, lng))
                            .radius(2000)
                            .fillColor(Color.LTGRAY)
                            .strokeColor(Color.BLUE);
                    mMap.addCircle(circleOptions);
            }
        }
    };


    private BroadcastReceiver receiverConnectivity = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Conectividade.haveConnectivity(MapsActivity.this)){
                txtOffline.setVisibility(View.GONE);

                mMap.clear();
                getLastLocation();
            } else {
                txtOffline.setVisibility(View.VISIBLE);
            }
        }
    };

    public void getLastLocation() {
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        LatLng eu = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        mMap.addMarker(new MarkerOptions().position(eu).title("Estou aqui"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(eu, 16));

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        if (Conectividade.haveConnectivity(this)) {
            Call<Itens> call = ((CoreApplication) getApplication()).service.searchPositions();
            call.enqueue(new Callback<Itens>() {
                @Override
                public void onResponse(Call<Itens> call, Response<Itens> response) {
                    mClusterManager = new ClusterManager<MyItem>(MapsActivity.this, mMap);
                    dbPositionHelper.clear();
                    dbCircleHelper.clear();

                    for (Poligono poligono : response.body().poligonos) {
                        List<LatLng> pontos = new ArrayList<LatLng>();
                        for (Ponto ponto : poligono.pontos) {
                            pontos.add(new LatLng(ponto.latitude, ponto.longitude));
                        }

                        PolygonOptions polOpt = new PolygonOptions()
                                .addAll(pontos)
                                .strokeColor(Color.BLUE)
                                .fillColor(Color.LTGRAY);
                        mMap.addPolygon(polOpt);
                    }
                    dbPositionHelper.fullCreate(response.body().posicoes);
                    dbCircleHelper.fullCreate(response.body().circulos);
                    populaCirculos(response.body().circulos);
                    populaMapa(response.body().posicoes);

                    if (imgIconList != null)
                        imgIconList.setVisibility(View.VISIBLE);
                }

                @Override
                public void onFailure(Call<Itens> call, Throwable t) {
                    Log.e("CURSO", "Pepino: " + t.getLocalizedMessage());
                }
            });
        } else {
            txtOffline.setVisibility(View.VISIBLE);

            List<Posicao> posicoes = dbPositionHelper.read();
            List<Circulo> circulos = dbCircleHelper.read();

            populaMapa(posicoes);
            populaCirculos(circulos);
        }

    }

    public void populaCirculos(List<Circulo> circulos){
        for (Circulo circulo : circulos) {
            CircleOptions circleOpt = new CircleOptions()
                    .center(new LatLng(circulo.latitude, circulo.longitude))
                    .fillColor(Color.LTGRAY)
                    .strokeColor(Color.BLACK)
                    .radius(circulo.raio);

            mMap.addCircle(circleOpt);
        }
    }

    public void populaMapa(List<Posicao> posicoes){
        this.posicoes = posicoes;

        if (listaFragment != null && listaFragment.isInLayout()){
            listaFragment.setPontos(posicoes);
        }

        for (Posicao posicao : posicoes) {
            MyItem offsetItem = new MyItem(posicao.latitude, posicao.longitude);
            mClusterManager.addItem(offsetItem);
        }

        mMap.setOnCameraChangeListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                Log.e("CURSO", "Sem esta permissão não podemos pegar informações sobre sua posição geográfica e melhor atendê-lo com nossos serviços.");
                return;
            }
            getLastLocation();
        } else {
            Log.e("CURSO", "Sem esta permissão não podemos pegar informações sobre sua posição geográfica e melhor atendê-lo com nossos serviços.");
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        getLastLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    public void mostraLista(View view){
        ContainerPosicao containerPosicao = new ContainerPosicao();
        containerPosicao.posicoes = posicoes;

        Intent intent = new Intent(this, ListaLocais.class);
        intent.putExtra("listaItens", containerPosicao);
        startActivity(intent);
    }

}
