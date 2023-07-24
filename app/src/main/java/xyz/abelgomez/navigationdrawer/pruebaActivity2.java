package xyz.abelgomez.navigationdrawer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import xyz.abelgomez.navigationdrawer.model.FileUploadTask;

public class pruebaActivity2 extends AppCompatActivity {

    private TextView textViewCotiId;
    private TextView txtInformacionReserva; // Agrega el TextView correspondiente
    private Button btnSubirIma;
    private String selectedFilePath = "";
    private static final int REQUEST_CODE_PICK_FILE = 1;
    String fileName = "";
    String uploadedFileName = "http://localhost:9999/file/filesImg/";

    private static final int REQUEST_PERMISSIONS = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba2);

        // Verificar y solicitar permisos
        checkAndRequestPermissions();

        // Obtener el ID de cotización desde los extras
        Intent intent = getIntent();
        long cotiId = intent.getLongExtra("cotiId", -1);

        txtInformacionReserva = findViewById(R.id.txtinformacionreserva); // Busca el TextView por su ID

        btnSubirIma = findViewById(R.id.btnsubirarchivo);
        btnSubirIma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seleccionarArchivo();
            }
        });

        Button btnGuardarReserva = findViewById(R.id.Guardarreserva);
        btnGuardarReserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!selectedFilePath.isEmpty()) {
                    // Crear una lista con el archivo seleccionado y enviarla al servidor
                    List<File> files = new ArrayList<>();
                    files.add(new File(selectedFilePath));
                    System.out.println("======================================================"+uploadedFileName);

                    // Llama al AsyncTask para realizar la operación de red en un hilo secundario
                    new FileUploadTask().execute(files);
                } else {
                    Toast.makeText(pruebaActivity2.this, "Por favor, seleccione un archivo antes de guardar la reserva", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void checkAndRequestPermissions() {
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        List<String> permissionsToRequest = new ArrayList<>();

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission);
            }
        }

        if (!permissionsToRequest.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsToRequest.toArray(new String[0]), REQUEST_PERMISSIONS);
        } else {
            // Si ya se tienen los permisos, realizar la acción deseada (por ejemplo, cargar el archivo)
            cargarArchivo();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS) {
            boolean allPermissionsGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }

            if (allPermissionsGranted) {
                // Permiso concedido, realizar la acción deseada (por ejemplo, cargar el archivo)
                cargarArchivo();
            } else {
                // Permiso denegado, manejar el escenario en consecuencia (por ejemplo, mostrar un mensaje)
                Toast.makeText(this, "Permisos denegados", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void cargarArchivo() {
        // Implementa aquí la lógica para cargar el archivo seleccionado o cualquier otra acción que desees realizar.
        // Por ejemplo, aquí puedes leer los datos del archivo o enviarlo al servidor.
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PICK_FILE && resultCode == RESULT_OK) {
            Uri uri = data.getData();

            if (uri != null) {
                selectedFilePath = getRealPathFromURI(uri);
                if (selectedFilePath != null) {
                    fileName = getFileNameFromPath(selectedFilePath);
                    Log.d("ArchivoSeleccionado", "Nombre del archivo seleccionado: " + fileName);

                    txtInformacionReserva.setText("Archivo seleccionado: " + fileName);
                    uploadedFileName+=fileName;
                } else {
                    Log.d("PruebaActivity2", "El path del archivo es nulo");
                }
            } else {
                Log.d("PruebaActivity2", "La URI es nula");
            }
        }
    }



    public void seleccionarArchivo() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, REQUEST_CODE_PICK_FILE);
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor == null) {
            return null;
        }
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(column_index);
        cursor.close();
        return path;
    }


    private String getPathFromUri(Uri uri) {
        String path = null;
        String[] projection = { MediaStore.Images.Media.DATA };

        try (Cursor cursor = getContentResolver().query(uri, projection, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                path = cursor.getString(columnIndex);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (path == null) {
            // Si la columna "_data" no está presente en el cursor o no se pudo obtener la ruta,
            // intentamos obtener el path de manera alternativa
            path = uri.getPath();
        }

        return path;
    }

    private String getFileNameFromPath(String path) {
        if (path != null) {
            return new File(path).getName();
        }
        return null;
    }

}

