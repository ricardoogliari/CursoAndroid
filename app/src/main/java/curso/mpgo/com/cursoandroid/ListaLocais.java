package curso.mpgo.com.cursoandroid;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.GradientDrawable;
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

public class ListaLocais extends AppCompatActivity {

    private ContainerPosicao container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_LANDSCAPE){
            finish();
        }

        setContentView(R.layout.listalocais_activity);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            container = (ContainerPosicao)bundle.getSerializable("listaItens");

            ((ListaFragment)getSupportFragmentManager().findFragmentById(R.id.listaFragment)).
                    setPontos(container.posicoes);
        }
    }




}
