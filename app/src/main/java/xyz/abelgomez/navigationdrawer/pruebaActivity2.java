package xyz.abelgomez.navigationdrawer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import xyz.abelgomez.navigationdrawer.model.VolleyMultipartRequest;

public class pruebaActivity2 extends AppCompatActivity {
    private TextView textViewCotiId;
    private TextView txtInformacionReserva; // Agrega el TextView correspondiente

    private Button btnSubirIma;
    private File f;
    private static final int REQUEST_CODE_PICK_FILE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba2);

        Log.d("PruebaActivity2", "La actividad se ha creado correctamente");

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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PICK_FILE && resultCode == RESULT_OK) {
            Uri uri = data.getData();

            if (uri != null) {
                try {
                    InputStream inputStream = getContentResolver().openInputStream(uri);
                    if (inputStream != null) {
                        inputStream.close();

                        String path = getPathFromUri(uri);
                        if (path != null) {
                            String fileName = getFileNameFromPath(path);
                            Log.d("ArchivoSeleccionado", "Nombre del archivo seleccionado: " + fileName);

                            txtInformacionReserva.setText("Archivo seleccionado: " + fileName);
                            enviarArchivoAlServidor(path); // Llama a la función para enviar el archivo al servidor
                        } else {
                            Log.d("PruebaActivity2", "El path del archivo es nulo");
                        }
                    } else {
                        Log.d("PruebaActivity2", "El archivo no se puede abrir");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Log.d("PruebaActivity2", "El intent data no contiene Uri");
            }
        }
    }


    public void seleccionarArchivo() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, REQUEST_CODE_PICK_FILE);
    }
    private String getPathFromUri(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            try {
                cursor.moveToFirst();
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (column_index >= 0) {
                    String result = cursor.getString(column_index);
                    if (result != null) {
                        return result;
                    }
                }
            } finally {
                cursor.close();
            }
        }

        // Si la columna "_data" no está presente en el cursor, intentamos obtener el path de manera alternativa
        String path = uri.getPath();
        return path;
    }




    private String getFileNameFromPath(String path) {
        if (path != null) {
            return new File(path).getName();
        }
        return null;
    }

    private void enviarArchivoAlServidor(String filePath) {
        String url = "http://192.168.18.4:9999/file/upload"; // Reemplaza con la URL de tu servidor

        File file = new File(filePath);

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(
                url,
                null,
                null,
                "multipart/form-data", // Utiliza el nombre correcto "multipart/form-data"
                file,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        String message = new String(response.data);
                        Log.d("ArchivoSubido", "Respuesta del servidor: " + message);
                        // Puedes realizar acciones adicionales con la respuesta del servidor
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ArchivoSubido", "Error al subir el archivo: " + error.toString());
                        // Maneja el error aquí si es necesario
                    }
                }
        );

        // Agrega la solicitud a la cola de Volley
        requestQueue.add(multipartRequest);
    }

}

