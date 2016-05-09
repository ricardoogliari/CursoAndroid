package curso.mpgo.com.cursoandroid;

import android.app.Application;

import com.facebook.FacebookSdk;

/**
 * Created by ricardoogliari on 5/9/16.
 */
public class CoreApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
    }
}
