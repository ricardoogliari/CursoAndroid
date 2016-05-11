package curso.mpgo.com.cursoandroid.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by ricardoogliari on 5/11/16.
 */
public class SharedManager {

    private static final String DEFAULT_CONF = "default_conf";
    public static final String KEY_LOGIN_GOOGLE = "signInGoogle";

    public static void saveBoolean(Context ctx, String name, String key, boolean value){
        SharedPreferences sp = ctx.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor spEdit = sp.edit();
        spEdit.putBoolean(key, value);
        spEdit.apply();
    }

    public static void saveBoolean(Context ctx, String key, boolean value){
        saveBoolean(ctx, DEFAULT_CONF, key, value);
    }

    public static boolean getBoolean(Context ctx, String name, String key){
        SharedPreferences sp = ctx.getSharedPreferences(name, Context.MODE_PRIVATE);
        return sp.getBoolean(key, false);//default
    }

    public static boolean getBoolean(Context ctx, String key){
        return getBoolean(ctx, DEFAULT_CONF, key);
    }

}
