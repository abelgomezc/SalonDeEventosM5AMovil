package xyz.abelgomez.navigationdrawer.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

import xyz.abelgomez.navigationdrawer.Activity_reserva;
import xyz.abelgomez.navigationdrawer.R;
import xyz.abelgomez.navigationdrawer.pruebaActivity2;

public class AdapterCotizaciones extends BaseAdapter {

    private Context context;
    private ArrayList<Map<String, Object>> cotizaciones;

    public AdapterCotizaciones(Context context, ArrayList<Map<String, Object>> cotizaciones) {
        this.context = context;
        this.cotizaciones = cotizaciones;
    }

    @Override
    public int getCount() {
        return cotizaciones.size();
    }

    @Override
    public Map<String, Object> getItem(int position) {
        return cotizaciones.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_cotizacion, null);
        } else {
            view = convertView;
        }
        TextView idCotizacionTextView = view.findViewById(R.id.textViewIdCotizacion);
        TextView nombreSalonTextView = view.findViewById(R.id.nombreSalonTextView);
        TextView montoTextView = view.findViewById(R.id.montoTextView);
        TextView fechaTextView = view.findViewById(R.id.fechaTextView);

        Map<String, Object> cotizacion = cotizaciones.get(position);
        String idCotizacion = String.valueOf(cotizacion.get("cotiId"));
        String salonNombre = (String) cotizacion.get("salonNombre");
        Double monto = (Double) cotizacion.get("monto");
        String fechaReserva = (String) cotizacion.get("fechaReserva");
        Button reservarButton = view.findViewById(R.id.btnReservar);



        idCotizacionTextView.setText(idCotizacion);
        nombreSalonTextView.setText(salonNombre);
        montoTextView.setText(String.valueOf(monto));
        fechaTextView.setText(fechaReserva);
        if (position % 2 == 0) {
            //   view.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
            // Reemplaza "image_name" con el nombre del archivo de imagen en la carpeta "drawable"
            view.setBackgroundResource(R.drawable.imgitem2);

        } else {
            //  view.setBackgroundColor(ContextCompat.getColor(context, R.color.combined_color));
            view.setBackgroundResource(R.drawable.imgitem4);
        }

        reservarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Confirmación");
                builder.setMessage("Para acceder a la confirmacion de la reserva debe tener una imagen del pago de la cotizacion" +
                        "Posee el comprobante?");
                builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        long cotiId = (long) cotizacion.get("cotiId");
                        Log.d("ReservaActivity", "ID de cotización: " + cotiId);

                        // Abrir el Activity de reserva y pasar el ID de la cotización como extra
                        Intent intent = new Intent(context, pruebaActivity2.class);
                        intent.putExtra("cotiId", cotiId);
                        context.startActivity(intent);
                    }
                });
                builder.setNegativeButton("Cancelar", null);
                builder.show();
            }
        });
        return view;
    }


    private void mostrarConfirmacion() {

    }
}
