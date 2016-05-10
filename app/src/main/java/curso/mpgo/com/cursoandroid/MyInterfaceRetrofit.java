package curso.mpgo.com.cursoandroid;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by ricardoogliari on 5/10/16.
 */
public interface MyInterfaceRetrofit {

    //ANNOTATIONS
    @GET("v2/57322cda0f0000550aead71c")
    Call<Itens> searchPositions();

   /* http://1-dot-temcarona-1201.appspot.com/listaviagens?pontocentral=-23.5454,-46.777&raio=5000
    @GET("listaviagens")
    Call<Itens> searchPositions(
            @Query("pontocentral") String position,
            @Query("raio") int raio);
*/

       /*@POST("api/user/store")
       Call<User> createUser(
               @Body RequestBody user);*/

    /*http://1-dot-temcarona-1201.appspot.com/listaviagens/userid=rewrwehrewkjh
       @GET("listaviagens/{userid}")
       Call<User> getUser(
               @Path("userid") String userId);
*/

}
