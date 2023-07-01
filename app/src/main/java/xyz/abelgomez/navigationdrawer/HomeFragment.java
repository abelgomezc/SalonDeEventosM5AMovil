package xyz.abelgomez.navigationdrawer;

import static java.io.File.createTempFile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;

import java.util.List;

import xyz.abelgomez.navigationdrawer.adapters.AdapterHome;
import xyz.abelgomez.navigationdrawer.model.Salon;


public class HomeFragment extends Fragment {

    private ArrayList<Salon> salones;
    private AdapterHome adapter;

    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

    listView = view.findViewById(R.id.listaView);


        listView = view.findViewById(R.id.listaView);

        // Realizar la solicitud para obtener los salones
        obtenerSalones();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Obtener el objeto Salon seleccionado
                Salon salon = salones.get(position);

                // Obtener el ID del salón seleccionado
                int idSalon = salon.getId_salon();

                // Crear una instancia del fragmento DetalleSalonFragment
                DetalleSalonFragment detalleSalonFragment = new DetalleSalonFragment();

                // Pasar el ID del salón al fragmento DetalleSalonFragment
                Bundle bundle = new Bundle();
                bundle.putInt("idSalon", idSalon);
                detalleSalonFragment.setArguments(bundle);

                // Reemplazar el fragmento actual por el fragmento DetalleSalonFragment
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, detalleSalonFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });



        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Limpia la lista de salones si existe
        if (salones != null) {
            salones.clear();
        }

        // Notifica al adaptador para que se actualice
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }

        // Realiza una nueva solicitud para obtener los salones actualizados
        obtenerSalones();
    }

    private void obtenerSalones() {
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        String url = "http://192.168.18.4:9999/salon/listar";

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Manejar la respuesta exitosa
                        ArrayList<Salon> salones = parseSalonesFromResponse(response);
                        mostrarSalones(salones);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar el error de respuesta
                        error.printStackTrace();
                        Toast.makeText(getActivity(), "Error al obtener los salones", Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(request);
    }

    private void mostrarSalones(ArrayList<Salon> salones) {
        this.salones = salones;
        adapter = new AdapterHome(getActivity(), salones);
        listView.setAdapter(adapter);
    }


    private ArrayList<Salon> parseSalonesFromResponse(String response) {
        ArrayList<Salon> salones = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(response);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonSalon = jsonArray.getJSONObject(i);
                Salon salon = new Salon();
                salon.setId_salon(jsonSalon.getInt("salId"));
                salon.setNombre(jsonSalon.getString("salNombre"));
                salon.setDireccion(jsonSalon.getString("salDireccion"));
                salon.setCapacidad(jsonSalon.getInt("salCapacidad"));
                salon.setCostoHora(jsonSalon.getDouble("salCostoHora"));
                salon.setEstado(jsonSalon.getBoolean("salEstado"));



                salon.setLatitud(jsonSalon.getDouble("salLatitud"));
                salon.setLongitud(jsonSalon.getDouble("salLongitud"));
                salones.add(salon);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return salones;
    }






    private File createTempFile(InputStream inputStream) throws IOException {
        File tempFile = File.createTempFile("temp_", null);

        FileOutputStream outputStream = new FileOutputStream(tempFile);
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        outputStream.close();
        inputStream.close();

        return tempFile;
    }

}




//    // Agregar objetos Salon de prueba
//    Salon salon2 = new Salon();
//        salon2.setNombre("Salón 2");
//                salon2.setDireccion("Descripción del Salón 2");
//
//
//
//
//                salones.add(salon2);/////
//
//                Salon salon1 = new Salon();
//                salon1.setNombre("Salón 1");
//                salon1.setDireccion("Descripción del Salón 1");
//
//
//                salones.add(salon1);///
//
//
//                Salon salon3 = new Salon();
//                salon3.setNombre("Salón 3");
//                salon3.setDireccion("Descripción del Salón 3");
//
//
//                salones.add(salon3);//
//                Salon salon4 = new Salon();
//                salon4.setNombre("Salón 4");
//                salon4.setDireccion("Descripción del Salón 4");
//
//
//                salones.add(salon4);


    // String url = "http://10.0.2.2.4:9999/salon/listar";//emulador
  //  String url = "http://192.168.18.4:9999/salon/listar";//exterior celu




//
//public class HomeFragment extends Fragment {
//
//    private ArrayList<Salon> salones;
//    private AdapterHome adapter;
//
//    private ListView listView;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_home, container, false);
//
//        ListView listView = view.findViewById(R.id.listaView);
//
//
//        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
//        // String url = "http://10.0.2.2.4:9999/salon/listar";//emulador
//        String url = "http://192.168.18.4:9999/salon/listar";//exterior celu
//
//        StringRequest request = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        // Manejar la respuesta exitosa
//                        ArrayList<Salon> salones = parseSalonesFromResponse(response);
//                        adapter = new AdapterHome(getActivity(), salones);
//                        listView.setAdapter(adapter);
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        // Manejar el error de respuesta
//
//                        error.printStackTrace();
//                        Toast.makeText(getActivity(), "Error al obtener los salones", Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//        requestQueue.add(request);
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                // Obtener el objeto Salon seleccionado
//                Salon salon = salones.get(position);
//
//                // Crear una instancia del fragmento DetalleSalonFragment
//                DetalleSalonFragment detalleSalonFragment = new DetalleSalonFragment();
//
//                // Pasar los datos del objeto Salon al fragmento DetalleSalonFragment
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("salon", salon);
//                detalleSalonFragment.setArguments(bundle);
//
//                // Reemplazar el fragmento actual por el fragmento DetalleSalonFragment
//                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
//                fragmentManager.beginTransaction()
//                        .replace(R.id.fragment_container, detalleSalonFragment)
//                        .addToBackStack(null)
//                        .commit();
//            }
//        });
//
//        return view;
//    }
//
//
//    private ArrayList<Salon> parseSalonesFromResponse(String response) {
//        ArrayList<Salon> salones = new ArrayList<>();
//
//        try {
//            JSONArray jsonArray = new JSONArray(response);
//            for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject jsonSalon = jsonArray.getJSONObject(i);
//                Salon salon = new Salon();
//                salon.setId_salon(jsonSalon.getInt("salId"));
//                salon.setNombre(jsonSalon.getString("salNombre"));
//                salon.setDireccion(jsonSalon.getString("salDireccion"));
//                salon.setCapacidad(jsonSalon.getInt("salCapacidad"));
//                salon.setCostoHora(jsonSalon.getDouble("salCostoHora"));
//                salon.setEstado(jsonSalon.getBoolean("salEstado"));
//
//
//
//                salon.setLatitud(jsonSalon.getDouble("salLatitud"));
//                salon.setLongitud(jsonSalon.getDouble("salLongitud"));
//                salones.add(salon);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        return salones;
//    }
//
