package xyz.abelgomez.navigationdrawer.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import xyz.abelgomez.navigationdrawer.model.Reserva;
import xyz.abelgomez.navigationdrawer.utils.GenericResponse;

public interface ReservaApi {
    String base = "/reserva/crearReserva";
    @POST(base)
    Call<GenericResponse<Reserva>> save (@Body Reserva u);
}
