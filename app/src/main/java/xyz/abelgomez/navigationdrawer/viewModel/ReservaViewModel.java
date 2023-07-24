package xyz.abelgomez.navigationdrawer.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import xyz.abelgomez.navigationdrawer.model.Reserva;
import xyz.abelgomez.navigationdrawer.repository.Reservarepository;
import xyz.abelgomez.navigationdrawer.utils.GenericResponse;

public class ReservaViewModel extends AndroidViewModel {
    private final Reservarepository repository;

    public ReservaViewModel(@NonNull Application application) {
        super(application);
        repository = Reservarepository.getInstance();
    }

    public LiveData<GenericResponse<Reserva>> guardarproducto(Reserva c){
        return  repository.guardarproducto(c);
    }

}
