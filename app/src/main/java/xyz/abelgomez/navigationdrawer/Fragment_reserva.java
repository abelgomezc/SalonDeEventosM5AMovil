package xyz.abelgomez.navigationdrawer;

import static android.app.Activity.RESULT_OK;

import static xyz.abelgomez.navigationdrawer.R.id.btnsubirarchivo;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.loader.content.CursorLoader;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.File;

public class Fragment_reserva extends Fragment {
    View view;


    private Button btnSubirIma;
    private File f;
    private Button buttonSelectPdf;
    private Button buttonSelectImage;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reserva, container, false);


        btnSubirIma=getView().findViewById(btnsubirarchivo);


        btnSubirIma.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                    seleccionarArchivo();


            }
        });

        return view;

    }


    private static final int REQUEST_CODE_PICK_FILE = 1;

    public void seleccionarArchivo() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, REQUEST_CODE_PICK_FILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PICK_FILE && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            String path = getPathFromUri(uri);
            // enviarArchivoAlServidor(path);
        }
    }

    private String getPathFromUri(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getActivity().getApplicationContext(), uri, projection, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }



    private void enviarReserva() {

    }
}