package curso.mpgo.com.cursoandroid;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class ListaLocais extends ListActivity {

    private LayoutInflater inflater;
    private ContainerPosicao container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            container = (ContainerPosicao)bundle.getSerializable("listaItens");

            setListAdapter(new MeuAdapter(container.posicoes));
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Posicao posicao = container.posicoes.get(position);
        Uri gmmIntentUri = Uri.parse("geo:"+posicao.latitude+","+posicao.longitude+"?q=restaurants");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    class MeuAdapter extends BaseAdapter{
        private List<Posicao> posicoes;

        public MeuAdapter(List<Posicao> pos){
            posicoes = pos;
        }

        @Override
        public int getCount() {
            return posicoes.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = inflater.inflate(R.layout.item_lista, null);
            ImageView imgFundo = (ImageView)view.findViewById(R.id.itemListaFundo);
            TextView txtLocal = (TextView) view.findViewById(R.id.itemListaLocal);
            switch (position){
                case 0:
                    imgFundo.setImageResource(R.drawable.back_lista);
                    txtLocal.setText("Muliterno");
                    break;
                case 1:
                    imgFundo.setImageResource(R.drawable.back_lista2);
                    txtLocal.setText("Vanini");
                    break;
                case 2:
                    imgFundo.setImageResource(R.drawable.back_lista3);
                    txtLocal.setText("David Canabarro");
            }

            ((TextView) view.findViewById(R.id.itemListaTitulo)).setText(posicoes.get(position).name);

            return view;
        }
    }

}
