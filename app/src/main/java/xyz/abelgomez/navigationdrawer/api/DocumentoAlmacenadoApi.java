package xyz.abelgomez.navigationdrawer.api;


import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import xyz.abelgomez.navigationdrawer.model.DocumentoAlmacenado;
import xyz.abelgomez.navigationdrawer.utils.GenericResponse;

public interface DocumentoAlmacenadoApi {
    String base = "api/documento-almacenado/crear";
    @Multipart
    @POST(base)
    Call<GenericResponse<DocumentoAlmacenado>> save(@Part MultipartBody.Part file, @Part("nombre") RequestBody requestBody);
}
