package xyz.abelgomez.navigationdrawer.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xyz.abelgomez.navigationdrawer.api.ConfigApi;
import xyz.abelgomez.navigationdrawer.api.ReservaApi;
import xyz.abelgomez.navigationdrawer.model.Reserva;
import xyz.abelgomez.navigationdrawer.utils.GenericResponse;

public class Reservarepository {
    private final ReservaApi api;
    private static Reservarepository repository;


    public Reservarepository() {
        this.api = ConfigApi.getReservaApi();
    }
    public static Reservarepository getInstance() {
        if (repository == null) {
            repository = new Reservarepository();
        }
        return repository;
    }
    public LiveData<GenericResponse<Reserva>> guardarproducto(Reserva c){
        final MutableLiveData<GenericResponse<Reserva>> mld = new MutableLiveData<>();
        this.api.save(c).enqueue(new Callback<GenericResponse<Reserva>>() {
            @Override
            public void onResponse(Call<GenericResponse<Reserva>> call, Response<GenericResponse<Reserva>> response) {
                mld.setValue(response.body());
            }

            @Override
            public void onFailure(Call<GenericResponse<Reserva>> call, Throwable t) {
                mld.setValue(new GenericResponse<>());
                System.out.println("Se ha producido un error : " + t.getMessage());
                t.printStackTrace();
            }
        });
        return mld;
    }
}
