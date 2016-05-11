package curso.mpgo.com.cursoandroid;

import android.app.ListActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class ListaLocais extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            ContainerPosicao container = (ContainerPosicao)bundle.getSerializable("listaItens");

            //ADAPTER
            //M.V.C
            setListAdapter(
                    new ArrayAdapter<Posicao>(this, android.R.layout.simple_list_item_1,
                            container.posicoes)
            );
        }
    }

}
