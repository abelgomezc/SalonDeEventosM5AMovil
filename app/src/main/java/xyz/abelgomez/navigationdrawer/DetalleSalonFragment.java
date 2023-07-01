package xyz.abelgomez.navigationdrawer;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import xyz.abelgomez.navigationdrawer.model.Salon;


public class DetalleSalonFragment extends Fragment {

    private Salon salon;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detalle_salon, container, false);

        // Obtener el ID del salón desde los argumentos
        Bundle bundle = getArguments();
        if (bundle != null) {
            int idSalon = bundle.getInt("idSalon");
            System.out.println("--------------------------------------------ID del salón seleccionado: " + idSalon);

            obtenerDatosSalon(idSalon);
        }

        return view;
    }

    private void obtenerDatosSalon(int idSalon) {
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        String url = "http://192.168.18.4:9999/salon/salonporid/" + idSalon;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Manejar la respuesta exitosa
                        Salon salon = parseSalonFromResponse(response);
                        mostrarDatosSalon(salon);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar el error de respuesta
                        error.printStackTrace();
                        Toast.makeText(getActivity(), "Error al obtener los datos del salón", Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(request);
    }

    private Salon parseSalonFromResponse(JSONObject response) {
        Salon salon = null;

        try {
            salon = new Salon();
            salon.setId_salon(response.getInt("salId"));
            salon.setNombre(response.getString("salNombre"));
            salon.setDireccion(response.getString("salDireccion"));
            salon.setCapacidad(response.getInt("salCapacidad"));
            salon.setCostoHora(response.getDouble("salCostoHora"));
            salon.setEstado(response.getBoolean("salEstado"));
            salon.setLatitud(response.getDouble("salLatitud"));
            salon.setLongitud(response.getDouble("salLongitud"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return salon;
    }


    private void mostrarDatosSalon(Salon salon) {

        TextView nombreTextView = getView().findViewById(R.id.nombreTextView);
        TextView direccionTextView = getView().findViewById(R.id.descripcionTextView);
        TextView cantiView = getView().findViewById(R.id.textViewCapacidad);
        TextView costoHoraView = getView().findViewById(R.id.textViewCostoHora);
        TextView estadoView  = getView().findViewById(R.id.textViewEstado);

        if (salon != null) {


            nombreTextView.setText(salon.getNombre());
            direccionTextView.setText(salon.getDireccion());
            //cantiView.setText(String.valueOf(salon.getCapacidad()));
            cantiView.setText(String.valueOf(salon.getCapacidad()) + " Personas");
            if (salon.isEstado() == true){

                estadoView.setText("Disponible.");

            }else{

                estadoView.setText("No Disponible.");
            }


            costoHoraView.setText("$  "+String.valueOf(salon.getCostoHora()));

//            // Mostrar los datos del salón en la consola
//            System.out.println("Datos del salón consultado:");
//            System.out.println("ID: " + salon.getId_salon());
//            System.out.println("Nombre: " + salon.getNombre());
//            System.out.println("Dirección: " + salon.getDireccion());
            // Mostrar otros datos según sea necesario
        }
    }







}



// Mostrar la imagen del Salon utilizando Glide u otra biblioteca de terceros
//            Glide.with(requireContext())
//                    .load(salon.getImagen())
//                    .into(imageView);
