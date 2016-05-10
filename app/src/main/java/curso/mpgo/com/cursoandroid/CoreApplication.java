package curso.mpgo.com.cursoandroid;

import android.app.Application;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ricardoogliari on 5/9/16.
 */
public class CoreApplication extends Application {

    public MyInterfaceRetrofit service;

    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new LoggingInterceptor()).build();

        Gson gson = new GsonBuilder().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://1-dot-temcarona-1201.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        service = retrofit.create(MyInterfaceRetrofit.class);
    }

    class LoggingInterceptor implements Interceptor {
        @Override
        public okhttp3.Response intercept(Interceptor.Chain chain) throws IOException {
            Request request = chain.request();

            long t1 = System.nanoTime();
            Log.e("CURSO", String.format("Sending request %s on %s",
                    request.url(), request.headers()));

            okhttp3.Response response = chain.proceed(request);

            long t2 = System.nanoTime();
            Log.e("CURSO", String.format("Received response for %s in %.1fms    \n%s",
                    response.request().url(), (t2 - t1) / 1e6d, response.headers()));

            final String responseString = new String(response.body().bytes());

            Log.e("CURSO", "Response: " + responseString);

            return response.newBuilder()
                    .body(ResponseBody.create(response.body().contentType(), responseString))
                    .build();
        }
    }
}
