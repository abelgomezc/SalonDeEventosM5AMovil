package xyz.abelgomez.navigationdrawer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TimePicker;
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
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import xyz.abelgomez.navigationdrawer.api.ConfigApi;
import xyz.abelgomez.navigationdrawer.model.Cotizacion;
import xyz.abelgomez.navigationdrawer.model.ImagenReserva;
import xyz.abelgomez.navigationdrawer.model.Reserva;
import xyz.abelgomez.navigationdrawer.model.Usuario;
import xyz.abelgomez.navigationdrawer.model.VolleyRequest;

import xyz.abelgomez.navigationdrawer.model.Reserva;
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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import xyz.abelgomez.navigationdrawer.api.ConfigApi;
import xyz.abelgomez.navigationdrawer.model.Reserva;

import xyz.abelgomez.navigationdrawer.model.Usuario;
public class Activity_reserva extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private TextView textViewCotiId;
    private TextView txtInformacionReserva;
    DatePicker datefecha;

    private Button btnSubirIma;
    private String selectedFilePath = "";
    private static final int REQUEST_CODE_PICK_FILE = 1;

    String fileName="";
    //  String uploadedFileName =  ConfigApi.baseUrlE+"/file/filesImg/"+fileName;
    String uploadedFileName = "http://localhost:9999/file/filesImg/"+fileName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserva);
        datefecha = findViewById(R.id.datePickerfecha);

        Log.d("Activity_reserva", "La actividad se ha creado correctamente");

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
                if (validarfecha()) {

                    if(validaraño()) {
                        guardarReservaConImagen();
                    }
                }

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
                            Log.d("Activity_reserva", "El path del archivo es nulo");
                        }
                    } else {
                        Log.d("Activity_reserva", "El archivo no se puede abrir");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Log.d("Activity_reserva|", "La URI es nula o no tiene el esquema 'content'");
            }
        }
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

            new FileUploadTask().execute(new File(selectedFilePath));
        } else {
            Toast.makeText(Activity_reserva.this, "Por favor, seleccione un archivo antes de guardar la reserva", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(Activity_reserva.this, "Error al subir el archivo", Toast.LENGTH_SHORT).show();
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
            // Crea una nueva reserva con los datos requeridos

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

            Reserva reserva = new Reserva();
            reserva.setResComprobante(uploadedFileName);

            reserva.setResFechaEvento(fechaFormateada);
            System.out.println(fechaFormateada);
            // Obtén el ID de cotización desde los extras
            Intent intent = getIntent();
            long cotiId = intent.getLongExtra("cotiId", -1);

            // Asigna el ID de cotización a la reserva
            reserva.setResId(cotiId);

            // Guarda la reserva en el servidor utilizando una solicitud HTTP con JSONObjectRequest
            RequestQueue requestQueue = Volley.newRequestQueue(Activity_reserva.this);
            String url = ConfigApi.baseUrlE + "/reserva/crear"; // URL para guardar la reserva en el servidor

            try {
                Cotizacion coti=new Cotizacion();
                coti.setCotiId(cotiId);

                reserva.setReCotiId(coti);

                JSONObject reservaJson = new JSONObject();
                reservaJson.put("resComprobante", reserva.getResComprobante());
                reservaJson.put("reCotiId", cotiId);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, reservaJson,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // Aquí puedes manejar la respuesta del servidor después de guardar la reserva
                                Toast.makeText(Activity_reserva.this, "Reserva guardada con éxito", Toast.LENGTH_SHORT).show();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // Aquí puedes manejar el error en caso de que ocurra algún problema al guardar la reserva
                                Toast.makeText(Activity_reserva.this, "Error al guardar la reserva", Toast.LENGTH_SHORT).show();
                            }
                        });

                // Agrega la solicitud a la cola de solicitudes
                requestQueue.add(jsonObjectRequest);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }





    }

    public boolean validarfecha(){
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