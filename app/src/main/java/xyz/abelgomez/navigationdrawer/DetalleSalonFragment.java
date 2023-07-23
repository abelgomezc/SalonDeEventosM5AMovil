package xyz.abelgomez.navigationdrawer;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import xyz.abelgomez.navigationdrawer.api.ConfigApi;
import xyz.abelgomez.navigationdrawer.model.Salon;


public class DetalleSalonFragment extends Fragment {

    private Salon salon;
    private Button btnContizar;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detalle_salon, container, false);

        // obtener el ID de home
        Bundle bundle = getArguments();
        if (bundle != null) {
            int idSalon = bundle.getInt("idSalon");
            System.out.println("--------------------------------------------ID del salón seleccionado: " + idSalon);

            obtenerDatosSalon(idSalon);
            obtenerUrlsSalon(idSalon);
        }
       btnContizar = view.findViewById(R.id.btn_Cotizar);

        btnContizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear instancia del fragmento CotizarFragment
                FragmentCotitacion cotizarFragment = new FragmentCotitacion();

                // Establecer el objeto salon en el fragmento cotizarFragment
                cotizarFragment.setSalon(salon);

                // Obtener el FragmentManager y comenzar la transacción
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                // Reemplazar el fragmento actual con cotizarFragment
                fragmentTransaction.replace(R.id.fragment_container, cotizarFragment);

                // Agregar la transacción al BackStack
                fragmentTransaction.addToBackStack(null);

                // Ejecutar la transacción
                fragmentTransaction.commit();
            }
        });









        return view;
    }

    private void obtenerDatosSalon(int idSalon) {
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
      //  String url = "http://192.168.18.4:9999/salon/salonporid/" + idSalon;
        String url = ConfigApi.baseUrlE+"/salon/salonporid/"+idSalon;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // respuesta exitosa
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
         salon = null;

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


        }
    }


    private void obtenerUrlsSalon(int idSalon) {
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
       // String url = "http://192.168.18.4:9999/imgsalones/urls/" + idSalon;
        String url = ConfigApi.baseUrlE+"/imgsalones/urls/"+idSalon;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        mostrarUrlsSalon(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        error.printStackTrace();
                        Toast.makeText(getActivity(), "Error al obtener las URLs del salón", Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(request);
    }

    private void mostrarUrlsSalon(JSONArray response) {

     //   ImageView imageView = getView().findViewById(R.id.imageView);
        ImageSlider imageSlider = getView().findViewById(R.id.imageSlider);
        ArrayList<SlideModel> slideModels = new ArrayList<>();


        String imageUrl="";
        try {
            for (int i = 0; i < response.length(); i++) {
                 imageUrl = response.getString(i);
                // Reemplaza "localhost" con la dirección IP del servidor
           //  imageUrl = imageUrl.replace("localhost", "10.0.2.2");
              imageUrl = imageUrl.replace("localhost", "192.168.18.4");
                System.out.println("URL de imagen: " + imageUrl);
                slideModels.add(new SlideModel(imageUrl, ScaleTypes.FIT));

            }
            imageSlider.setImageList(slideModels,ScaleTypes.FIT);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}



// Mostrar la imagen del Salon utilizando Glide u otra biblioteca de terceros
//            Glide.with(requireContext())
//                    .load(salon.getImagen())
//                    .into(imageView);
