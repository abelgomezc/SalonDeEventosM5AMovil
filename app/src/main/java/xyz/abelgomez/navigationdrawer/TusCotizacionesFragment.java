package xyz.abelgomez.navigationdrawer;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import xyz.abelgomez.navigationdrawer.adapters.AdapterCotizaciones;
import xyz.abelgomez.navigationdrawer.api.ConfigApi;
import xyz.abelgomez.navigationdrawer.model.Cotizacion;
import xyz.abelgomez.navigationdrawer.model.Usuario;


public class TusCotizacionesFragment extends Fragment {

    private static final String PREF_NAME = "MiPreferencia";
    private static final String KEY_USUARIO = "usuario";

    private ListView listViewCotizaciones;

    private AdapterCotizaciones adapterCotizaciones;
    private ArrayList<Map<String, Object>> cotizaciones;

    TextView titulo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tus_cotizaciones, container, false);

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String usuarioJson = sharedPreferences.getString(KEY_USUARIO, "");
        Gson gson = new Gson();
        Usuario usuario = gson.fromJson(usuarioJson, Usuario.class);
        titulo= view.findViewById(R.id.textViewTitulo);

        titulo.setText("Tus Cotizaciones");
        listViewCotizaciones = view.findViewById(R.id.listViewCotizaciones1);



        cotizaciones = new ArrayList<>();
        adapterCotizaciones = new AdapterCotizaciones(requireContext(), cotizaciones);
        listViewCotizaciones.setAdapter(adapterCotizaciones);

        obtenerCotizaciones(usuario.getUsuId());

        return  view;
    }

    private void obtenerCotizaciones(Long usuarioId) {
        String url = ConfigApi.baseUrlE + "/cotizacion/cotizaciones-usuario/" + usuarioId;

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            cotizaciones.clear();
                            cotizaciones.addAll(parsearCotizaciones(jsonArray));
                            adapterCotizaciones.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error en la solicitud: " + error.getMessage(), error);
                        Toast.makeText(requireContext(), "Error al obtener las cotizaciones", Toast.LENGTH_SHORT).show();
                    }
                });

        Volley.newRequestQueue(requireContext()).add(request);
    }




    private List<Map<String, Object>> parsearCotizaciones(JSONArray jsonArray) {
        List<Map<String, Object>> cotizaciones = new ArrayList<>();

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                // Decodificar la cadena JSON utilizando la codificación UTF-8
                String jsonString = jsonArray.getJSONObject(i).toString();
                String decodedJsonString = new String(jsonString.getBytes("ISO-8859-1"), "UTF-8");
                JSONObject jsonObject = new JSONObject(decodedJsonString);

                // Obtener los datos de la cotización del JSON
                double monto = jsonObject.getDouble("monto");
                String salonNombre = jsonObject.getString("salonNombre");
                String fechaReserva = jsonObject.getString("fechaReserva");

                // Adaptar la fecha a un formato deseado
                String fechaFormateada = adaptarFecha(fechaReserva);

                // Crear un mapa con los datos de la cotización
                Map<String, Object> cotizacionMap = new HashMap<>();
                cotizacionMap.put("salonNombre", salonNombre);
                cotizacionMap.put("monto", monto);
                cotizacionMap.put("fechaReserva", fechaFormateada);

                // Agregar la cotización al listado
                cotizaciones.add(cotizacionMap);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return cotizaciones;
    }

    private String adaptarFecha(String fecha) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.getDefault());
            Date parsedDate = inputFormat.parse(fecha);

            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            return outputFormat.format(parsedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }


}