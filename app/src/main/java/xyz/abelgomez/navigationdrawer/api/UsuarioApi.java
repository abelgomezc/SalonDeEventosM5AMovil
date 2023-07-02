package xyz.abelgomez.navigationdrawer.api;




import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import xyz.abelgomez.navigationdrawer.entity.GenericResponse;
import xyz.abelgomez.navigationdrawer.model.Usuario;

public interface UsuarioApi {
    String base = "/usuario";

    @FormUrlEncoded
    @POST(base + "/login1")
    Call<GenericResponse<Usuario>> login(@Field("usu_nombre_usuario") String email, @Field("usu_contrasena") String contrasenia);

    @POST(base)
    Call<GenericResponse<Usuario>> save (@Body Usuario u);
}
