package xyz.abelgomez.navigationdrawer.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;


import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import xyz.abelgomez.navigationdrawer.model.DocumentoAlmacenado;
import xyz.abelgomez.navigationdrawer.repository.DocumentoAlmacenadoRepository;
import xyz.abelgomez.navigationdrawer.utils.GenericResponse;

public class DocumentoAlmacenadoViewModel extends AndroidViewModel {
    private final DocumentoAlmacenadoRepository repository;

    public DocumentoAlmacenadoViewModel(@NonNull Application application) {
        super(application);
        this.repository = DocumentoAlmacenadoRepository.getInstance();
    }
    public LiveData<GenericResponse<DocumentoAlmacenado>> save(MultipartBody.Part part, RequestBody requestBody){
        return this.repository.savePhoto(part, requestBody);
    }
}
