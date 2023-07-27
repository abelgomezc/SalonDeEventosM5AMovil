package xyz.abelgomez.navigationdrawer;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
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

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import xyz.abelgomez.navigationdrawer.adapters.AdapterHome;
import xyz.abelgomez.navigationdrawer.api.ConfigApi;
import xyz.abelgomez.navigationdrawer.model.Cotizacion;
import xyz.abelgomez.navigationdrawer.model.Producto;
import xyz.abelgomez.navigationdrawer.model.Salon;
import xyz.abelgomez.navigationdrawer.model.Salon2;
import xyz.abelgomez.navigationdrawer.model.Usuario;


public class FragmentCotitacion extends Fragment {


    private Button btncalcularcoti, btnguardarcoti;
    private TextInputLayout txtsillita, txtmesita, txtdescrion, txtmontocoti, txthoritas, txtmantelcito;
    private EditText edtmesa, edtmantelcito, edtnomb;
    private EditText edtsilla;
    private EditText edtdescripcion;
    private EditText edtmontocoti,edttipoevento;
    private EditText edthorias;
    // private CotizacionViewModel cotizacionViewModel;
    ArrayList<String> arraynombres;
    ArrayAdapter<String> adapternombres;
    private ArrayList<Producto> prod;
    private AdapterHome adapter1;
    private ListView listView;
    TimePicker timePickerinicio, timePickerfinal;
    Spinner spinnerEventos;
    String seleccion;
    private double resultadoTotal = 0.0;

    private static final String PREF_NAME = "MiPreferencia";
    private static final String KEY_USUARIO = "usuario";
    private List<Producto> productosList;
    private Spinner spinnerProductos;
    double precioProductoSeleccionado;
    private double precioTotalSeleccionado = 0.0;
    private RequestQueue queue;
    Usuario usuario;
    View view;
    Salon salon;

    Salon2 salon2;

    public void setSalon(Salon2 salon2) {
      this.salon2=salon2;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cotitacion, container, false);


        spinnerProductos = view.findViewById(R.id.spinner);
        edtdescripcion = view.findViewById(R.id.edtdescripcion);
        txtdescrion = view.findViewById(R.id.txtdescripcionsalon);
        edtmantelcito = view.findViewById(R.id.edtMantel);
        txtmantelcito = view.findViewById(R.id.txtMantel);

        edtnomb = view.findViewById(R.id.edtNombresalon);
        edttipoevento=view.findViewById(R.id.edttipo);
        txthoritas = view.findViewById(R.id.txthoritas);
        edthorias = view.findViewById(R.id.edthoritas);
        spinnerEventos = view.findViewById(R.id.spinnerEventos);

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

// Obtener el objeto salon que fue configurado en el FragmentDetalleSalon
        if (salon2 != null) {
            // Imprimir los datos en la consola
            System.out.println("Datos del salon:");
            System.out.println("ID: " + salon2.getSalId());
            System.out.println("Nombre: " + salon2.getSalNombre());
            System.out.println("Dirección: " + salon2.getSalDireccion());
            System.out.println("Capacidad: " + salon2.getSalCapacidad());
            System.out.println("Costo por hora: " + salon2.getSalCostoHora());
            System.out.println("Estado: " + salon2.getSalEstado());
            System.out.println("Latitud: " + salon2.getSalLatitud());
            System.out.println("Longitud: " + salon2.getSalLongitud());
            // Puedes seguir imprimiendo los demás campos que tenga el objeto salon
        } else {
            System.out.println("El objeto salon es nulo. Asegúrate de configurarlo correctamente desde FragmentDetalleSalon.");
        }

        edtdescripcion.setText(salon2.getSalDireccion());
        edtnomb.setText(salon2.getSalNombre());

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String usuarioJson = sharedPreferences.getString(KEY_USUARIO, "");
        Gson gson = new Gson();
        usuario = gson.fromJson(usuarioJson, Usuario.class);


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

                    if(validarduracionhoras()){
                        calcular();
                    }

                    } else {


                      toastIncorrecto("No puede seleccionar una fecha menor a la fecha actual");

                   }

                    //   calcularhora();
                }


        });

        btnguardarcoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validarenvio() == true) {

                            mostrarConfirmacion();
                    // enviarCotizacion();
                    // enviarCotizacion1();
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
                    Producto productoSeleccionado = (Producto) parent.getItemAtPosition(position);

                    precioProductoSeleccionado = productoSeleccionado.getPrecio();
                    System.out.println("precio:" +precioProductoSeleccionado);


                }
                if (selectedItem.equals("mesa")) {
                    txtmesita.setVisibility(View.VISIBLE);
                    edtmesa.setVisibility(View.VISIBLE);
                    edtsilla.setVisibility(View.GONE);
                    txtsillita.setVisibility(View.GONE);
                    edtmantelcito.setVisibility(View.GONE);
                    txtmantelcito.setVisibility(View.GONE);
                    Producto productoSeleccionado = (Producto) parent.getItemAtPosition(position);
                    precioProductoSeleccionado = productoSeleccionado.getPrecio();
                    System.out.println("precio:" +precioProductoSeleccionado);

                }
                if (selectedItem.equals("mantel")) {
                    txtmesita.setVisibility(View.GONE);
                    edtmesa.setVisibility(View.GONE);
                    edtsilla.setVisibility(View.GONE);
                    txtsillita.setVisibility(View.GONE);
                    edtmantelcito.setVisibility(View.VISIBLE);
                    txtmantelcito.setVisibility(View.VISIBLE);
                    Producto productoSeleccionado = (Producto) parent.getItemAtPosition(position);

                    precioProductoSeleccionado = productoSeleccionado.getPrecio();
                    System.out.println("precio:" +precioProductoSeleccionado);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No se realiza ninguna acción cuando no hay opción seleccionada
            }
        });

        spinnerEventos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if (selectedItem.equals("Boda")) {
                    edttipoevento.setText("Boda");
                }
                if (selectedItem.equals("Graduación")) {
                    edttipoevento.setText("Graduación");
                }
                if (selectedItem.equals("Bautizo")) {
                    edttipoevento.setText("Bautizo");
                }
                if (selectedItem.equals("Confirmación")) {
                    edttipoevento.setText("Confirmación");
                }
                if (selectedItem.equals("Cumpleaños")) {
                    edttipoevento.setText("Cumpleaños");
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

    private void enviarCotizacion1() {
        // Aquí colocas la lógica para enviar la cotización
        Toast.makeText(getActivity(), "Cotización enviada con éxito", Toast.LENGTH_SHORT).show();
    }

    private void mostrarConfirmacion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Confirmación");
        builder.setMessage("¿Estás seguro de que deseas enviar la cotización?");
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                enviarCotizacion();
            }
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
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


        double costohora = diffInHours * salon2.getSalCostoHora();
        System.out.println("precio:" + salon2.getSalCostoHora());
        Double value1 = Double.parseDouble(edtmantelcito.getText().toString()) * precioProductoSeleccionado;
        Double value2 = Double.parseDouble(edtmesa.getText().toString()) * precioProductoSeleccionado;
        Double value3 = Double.parseDouble(edtsilla.getText().toString()) * precioProductoSeleccionado;

        double sum = value1+value2+value3+ costohora;

        edtmontocoti.setText(String.valueOf(sum));

    }


   /* private void createNewTxtFile(String contenido) {
        String filename = "producto_" + System.currentTimeMillis() + ".txt";

        try {
            FileOutputStream fos =this.openFileOutput(filename, Context.MODE_PRIVATE);
            fos.write(contenido.getBytes());
            fos.close();
            Log.d("FileCreation", "Archivo creado: " + filename);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("FileCreation", "Error al crear el archivo: " + e.getMessage());
        }
    }*/


    private void enviarCotizacion() {

        String url = ConfigApi.baseUrlE + "/cotizacion/crear";
        // Crear una instancia de la clase Persona con los datos que deseas enviar
        Cotizacion coti = new Cotizacion();
        coti.setCotiDescripcion(edtdescripcion.getText().toString());
        coti.setCotiEstado(1);
        int hora = timePickerinicio.getCurrentHour();
        int minuto = timePickerinicio.getCurrentMinute();
        LocalTime horaLocal = LocalTime.of(hora, minuto);
        String horainicio1 = horaLocal.toString();


        int hora1 = timePickerfinal.getCurrentHour();
        int minuto1 = timePickerfinal.getCurrentMinute();
        LocalTime horaLocal1 = LocalTime.of(hora1, minuto1);
        String horafin1 = horaLocal1.toString();



        coti.setCotiHoraInicio(horainicio1);
        coti.setCotiHoraFin(horafin1);

        coti.setCotiMonto(Double.parseDouble(edtmontocoti.getText().toString()));

        coti.setCotiTipoEvento(edttipoevento.getText().toString());
        coti.setUsuId(usuario);
        coti.setSalId(salon2);
        System.out.println("usuario: " + usuario.getUsuId());
        System.out.println("salon: " + salon2.getSalId());
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
                        toastCorrecto("Cotización guardado correctamente ");

                        startActivity(new Intent(getActivity(), MainActivity.class));


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



    public void toastCorrecto(String msg) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View view = layoutInflater.inflate(R.layout.custom_toast_ok, (ViewGroup) getActivity().findViewById(R.id.ll_custom_toast_ok));
        TextView txtMensaje = view.findViewById(R.id.txtMensajeToast1);
        txtMensaje.setText(msg);

        Toast toast = new Toast(getContext());
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM, 0, 200);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(view);
        toast.show();
    }

    public void toastIncorrecto(String msg) {
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.custom_toast_error, (ViewGroup) getActivity().findViewById(R.id.ll_custom_toast_error));
        TextView txtMensaje = view.findViewById(R.id.txtMensajeToast2);
        txtMensaje.setText(msg);

        Toast toast = new Toast(getContext());
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM, 0, 200);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(view);
        toast.show();
    }


    public boolean validarduracionhoras() {
        timePickerinicio.setIs24HourView(true);
        timePickerfinal.setIs24HourView(true);

        int horaInicio = timePickerinicio.getHour();
        int minutoInicio = timePickerinicio.getMinute();
        int horaFin = timePickerfinal.getHour();
        int minutoFin = timePickerfinal.getMinute();

        int diferenciaHoras = horaFin - horaInicio;
        int diferenciaMinutos = minutoFin - minutoInicio;

        if (diferenciaMinutos < 0) {
            diferenciaHoras--;
            diferenciaMinutos += 60;
        }

        if (diferenciaHoras >= 4 || (diferenciaHoras == 3 && diferenciaMinutos >= 60)) {
            return true;
        } else {
            toastIncorrecto("El evento debe durar minimo 4 horas.");
            return false;

        }

    }



}