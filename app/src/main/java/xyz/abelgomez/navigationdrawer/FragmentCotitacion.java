package xyz.abelgomez.navigationdrawer;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import xyz.abelgomez.navigationdrawer.adapters.AdapterHome;
import xyz.abelgomez.navigationdrawer.api.ConfigApi;
import xyz.abelgomez.navigationdrawer.model.Cotizacion;
import xyz.abelgomez.navigationdrawer.model.Producto;
import xyz.abelgomez.navigationdrawer.model.Salon;


public class FragmentCotitacion extends Fragment {

    private Button btncalcularcoti, btnguardarcoti;
    private TextInputLayout txtsillita, txtmesita, txtdescrion, txtmontocoti, txthoritas, txtmantelcito;
    private EditText edtmesa, edtmantelcito;
    private EditText edtsilla;
    private EditText edtdescripcion;
    private EditText edtmontocoti;
    private EditText edthorias;
    // private CotizacionViewModel cotizacionViewModel;
    ArrayList<String> arraynombres;
    ArrayAdapter<String> adapternombres;
    private ArrayList<Producto> prod;
    private AdapterHome adapter1;
    private ListView listView;
    TimePicker timePickerinicio, timePickerfinal;
    DatePicker datefecha;


    private List<Producto> productosList;
    private Spinner spinnerProductos;
    private RequestQueue queue;




    private Salon salon;


    public void setSalon(Salon salon) {
        this.salon = salon;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cotitacion, container, false);




        spinnerProductos = view.findViewById(R.id.spinner);
        edtdescripcion = view.findViewById(R.id.edtdescripcion);
        txtdescrion = view.findViewById(R.id.txtdescripcionsalon);
        edtmantelcito = view.findViewById(R.id.edtMantel);
        txtmantelcito = view.findViewById(R.id.txtMantel);

        txthoritas = view.findViewById(R.id.txthoritas);
        edthorias = view.findViewById(R.id.edthoritas);

        datefecha = view.findViewById(R.id.datePickerfecha);

        edtmontocoti = view.findViewById(R.id.edttotal);
        edtmontocoti.setKeyListener(null);
        txtmontocoti = view.findViewById(R.id.txtTotalcoti);
        txtsillita = view.findViewById(R.id.txtSilla);
        txtmesita = view.findViewById(R.id.txtMesa);
        edtmesa = view.findViewById(R.id.edtMesa);
        edtsilla = view.findViewById(R.id.edtSillas);
        timePickerinicio = view.findViewById(R.id.timerinicio);
        timePickerfinal = view.findViewById(R.id.timerfinal);

        btncalcularcoti = view.findViewById(R.id.btncalcularcoti);
        btnguardarcoti = view.findViewById(R.id.btnGuardarCoti);


        String url = ConfigApi.baseUrlE + "/productoServicio/listar";
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




        // Realizar la solicitud GET para obtener la lista de productos
        String url1 = ConfigApi.baseUrlE + "/productoServicio/listar";
        JsonArrayRequest jsonArrayRequest1 = new JsonArrayRequest(Request.Method.GET, url1, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Procesar la respuesta JSON y obtener la lista de productos
                        productosList = parseProductosFromResponse(response);
                        Producto seleccionarOpcion = new Producto();
                        seleccionarOpcion.setId(0);
                        seleccionarOpcion.setNombre("Seleccione una opción");
                        List<Producto> productosListWithOption = new ArrayList<>();
                        productosListWithOption.add(seleccionarOpcion);
                        productosListWithOption.addAll(productosList);

                        ArrayAdapter<Producto> adapter1 = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, productosList);
                        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerProductos.setAdapter(adapter1);


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error en la solicitud: " + error.getMessage(), error);
                        Toast.makeText(requireContext(), "Error al obtener la lista de productos", Toast.LENGTH_SHORT).show();
                    }
                });

        btncalcularcoti.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (validarcalculo() == true) {
                    calcular();
                    //   calcularhora();
                }

            }
        });
        btnguardarcoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validarenvio() == true) {


                    enviarCotizacion();


                }
            }
        });
        // Agregar la solicitud a la cola
        requestQueue.add(jsonArrayRequest);


        queue = Volley.newRequestQueue(getActivity());
        txtsillita.setVisibility(View.GONE);
        txtmesita.setVisibility(View.GONE);

        edtmesa.setVisibility(View.GONE);
        edtsilla.setVisibility(View.GONE);
        edtmantelcito.setVisibility(View.GONE);
        txtmantelcito.setVisibility(View.GONE);

        spinnerProductos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if (selectedItem.equals("silla")) {
                    txtsillita.setVisibility(View.VISIBLE);
                    edtsilla.setVisibility(View.VISIBLE);
                    edtmesa.setVisibility(View.GONE);
                    txtmesita.setVisibility(View.GONE);
                    edtmantelcito.setVisibility(View.GONE);
                    txtmantelcito.setVisibility(View.GONE);

                }
                if (selectedItem.equals("mesa")) {
                    txtmesita.setVisibility(View.VISIBLE);
                    edtmesa.setVisibility(View.VISIBLE);
                    edtsilla.setVisibility(View.GONE);
                    txtsillita.setVisibility(View.GONE);
                    edtmantelcito.setVisibility(View.GONE);
                    txtmantelcito.setVisibility(View.GONE);
                }
                if (selectedItem.equals("mantel")) {
                    txtmesita.setVisibility(View.GONE);
                    edtmesa.setVisibility(View.GONE);
                    edtsilla.setVisibility(View.GONE);
                    txtsillita.setVisibility(View.GONE);
                    edtmantelcito.setVisibility(View.VISIBLE);
                    txtmantelcito.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No se realiza ninguna acción cuando no hay opción seleccionada
            }
        });


        queue = Volley.newRequestQueue(getActivity());


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


    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if (position == 0) {

        } else if (position == 1) {
            txtsillita.setVisibility(View.VISIBLE);
            edtsilla.setVisibility(View.VISIBLE);
            edtmesa.setVisibility(View.GONE);
            txtmesita.setVisibility(View.GONE);

        } else if (position == 2) {
            txtmesita.setVisibility(View.VISIBLE);
            edtmesa.setVisibility(View.VISIBLE);
            edtsilla.setVisibility(View.GONE);
            txtsillita.setVisibility(View.GONE);
        }
    }

    private void calcular() {

        //   timePicker1 = findViewById(R.id.timerinicio);
        int startHour = timePickerinicio.getHour();
        int startMinute = timePickerinicio.getMinute();
        //      timePicker2 = findViewById(R.id.timerfinal);
        int endHour = timePickerfinal.getHour();
        int endMinute = timePickerfinal.getMinute();

// Crea objetos Calendar para las horas seleccionadas
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.set(Calendar.HOUR_OF_DAY, startHour);
        startCalendar.set(Calendar.MINUTE, startMinute);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.set(Calendar.HOUR_OF_DAY, endHour);
        endCalendar.set(Calendar.MINUTE, endMinute);

// Comprueba si la hora de finalización es anterior a la hora de inicio
        if (endCalendar.before(startCalendar)) {
            endCalendar.add(Calendar.DATE, 1); // agrega un día a la hora de finalización
        }

// Calcula la diferencia de tiempo entre las horas seleccionadas
        long diffInMillis = endCalendar.getTimeInMillis() - startCalendar.getTimeInMillis();
        double diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis);
        //double diffInHours = TimeUnit.MILLISECONDS.toHours(diffInMillis);
        double diffInHours = diffInMinutes / 60.0;  // Dividir los minutos entre 60 para obtener el valor decimal de horas
        edthorias.setText("La diferencia de tiempo es de " + diffInHours + " horas y " + (diffInMinutes % 60) + " minutos.");



        double costohora = diffInHours * 100;
        int value1 = Integer.parseInt(edtmantelcito.getText().toString()) * 10;
        int value2 = Integer.parseInt(edtmesa.getText().toString()) * 15;
        int value3 = Integer.parseInt(edtsilla.getText().toString()) *120;

        double sum = value2 + value3 + value1 + costohora;

        edtmontocoti.setText(String.valueOf(sum));

    }


    private void enviarCotizacion() {

        String url = ConfigApi.baseUrlE + "/cotizacion/crearcoti";
        // Crear una instancia de la clase Persona con los datos que deseas enviar
        Cotizacion coti = new Cotizacion();
        coti.setCotiDescripcion(edtdescripcion.getText().toString());
        coti.setCotiEstado(1);
        int hora = timePickerinicio.getCurrentHour();
        int minuto = timePickerinicio.getCurrentMinute();
        int seg = 25;
        LocalTime horaLocal = LocalTime.of(hora, minuto, seg);
        String horainicio1 = horaLocal.toString();


        int hora1 = timePickerfinal.getCurrentHour();
        int minuto1 = timePickerfinal.getCurrentMinute();
        int seg1 = 25;
        LocalTime horaLocal1 = LocalTime.of(hora1, minuto1, seg1);
        String horafin1 = horaLocal1.toString();

        int year = datefecha.getYear();
        int month = datefecha.getMonth();
        int day = datefecha.getDayOfMonth();

// Formatear la fecha en una cadena de texto
        String fechaSeleccionada = String.format("%02d/%02d/%04d", day, month + 1, year);


        coti.setCotiHoraInicio(horainicio1);
        coti.setCotiHoraFin(horafin1);
        coti.setCotiFechaEvento(fechaSeleccionada);
        //coti.setCotiFechaRegistro(fechahoy);
        coti.setCotiMonto(Double.parseDouble(edtmontocoti.getText().toString()));
        coti.setCotiTipoEvento("cumpleaños");

        spinnerProductos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                       @Override
                                                       public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                           Producto producto = (Producto) parent.getItemAtPosition(position);
                                                           coti.setProducto(producto);
                                                       }

                                                       @Override
                                                       public void onNothingSelected(AdapterView<?> parent) {
                                                           Toast.makeText(requireContext(), "NO HA SELCCIONADO UN PRODUCTO", Toast.LENGTH_SHORT).show();
                                                       }
                                                   }
        );


        // Convertir el objeto Persona a JSON utilizando la biblioteca Gson
        Gson gson = new Gson();
        String requestBody = gson.toJson(coti);

        // Crear una solicitud HTTP POST con la URL y el cuerpo de la solicitud
        StringRequest request = new StringRequest(Request.Method.POST, url,
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
                        Toast.makeText(getContext(), "¡No se pudo guardar su cotizacion!", Toast.LENGTH_SHORT).show();

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

        // Agregar la solicitud a la cola de solicitudes de Volley
        queue.add(request);
    }


    private boolean validarenvio() {
        boolean retorno = true;
        String descripcion, sillas, mesa, monto, nombre;
        descripcion = edtdescripcion.getText().toString();
        monto = edtmontocoti.getText().toString();
        sillas = edtsilla.getText().toString();
        mesa = edtmesa.getText().toString();
        if (descripcion.isEmpty()) {
            txtdescrion.setError("Ingrese la descripcion del salon");
            retorno = false;
        } else {
            txtdescrion.setErrorEnabled(false);
        }
        if (monto.isEmpty()) {
            txtmontocoti.setError("Realize el calculo de la cotizacion");
            retorno = false;
        } else {
            txtmontocoti.setErrorEnabled(false);
        }

        return retorno;
    }

    private boolean validarcalculo() {
        boolean retorno = true;
        String descripcion, sillas, mesa, monto, nombre;
        descripcion = edtdescripcion.getText().toString();
        monto = edtmontocoti.getText().toString();
        sillas = edtsilla.getText().toString();
        mesa = edtmesa.getText().toString();
        if (sillas.isEmpty()) {
            txtsillita.setError("Ingrese cuantas sillas desea");
            retorno = false;
        } else {
            txtsillita.setErrorEnabled(false);
        }
        if (mesa.isEmpty()) {
            txtmesita.setError("Ingrese cuantas mesas necesita");
            retorno = false;
        } else {
            txtmesita.setErrorEnabled(false);
        }
        return retorno;
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

    private boolean guardarCotizacion() {
        // Aquí debes agregar el código para guardar la cotización en la base de datos
        // Si la cotización se guarda correctamente, debes devolver true, de lo contrario, false.
        return true;
    }
    public void limpiar(){

    }



}