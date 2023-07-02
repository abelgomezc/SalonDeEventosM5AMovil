package xyz.abelgomez.navigationdrawer.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;


import org.jetbrains.annotations.NotNull;


import xyz.abelgomez.navigationdrawer.entity.GenericResponse;
import xyz.abelgomez.navigationdrawer.model.Usuario;
import xyz.abelgomez.navigationdrawer.repository.UsuarioRepository;

public class UsuarioViewModel extends AndroidViewModel {
 private final UsuarioRepository repository;
    public UsuarioViewModel(@NonNull @NotNull Application application) {
        super(application);
        this.repository = UsuarioRepository.getInstance();
    }
    public LiveData<GenericResponse<Usuario>> login(String email, String pass){
        return this.repository.login(email, pass);
    }

    public LiveData<GenericResponse<Usuario>> save(Usuario u){
        return this.repository.save(u);
    }
}
