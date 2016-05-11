package curso.mpgo.com.cursoandroid.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by ricardoogliari on 5/11/16.
 */
public class Conectividade {

    public static boolean haveConnectivity(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService (Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

}
