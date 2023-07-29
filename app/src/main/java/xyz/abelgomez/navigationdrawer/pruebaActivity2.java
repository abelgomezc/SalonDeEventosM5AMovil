package xyz.abelgomez.navigationdrawer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.FileUtils;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import xyz.abelgomez.navigationdrawer.api.ConfigApi;
import xyz.abelgomez.navigationdrawer.model.Cotizacion;
import xyz.abelgomez.navigationdrawer.model.FileModel1;
import xyz.abelgomez.navigationdrawer.model.Reserva;

import xyz.abelgomez.navigationdrawer.model.Usuario;

    public class pruebaActivity2 extends AppCompatActivity {

        private static final String PREF_NAME = "MiPreferencia";
        private static final String KEY_USUARIO = "usuario";
        Usuario usuario;
        private TextView textViewCotiId;
        private TextView txtInformacionReserva;
        private static final int PICK_IMAGE_REQUEST = 1;
        private RequestQueue queue;

        private Button btnSubirIma;
        private String selectedFilePath = "";
        private static final int REQUEST_CODE_PICK_FILE = 1;
        DatePicker datefecha;

        // Variable para almacenar el nombre del archivo subido en el servidor
        private String fileName1 = "";
        private static String fileName;
        long cotiId;

        private Cotizacion cotizacion;


        String uploadedFileName = "http://localhost:9999/file/filesImg/";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_prueba2);

            // Inicialización de componentes de la interfaz de usuario
            txtInformacionReserva = findViewById(R.id.txtinformacionreserva);

            SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            String usuarioJson = sharedPreferences.getString(KEY_USUARIO, "");
            Gson gson = new Gson();
            usuario = gson.fromJson(usuarioJson, Usuario.class);
            datefecha = findViewById(R.id.datePickerfecha);
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
                    if (validarfecha()) {

                        if (validaraño()) {
                            if (validarFechaAnticipacion()) {
                                guardarReservaConImagen();

                            }
                        }
                    }

                }
            });

            Button btnCancelarReserva = findViewById(R.id.Cancelarrreserva);
            btnCancelarReserva.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Llamar al método para cancelar la reserva
                    cancelarReserva();
                }
            });


            // Habilitar la flecha de retroceso en la barra de navegación (si está disponible)
            // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            // Inicialización de la cola de solicitudes de Volley
            queue = Volley.newRequestQueue(pruebaActivity2.this);
        }

        // Método para mostrar el cuadro de diálogo de confirmación antes de guardar la reserva
        private void mostrarConfirmacionGuardarReserva() {
            AlertDialog.Builder builder = new AlertDialog.Builder(pruebaActivity2.this);
            builder.setTitle("Confirmar Guardar Reserva");
            builder.setMessage("¿Estás seguro de que deseas guardar esta reserva?");
            builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Si el usuario confirma la reserva, llamamos al método para guardar la reserva con la imagen
                    //   guardarReservaConImagen();

                    new FileUploadTask().execute(new File(selectedFilePath));
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Si el usuario decide no guardar la reserva, simplemente cierra el cuadro de diálogo sin realizar ninguna acción adicional.
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }

        private void cancelarReserva() {


            AlertDialog.Builder builder = new AlertDialog.Builder(pruebaActivity2.this);
            builder.setTitle("Confirmar Cancelación");
            builder.setMessage("¿Estás seguro de que deseas cancelar esta reserva?");
            builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Si el usuario confirma la cancelación, redirigimos a la sección "Tus Cotizaciones"
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TusCotizacionesFragment()).commit();
                    // También puedes agregar un toast o mensaje para notificar al usuario que la reserva se ha cancelado
                    Toast.makeText(pruebaActivity2.this, "Reserva cancelada", Toast.LENGTH_SHORT).show();
                }
            });



            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Si el usuario decide no cancelar la reserva, simplemente cierra el cuadro de diálogo sin realizar ninguna acción adicional.
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }


        // Método para manejar el resultado de la selección de un archivo
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
                                // Obtener el nombre del archivo seleccionado
                                String fileName = getFileNameFromPath(selectedFilePath);
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
        }

        // Método para abrir el selector de archivos y permitir al usuario seleccionar uno
        private void seleccionarArchivo() {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Selecciona una imagen"), PICK_IMAGE_REQUEST);
        }

        // Método para obtener la ruta de un archivo seleccionado a partir de su URI
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

        // Método para obtener el nombre de un archivo a partir de su ruta
        private String getFileNameFromPath(String path) {
            if (path != null) {
                return new File(path).getName();
            }
            return null;
        }


        private void guardarReservaConImagen() {
            if (!selectedFilePath.isEmpty()) {
                // Subir la imagen al servidor en un AsyncTask
                mostrarConfirmacionGuardarReserva();


            } else {
                Toast.makeText(pruebaActivity2.this, "Por favor, seleccione un archivo antes de guardar la reserva", Toast.LENGTH_SHORT).show();
            }
        }

        // Clase interna para manejar la subida de archivos en segundo plano
        private class FileUploadTask extends AsyncTask<File, Void, List<FileModel>> {
            @Override
            protected List<FileModel> doInBackground(File... files) {
                File file = files[0];
                return enviarArchivosAlServidor(file);
            }

            @Override
            protected void onPostExecute(List<FileModel> uploadedFiles) {
                // El archivo se ha subido correctamente, ahora guardar la reserva con los datos requeridos
                if (!uploadedFiles.isEmpty()) {
                    // Aquí obtenemos el primer archivo de la lista y asignamos su nombre a fileName1
                    FileModel firstFile = uploadedFiles.get(0);
                    String name = firstFile.getName();

                    //    uploadedFileName = "http://localhost:9999/file/filesImg/" + fileName;


                    obtenerURLArchivo(fileName);


                } else {
                    Toast.makeText(pruebaActivity2.this, "Error: lista de archivos subidos vacía", Toast.LENGTH_SHORT).show();
                }
            }

            private void obtenerURLArchivo(String fileName) {
                //String url = "http://localhost:9999/file/files/" + fileName;

                String url = ConfigApi.baseUrlE + "/file/files/" + fileName;

                StringRequest request = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Manejar la respuesta del servidor
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String name = jsonObject.getString("name");
                                    String url = jsonObject.getString("url");
                                    // Asignar la URL a la variable uploadedFileName
                                    uploadedFileName = url;

                                    uploadedFileName = uploadedFileName.replace("192.168.18.4", "localhost");
                                   // uploadedFileName = uploadedFileName.replace("10.0.2.2", "localhost");
                                    Log.d("TAG+++++++++++++++++++++++++++++", "URL del archivo: " + uploadedFileName);
                                    // Aquí puedes hacer lo que necesites con la URL, como guardarla en la reserva
                                    // o usarla de alguna otra forma.

                                    guardarReserva(uploadedFileName);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // Manejar errores de la solicitud
                                Log.e("TAG", "Error al obtener la URL del archivo: " + error.toString());
                            }
                        });

                // Agregar la solicitud a la cola de solicitudes de Volley
                queue.add(request);
            }


            private List<FileModel> enviarArchivosAlServidor(File file) {
                String uploadUrl = ConfigApi.baseUrlE + "/file/upload";
                List<FileModel> uploadedFiles = new ArrayList<>();

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
                        BufferedReader reader = new BufferedReader(new InputStreamReader(responseStream));
                        StringBuilder responseBuilder = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            responseBuilder.append(line);
                        }
                        responseStream.close();

                        // Imprimir la respuesta JSON del servidor en logcat
                        String jsonResponse = responseBuilder.toString();
                        Log.d("TAG", "Respuesta JSON del servidor: " + jsonResponse);

                        // Parsear la respuesta JSON y agregar los objetos FileModel a la lista
                        JSONArray jsonArray = new JSONArray(jsonResponse);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String name = jsonObject.getString("name");

                            fileName = name;
                            System.out.println("forrrrrrrrrrrrrrrrr     " + fileName);
                            String url = jsonObject.getString("url");
                            FileModel fileModel = new FileModel(name, url);
                            uploadedFiles.add(fileModel);
                        }
                    } else {
                        // Manejar error en la respuesta del servidor si es necesario
                        // ...
                    }

                    connection.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return uploadedFiles;
            }


            private void guardarReserva(String urla) {
                // Obtener el ID de cotización desde los extras
                Intent intent = getIntent();
                long cotiId = intent.getLongExtra("cotiId", -1);
                System.out.println("id cotizacion ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::" + cotiId);
                // Crear un objeto de Cotizacion y establecer su ID con el valor obtenido de los extras
                Cotizacion cotizacion = new Cotizacion();
                cotizacion.setCotiId(cotiId);

                // Crear un objeto de Reserva y establecer la cotizacion
                Reserva reserva = new Reserva();

                int year = datefecha.getYear();
                int month = datefecha.getMonth();
                int day = datefecha.getDayOfMonth();
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                Date cotiFechaEvento = calendar.getTime();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String fechaFormateada = sdf.format(cotiFechaEvento);


                reserva.setResComprobante(urla);
                reserva.setReCotiId(cotizacion);
                reserva.setResEstado(1);
                reserva.setUsuId(usuario);
                reserva.setResFechaEvento(fechaFormateada);
                System.out.println(fechaFormateada);
                // reserva.setResFechaEvento(new Date());

                // Enviar la reserva al servidor y guardarla
                enviarReservaAlServidor(reserva);
            }

            // Método para enviar la reserva al servidor y guardarla
            private void enviarReservaAlServidor(Reserva reserva) {
                String url = ConfigApi.baseUrlE + "/reserva/crear"; // URL para guardar la reserva en el servidor

                Gson gson = new Gson();
                String requestBody = gson.toJson(reserva);
                StringRequest request = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Manejar la respuesta del servidor
                                Log.d("TAG", "Response: " + response);
                                // Guardar la reserva en el servidor fue exitoso
                                Toast.makeText(pruebaActivity2.this, "¡ Tu reserva ha sido guardada con éxito!", Toast.LENGTH_SHORT).show();
                                // Crear un intent para volver a MainActivity con savedInstanceState como null después de 2 segundos
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(pruebaActivity2.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);

                                        // Finalizar la actividad actual
                                        finish();
                                    }
                                }, 2000);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // Manejar errores de la solicitud
                                Log.e("TAG", "Error: " + error.toString());
                                String response = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                                Log.e("TAG", "Error Response from Server: " + response);
                                Toast.makeText(pruebaActivity2.this, "¡No se pudo guardar su Reserva!", Toast.LENGTH_SHORT).show();
                            }
                        }
                ) {
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


//            // Método para enviar la reserva al servidor y guardarla
//            private void enviarReservaAlServidor(Reserva reserva) {
//                String url = ConfigApi.baseUrlE + "/reserva/crear"; // URL para guardar la reserva en el servidor
//
//                Gson gson = new Gson();
//                String requestBody = gson.toJson(reserva);
//                StringRequest request = new StringRequest(Request.Method.POST, url,
//                        new Response.Listener<String>() {
//                            @Override
//                            public void onResponse(String response) {
//                                // Manejar la respuesta del servidor
//                                Log.d("TAG", "Response: " + response);
//                                // Guardar la reserva en el servidor fue exitoso
//                                Toast.makeText(pruebaActivity2.this, "¡ Tu reserva ha sido guardada con éxito!", Toast.LENGTH_SHORT).show();
//
//                            }
//                        },
//                        new Response.ErrorListener() {
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
//                                // Manejar errores de la solicitud
//                                Log.e("TAG", "Error: " + error.toString());
//                                String response = new String(error.networkResponse.data, StandardCharsets.UTF_8);
//                                Log.e("TAG", "Error Response from Server: " + response);
//                                Toast.makeText(pruebaActivity2.this, "¡No se pudo guardar su Reserva!", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                ) {
//                    @Override
//                    public String getBodyContentType() {
//                        return "application/json";
//                    }
//
//                    @Override
//                    public byte[] getBody() throws AuthFailureError {
//                        return requestBody.getBytes(StandardCharsets.UTF_8);
//                    }
//                };
//
//                // Agregar la solicitud a la cola de solicitudes de Volley
//                queue.add(request);
//            }


        }


        public boolean validarfecha() {
            int year = datefecha.getYear();
            int month = datefecha.getMonth();
            int day = datefecha.getDayOfMonth();

            Calendar selectedDateCalendar = Calendar.getInstance();
            selectedDateCalendar.set(year, month, day);
            Date selectedDate = selectedDateCalendar.getTime();

            Calendar todayCalendar = Calendar.getInstance();
            Date today = todayCalendar.getTime();

            // Verificar si la fecha seleccionada es menor que la fecha actual
            if (selectedDate.before(today)) {
                return false;
            } else {
                // Aquí puedes realizar cualquier acción en caso de que la fecha seleccionada sea válida.
                return true;
            }
        }

        public boolean validaraño() {
            int year = datefecha.getYear();
            int month = datefecha.getMonth();
            int day = datefecha.getDayOfMonth();

            Calendar selectedDateCalendar = Calendar.getInstance();
            selectedDateCalendar.set(year, month, day);
            Date selectedDate = selectedDateCalendar.getTime();

            Calendar todayCalendar = Calendar.getInstance();
            Date today = todayCalendar.getTime();

            // Obtener el año actual
            int currentYear = todayCalendar.get(Calendar.YEAR);

            // Verificar si el año seleccionado es igual al año actual
            if (year == currentYear || year == currentYear + 1) {
                // Aquí puedes realizar cualquier acción en caso de que la fecha sea válida.
                return true;
            } else {
                toastIncorrecto("El año seleccionado debe ser igual al año actual.");
                return false;
            }
        }

        public boolean validarFechaAnticipacion() {
            int year = datefecha.getYear();
            int month = datefecha.getMonth();
            int day = datefecha.getDayOfMonth();

            Calendar selectedDateCalendar = Calendar.getInstance();
            selectedDateCalendar.set(year, month, day);
            Date selectedDate = selectedDateCalendar.getTime();

            Calendar fiveDaysLaterCalendar = Calendar.getInstance();
            fiveDaysLaterCalendar.add(Calendar.DAY_OF_MONTH, 5); // Agrega 5 días a la fecha actual
            Date fiveDaysLater = fiveDaysLaterCalendar.getTime();

            // Verificar si la fecha seleccionada es mayor o igual a 5 días en el futuro
            if (selectedDate.after(fiveDaysLater) || selectedDate.equals(fiveDaysLater)) {
                // La fecha seleccionada es válida (está a 5 días o más en el futuro)
                return true;
            } else {
                toastIncorrecto("La fecha de la reserva debe tener minimo 5 días de anticipación");
                return false;
            }
        }

        public void toastIncorrecto(String msg) {
            LayoutInflater layoutInflater = getLayoutInflater();
            View view = layoutInflater.inflate(R.layout.custom_toast_error, (ViewGroup) findViewById(R.id.ll_custom_toast_error));
            TextView txtMensaje = view.findViewById(R.id.txtMensajeToast2);
            txtMensaje.setText(msg);

            Toast toast = new Toast(getApplicationContext());
            toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM, 0, 200);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(view);
            toast.show();
        }

        public void toastCorrecto(String msg) {
            LayoutInflater layoutInflater = getLayoutInflater();
            View view = layoutInflater.inflate(R.layout.custom_toast_ok, (ViewGroup) findViewById(R.id.ll_custom_toast_ok));
            TextView txtMensaje = view.findViewById(R.id.txtMensajeToast1);
            txtMensaje.setText(msg);

            Toast toast = new Toast(getApplicationContext());
            toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM, 0, 200);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(view);
            toast.show();
        }
    }