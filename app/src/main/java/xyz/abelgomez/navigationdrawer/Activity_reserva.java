package xyz.abelgomez.navigationdrawer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import java.util.Date;

import xyz.abelgomez.navigationdrawer.api.ConfigApi;
import xyz.abelgomez.navigationdrawer.model.ImagenReserva;
import xyz.abelgomez.navigationdrawer.model.Reserva;
import xyz.abelgomez.navigationdrawer.model.Usuario;
import xyz.abelgomez.navigationdrawer.model.VolleyRequest;

public class Activity_reserva extends AppCompatActivity {

    private RequestQueue queue;

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String PREF_NAME = "MiPreferencia";
    private static final String KEY_USUARIO = "usuario";
    private ImageView imageView;
    private Button chooseButton;
    private Button uploadButton;

    Usuario usuario;
    private Bitmap selectedBitmap;
    private VolleyRequest volleyRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserva);
        imageView = findViewById(R.id.imageUser);
        chooseButton = findViewById(R.id.btnsubirarchivo);
        uploadButton = findViewById(R.id.Guardarreserva);

        volleyRequest = new VolleyRequest(this);
        queue = Volley.newRequestQueue(this);

        chooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadImageToServer();


            }
        });
    }

    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Selecciona una imagen"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            try {
                selectedBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                imageView.setImageBitmap(selectedBitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImageToServer() {
        String serverUrl = ConfigApi.baseUrlE + "/imagen/guardar-imagen";

        volleyRequest.uploadImageToServer(serverUrl, selectedBitmap,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(Activity_reserva.this, "Imagen cargada exitosamente", Toast.LENGTH_SHORT).show();
                        // La imagen se cargó con éxito, obtenemos el ID de la imagen
                        String imageId = response; // Esto depende de cómo el servidor responde con el ID
                        // Ahora podemos guardar la reserva con el ID de la imagen
                        enviarSolicitudGuardarReserva(imageId);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Activity_reserva.this, "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
                        Log.e("VolleyError", error.toString());
                    }
                });
    }


    private String convertirReservaAJson(Reserva reserva) {
        Gson gson = new Gson();
        return gson.toJson(reserva);
    }

    private void enviarSolicitudGuardarReserva(String imageId) {


        String serverUrl = ConfigApi.baseUrlE + "/reserva/guardar"; // Cambia esto a la dirección de tu servidor Spring Boot
        String dateString = "21/07/2023"; // Tu cadena de fecha
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date = dateFormat.parse(dateString);
            Reserva rese = new Reserva();

            rese.setResFechaEvento(date);
            rese.setResImagenRerserva(Integer.parseInt(imageId)); // Convierte el ID a int y asigna


            Gson gson = new Gson();
            String requestBody = gson.toJson(rese);


            // Crear una solicitud HTTP POST con la URL y el cuerpo de la solicitud
            StringRequest request = new StringRequest(Request.Method.POST, serverUrl,
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
                            Toast.makeText(Activity_reserva.this, "¡No se pudo guardar su cotizacion!", Toast.LENGTH_SHORT).show();

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
            queue.add(request);

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }


    private String obtenerImageIdDeRespuesta(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.has("id_imagen_reserva")) {
                Object idObject = jsonObject.get("id_imagen_reserva");
                if (idObject instanceof Integer) {
                    int imageId = (int) idObject;
                    return String.valueOf(imageId);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}