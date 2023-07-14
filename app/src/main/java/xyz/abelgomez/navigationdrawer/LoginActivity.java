package xyz.abelgomez.navigationdrawer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.sql.Time;

import xyz.abelgomez.navigationdrawer.api.ConfigApi;
import xyz.abelgomez.navigationdrawer.model.Usuario;
import xyz.abelgomez.navigationdrawer.utils.DateSerializer;
import xyz.abelgomez.navigationdrawer.utils.TimeSerializer;
import xyz.abelgomez.navigationdrawer.viewModel.UsuarioViewModel;

import android.content.SharedPreferences;


public class LoginActivity extends AppCompatActivity {
    private EditText edtMail, edtPassword;
    private Button btnIniciarSesion;
    private UsuarioViewModel viewModel;
    private TextInputLayout txtInputUsuario, txtInputPassword;
    private TextView txtNuevoUsuario;

    private static final String PREF_NAME = "MiPreferencia";
    private static final String KEY_USUARIO = "usuario";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.initViewModel();
        this.init();
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(UsuarioViewModel.class);
    }

    private void init() {

        edtMail = findViewById(R.id.edtMail);
        edtPassword = findViewById(R.id.edtPassword);
        txtInputUsuario = findViewById(R.id.txtInputUsuario);
        txtInputPassword = findViewById(R.id.txtInputPassword);
        txtNuevoUsuario = findViewById(R.id.txtNuevoUsuario);
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);

//        btnIniciarSesion.setOnClickListener(v -> {
//            startActivity(new Intent(this, MainActivity.class));
//        }


        btnIniciarSesion.setOnClickListener(v -> {

            if(validar()==true){

                String url = ConfigApi.baseUrlE+"/usuario/login/"+edtMail.getText().toString()+"/"+edtPassword.getText().toString();


                StringRequest request = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                if (response != null) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);


                                        Long usuId = jsonObject.getLong("usuId");
                                        String usuNombreUsuario = jsonObject.getString("usuNombreUsuario");
                                        String usuContrasena = jsonObject.getString("usuContrasena");

                                        // Crear una instancia de Usuario con los datos extraídos
                                        Usuario usuario = new Usuario(usuId, usuNombreUsuario, usuContrasena);
                                        //  System.out.println("Usuario xd    -----------+++++++++++++++++++++ " + usuario.getUsuNombreUsuario()+"======================"+usuario.getUsuContrasena());

                                        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString(KEY_USUARIO, usuNombreUsuario); // Reemplaza "nombreUsuario" con el valor obtenido del servidor
                                        editor.apply();
                                        toastCorrecto("Bienvenido  "+ usuNombreUsuario);
                                        // El inicio de sesión fue exitoso
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));




                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        toastIncorrecto(" Por favor , Verifique sus credenciales.");
                                    }
                                } else {
                                    // El inicio de sesión falló
                                    toastIncorrecto("Inicio de sesión fallido");
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // Se produjo un error en la solicitud
                                // Aquí puedes manejar errores de red o mostrar mensajes de error personalizados
                                toastIncorrecto("Error de red");
                            }
                        });

                // Agrega la solicitud a la cola de Volley para que se envíe
                Volley.newRequestQueue(this).add(request);


            }




        });




    }

        private boolean validar( ) {
        boolean retorno = true;
        String usuario, password;
        usuario = edtMail.getText().toString();
       password = edtPassword.getText().toString();
        if (usuario.isEmpty()) {
            txtInputUsuario.setError("Ingrese su usario y/o correo electrónico");
            retorno = false;
        } else {
            txtInputUsuario.setErrorEnabled(false);
        }
        if (password.isEmpty()) {
            txtInputPassword.setError("Ingrese su contraseña");
            retorno = false;
        } else {
            txtInputPassword.setErrorEnabled(false);
        }
        return retorno;
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

    @Override
    public void onBackPressed() {
        // Cerrar completamente la aplicación al presionar "Atrás"
        finishAffinity();
    }
}


//        btnIniciarSesion.setOnClickListener(v -> {
//            try {
//                if (validar()) {
//                    viewModel.login(edtMail.getText().toString(), edtPassword.getText().toString()).observe(this, response -> {
//                        if (response != null && response.getRpta() == 1) {
//                            toastCorrecto(response.getMessage());
//                            Usuario u = response.getBody();
//                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
//                            SharedPreferences.Editor editor = preferences.edit();
//                            final Gson g = new GsonBuilder()
//                                    .registerTypeAdapter(Date.class, new DateSerializer())
//                                    .registerTypeAdapter(Time.class, new TimeSerializer())
//                                    .create();
//                            editor.putString("UsuarioJson", g.toJson(u, new TypeToken<Usuario>() {
//                            }.getType()));
//                            editor.apply();
//                            edtMail.setText("");
//                            edtPassword.setText("");
//                            startActivity(new Intent(this, MainActivity.class));
//
//                        } else {
//                            toastIncorrecto("Correo o Contraseña equivocada, vuelve a intentar :D");
//                        }
//                    });
//                } else {
//                    toastIncorrecto("Por favor, complete todos los campos.");
//                }
//            } catch (Exception e) {
//                toastIncorrecto("Se ha producido un error al intentar ingresar : " + e.getMessage());
//            }
//        });



//    private boolean validar() {
//        boolean retorno = true;
//        String usuario, password;
//        usuario = edtMail.getText().toString();
//        password = edtPassword.getText().toString();
//        if (usuario.isEmpty()) {
//            txtInputUsuario.setError("Ingrese su usario y/o correo electrónico");
//            retorno = false;
//        } else {
//            txtInputUsuario.setErrorEnabled(false);
//        }
//        if (password.isEmpty()) {
//            txtInputPassword.setError("Ingrese su contraseña");
//            retorno = false;
//        } else {
//            txtInputPassword.setErrorEnabled(false);
//        }
//        return retorno;
//    }