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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xyz.abelgomez.navigationdrawer.api.ConfigApi;
import xyz.abelgomez.navigationdrawer.model.Reserva;

import xyz.abelgomez.navigationdrawer.model.Usuario;

public class pruebaActivity2 extends AppCompatActivity {
    private TextView textViewCotiId;
    private TextView txtInformacionReserva;
    private static final int PICK_IMAGE_REQUEST = 1;
    private RequestQueue queue;

    private Button btnSubirIma;
    private String selectedFilePath = "";
    private static final int REQUEST_CODE_PICK_FILE = 1;

    String fileName="";
    //  String uploadedFileName =  ConfigApi.baseUrlE+"/file/filesImg/"+fileName;
    String uploadedFileName = "http://localhost:9999/file/filesImg/"+fileName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba2);

        Log.d("PruebaActivity2", "La actividad se ha creado correctamente");

        txtInformacionReserva = findViewById(R.id.txtinformacionreserva);

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
                guardarReservaConImagen();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PICK_FILE && resultCode == RESULT_OK) {
            Uri uri = data.getData();

            if (uri != null && "content".equals(uri.getScheme())) {
                try {
                    InputStream inputStream = getContentResolver().openInputStream(uri);
                    if (inputStream != null) {
                        inputStream.close();

                        selectedFilePath = getPathFromUri(uri);
                        if (selectedFilePath != null) {
                            fileName = getFileNameFromPath(selectedFilePath);
                            Log.d("ArchivoSeleccionado", "Nombre del archivo seleccionado: " + fileName);
                            txtInformacionReserva.setText("Archivo seleccionado: " + fileName);
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
                Log.d("PruebaActivity2", "La URI es nula o no tiene el esquema 'content'");
            }
        }

        queue = Volley.newRequestQueue(pruebaActivity2.this);

    }

    private void seleccionarArchivo() {
       /* Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("/");
        startActivityForResult(intent, REQUEST_CODE_PICK_FILE);*/
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Selecciona una imagen"), PICK_IMAGE_REQUEST);
    }

    private String getPathFromUri(Uri uri) {
        String path = null;
        String[] projection = {MediaStore.Images.Media.DATA};

        try (Cursor cursor = getContentResolver().query(uri, projection, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                path = cursor.getString(columnIndex);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (path == null) {
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

    private void guardarReservaConImagen() {
        if (!selectedFilePath.isEmpty()) {
            // Sube la imagen al servidor en un AsyncTask
            new FileUploadTask().execute(new File(selectedFilePath));
        } else {
            Toast.makeText(pruebaActivity2.this, "Por favor, seleccione un archivo antes de guardar la reserva", Toast.LENGTH_SHORT).show();
        }
    }

    private class FileUploadTask extends AsyncTask<File, Void, String> {
        @Override
        protected String doInBackground(File... files) {
            File file = files[0];
            return enviarArchivoAlServidor(file);
        }

        @Override
        protected void onPostExecute(String uploadedFileName) {
            // La imagen se ha subido con éxito, ahora guarda la reserva con los datos requeridos
            if (uploadedFileName != null) {
                // Aquí llamamos al método para guardar la reserva
                guardarReserva();
            } else {
                Toast.makeText(pruebaActivity2.this, "Error al subir el archivo", Toast.LENGTH_SHORT).show();
            }
        }

        private String enviarArchivoAlServidor(File file) {
            //  String uploadUrl = "http://localhost:9999/file/upload"; // URL para subir el archivo
            String uploadUrl = ConfigApi.baseUrlE+"/file/upload";
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(uploadUrl).openConnection();
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                String boundary = "===" + System.currentTimeMillis() + "===";
                connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

                DataOutputStream request = new DataOutputStream(connection.getOutputStream());

                request.writeBytes("--" + boundary + "\r\n");
                request.writeBytes("Content-Disposition: form-data; name=\"files\"; filename=\"" + file.getName() + "\"\r\n");
                request.writeBytes("Content-Type: " + URLConnection.guessContentTypeFromName(file.getName()) + "\r\n");
                request.writeBytes("\r\n");

                FileInputStream inputStream = new FileInputStream(file);
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    request.write(buffer, 0, bytesRead);
                }
                request.writeBytes("\r\n");
                inputStream.close();

                request.writeBytes("--" + boundary + "--\r\n");
                request.flush();
                request.close();

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream responseStream = connection.getInputStream();
                    // Leer la respuesta del servidor si es necesario
                    // ...
                    responseStream.close();
                } else {
                    // Manejar error en la respuesta del servidor si es necesario
                    // ...
                }

                connection.disconnect();

                // Si la subida fue exitosa, retorna el nombre del archivo subido (sin la URL base)
                return file.getName();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }


        private void guardarReserva() {
            String url = ConfigApi.baseUrlE + "/reserva/crear"; // URL para guardar la reserva en el servidor
            Reserva reserva = new Reserva();
            reserva.setResComprobante(uploadedFileName+ fileName);
            Gson gson = new Gson();
            String requestBody = gson.toJson(reserva);
            StringRequest request = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Manejar la respuesta de la solicitud
                            Log.d("TAG", "Response: " + response);
                            // Guardar la cotización

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Manejar errores de la solicitud
                            Log.e("TAG", "Error: " + error.toString());
                            Toast.makeText(pruebaActivity2.this, "¡No se pudo guardar su cotizacion!", Toast.LENGTH_SHORT).show();

                        }
                    }) {
                @Override
                public String getBodyContentType() {
                    return "application/json";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    return requestBody.getBytes(StandardCharsets.UTF_8);
                }
            };

            // Agregar la solicitud a la cola de solicitudes de Volley
            queue.add(request);


        }

    }
}