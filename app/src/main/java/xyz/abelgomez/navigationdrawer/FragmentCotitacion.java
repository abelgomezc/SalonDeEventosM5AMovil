package xyz.abelgomez.navigationdrawer;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import xyz.abelgomez.navigationdrawer.api.ConfigApi;
import xyz.abelgomez.navigationdrawer.model.Producto;


public class FragmentCotitacion extends Fragment {

    private List<Producto> productosList;
    private Spinner spinnerProductos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cotitacion, container, false);

        spinnerProductos = view.findViewById(R.id.spinnerProductos);

        // Realizar la solicitud GET para obtener la lista de productos
       // String url = "URL_DE_TU_API/listar"; // Reemplaza con la URL correcta de tu API

        String url = ConfigApi.baseUrlE+"/productoServicio/listar";
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Procesar la respuesta JSON y obtener la lista de productos
                        productosList = parseProductosFromResponse(response);


                        ArrayAdapter<Producto> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, productosList);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerProductos.setAdapter(adapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.e("Volley", "Error en la solicitud: " + error.getMessage(), error);

                        Toast.makeText(requireContext(), "Error al obtener la lista de productos", Toast.LENGTH_SHORT).show();
                    }
                });

// Agregar la solicitud a la cola
        requestQueue.add(jsonArrayRequest);

        return view;
    }


    private List<Producto> parseProductosFromResponse(JSONArray response) {
        List<Producto> productosList = new ArrayList<>();

        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject jsonProducto = response.getJSONObject(i);

                int id = jsonProducto.getInt("prodId");
                String nombre = jsonProducto.getString("prodNombre");
                double precio = jsonProducto.getDouble("prodPrecio");

                Producto producto = new Producto(id, nombre, precio);
                productosList.add(producto);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return productosList;
    }

}