package curso.mpgo.com.cursoandroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by ricardoogliari on 5/12/16.
 */
public class AlterouEstado extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent i) {
        //sistema operacional
        Intent intent = new Intent("mudar.estado.conectividade.tela");
        context.sendBroadcast(intent);
    }
}
