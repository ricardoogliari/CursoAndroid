package curso.mpgo.com.cursoandroid;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ricardoogliari on 5/12/16.
 */
public class ListaFragment extends ListFragment {

    private LayoutInflater inflater;
    private List<Posicao> posicoes;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setPontos(List<Posicao> posicoes){
        setListAdapter(new MeuAdapter(posicoes));
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Posicao posicao = posicoes.get(position);
        Uri gmmIntentUri = Uri.parse("geo:"+posicao.latitude+","+posicao.longitude+"?q=restaurants");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    class MeuAdapter extends BaseAdapter {
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
