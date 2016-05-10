package curso.mpgo.com.cursoandroid;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

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
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap;

    private GoogleApiClient mGoogleApiClient;

    private LocationRequest mLocationRequest;

    private ClusterManager mClusterManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);

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
        mMap.addMarker(new MarkerOptions().position(latLng).title("Estou aqui"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    public void getLastLocation() {
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        LatLng eu = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        mMap.addMarker(new MarkerOptions().position(eu).title("Estou aqui"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(eu, 16));

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        Call<Itens> call = ((CoreApplication) getApplication()).service.searchPositions();
        call.enqueue(new Callback<Itens>() {
            @Override
            public void onResponse(Call<Itens> call, Response<Itens> response) {
                mClusterManager = new ClusterManager<MyItem>(MapsActivity.this, mMap);
                for (Posicao posicao : response.body().posicoes) {
                    MyItem offsetItem = new MyItem(posicao.latitude, posicao.longitude);
                    mClusterManager.addItem(offsetItem);
                }

                for (Circulo circulo : response.body().circulos) {
                    CircleOptions circleOpt = new CircleOptions()
                            .center(new LatLng(circulo.latitude, circulo.longitude))
                            .fillColor(Color.LTGRAY)
                            .strokeColor(Color.BLACK)
                            .radius(circulo.raio);

                    mMap.addCircle(circleOpt);
                }

                for (Poligono poligono : response.body().poligonos) {
                    List<LatLng> pontos = new ArrayList<LatLng>();
                    for (Ponto ponto : poligono.pontos){
                        pontos.add(new LatLng(ponto.latitude, ponto.longitude));
                    }

                    PolygonOptions polOpt = new PolygonOptions()
                        .addAll(pontos)
                            .strokeColor(Color.BLUE)
                            .fillColor(Color.LTGRAY);
                    mMap.addPolygon(polOpt);
                }
                mMap.setOnCameraChangeListener(mClusterManager);
                mMap.setOnMarkerClickListener(mClusterManager);
            }

            @Override
            public void onFailure(Call<Itens> call, Throwable t) {
                Log.e("CURSO", "Pepino: " + t.getLocalizedMessage());
            }
        });

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

}
