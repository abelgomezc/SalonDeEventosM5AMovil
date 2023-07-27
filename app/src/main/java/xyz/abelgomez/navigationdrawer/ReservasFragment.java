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
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import xyz.abelgomez.navigationdrawer.adapters.ReservaAdapter;
import xyz.abelgomez.navigationdrawer.api.ConfigApi;
import xyz.abelgomez.navigationdrawer.model.Usuario;

public class ReservasFragment extends Fragment {

    private static final String PREF_NAME = "MiPreferencia";
    private static final String KEY_USUARIO = "usuario";


    private ListView listViewReservas;

    private ReservaAdapter adapterReservas;
    private ArrayList<Map<String, Object>> reservas;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reservas, container, false);

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String usuarioJson = sharedPreferences.getString(KEY_USUARIO, "");
        Gson gson = new Gson();
        Usuario usuario = gson.fromJson(usuarioJson, Usuario.class);
        listViewReservas = view.findViewById(R.id.listViewTusReservas);

        reservas = new ArrayList<>();
        adapterReservas = new ReservaAdapter(requireContext(), reservas);
        listViewReservas.setAdapter(adapterReservas);

        obtenerReservas(usuario.getUsuId());
        return  view;
    }

    private void obtenerReservas(Long usuarioId) {
        String url = ConfigApi.baseUrlE + "/reserva/listar/" + usuarioId;



        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    // Resto del código
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            reservas.clear();
                            reservas.addAll(parsearReservas(jsonArray));
                            adapterReservas.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    // Resto del código
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error en la solicitud: " + error.getMessage(), error);
                        Toast.makeText(requireContext(), "Error al obtener las reservas", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String jsonString;
                try {
                    jsonString = new String(response.data, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    jsonString = new String(response.data);
                }
                return Response.success(jsonString, HttpHeaderParser.parseCacheHeaders(response));
            }
        };

        Volley.newRequestQueue(requireContext()).add(request);
    }




    private List<Map<String, Object>> parsearReservas(JSONArray jsonArray) {
        List<Map<String, Object>> reservas = new ArrayList<>();

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONArray reservaArray = jsonArray.getJSONArray(i);

                // Obtener los datos de la reserva del JSONArray
                String fechaEvento = reservaArray.getString(0);
                String tipoEvento = reservaArray.getString(1);
                int estado = reservaArray.getInt(2);
                double monto = reservaArray.getDouble(3);
                String nombreSalon = reservaArray.getString(4);
                String fechaFormateada = adaptarFecha(fechaEvento);

                // Crear un mapa con los datos de la reserva
                Map<String, Object> reservaMap = new HashMap<>();
                reservaMap.put("salonNombre", nombreSalon);
                reservaMap.put("monto", monto);
                reservaMap.put("fechaReserva", fechaFormateada);
                reservaMap.put("estado", estado);
                reservaMap.put("tipoEvento", tipoEvento);

                // Agregar la reserva al listado
                reservas.add(reservaMap);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return reservas;
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