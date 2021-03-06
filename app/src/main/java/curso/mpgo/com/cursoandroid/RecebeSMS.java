package curso.mpgo.com.cursoandroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * Created by ricardoogliari on 5/12/16.
 */
public class RecebeSMS extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //sd23r5t6|1|-46.554|-46.767
        //1 - poi
        //2 - circulo
        //3 - lattiude
        //4 - longitude

        Log.e("CURSO", "OnReceive BoradcastReceiver");

        final Bundle bundle = intent.getExtras();

        try {

            if (bundle != null) {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String message = currentMessage.getDisplayMessageBody();

                    Log.e("CURSO", "message: " + message);
                    if (message.startsWith("a")){
                        String[] partes = message.split("x");
                        Log.e("CURSO", "message: " + partes[0]);
                        Log.e("CURSO", "message: " + partes[1]);
                        Log.e("CURSO", "message: " + partes[2]);
                        Log.e("CURSO", "message: " + partes[3]);

                        Intent iSMS = new Intent("recebeu.sms.com.marcador");
                        iSMS.putExtra("tipo", partes[1]);
                        iSMS.putExtra("latitude", partes[2]);
                        iSMS.putExtra("longitude", partes[3]);
                        context.sendBroadcast(iSMS);
                    }

                } // end for loop
            } // bundle is null

        } catch (Exception e) {
        }
    }
}
