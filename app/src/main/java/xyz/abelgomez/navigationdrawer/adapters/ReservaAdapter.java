package xyz.abelgomez.navigationdrawer.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;

import xyz.abelgomez.navigationdrawer.R;

public class ReservaAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Map<String, Object>> reservas;

    public ReservaAdapter(Context context, ArrayList<Map<String, Object>> reservas) {
        this.context = context;
        this.reservas = reservas;
    }

    @Override
    public int getCount() {
        return reservas.size();
    }

    @Override
    public Map<String, Object> getItem(int position) {
        return reservas.get(position);
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
            view = inflater.inflate(R.layout.item_reservas, null);
        } else {
            view = convertView;
        }

        TextView nombreSalonTextView = view.findViewById(R.id.nombreSalonTextView);
        TextView montoTextView = view.findViewById(R.id.montoTextView);
        TextView fechaTextView = view.findViewById(R.id.fechaTextView);
        TextView estadoTextView = view.findViewById(R.id.estadoTextView);
        TextView tipoEventoextView = view.findViewById(R.id.tipoEstadoTextView);


        Map<String, Object> reserva = reservas.get(position);
        String fechaEvento = (String) reserva.get("fechaReserva");
        String tipoEvento = (String) reserva.get("tipoEvento");
        int estado = (int) reserva.get("estado");
        //double monto =(double ) reserva.get("monto");
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        String montoFormateado = decimalFormat.format((double ) reserva.get("monto"));
        String nombreSalon = (String) reserva.get("salonNombre");


        // Aquí puedes usar el valor de tipoEvento y estado si es necesario
        // Tipo de evento: tipoEvento
        // Estado de la reserva: estado

        nombreSalonTextView.setText(nombreSalon);
        montoTextView.setText("$  "+String.valueOf(montoFormateado));
        fechaTextView.setText(fechaEvento);

        if (estado == 1) {
            estadoTextView.setText("En espera.");
            estadoTextView.setTextColor(Color.BLUE);
        } else if (estado == 2) {
            estadoTextView.setText("Aprobado");
            estadoTextView.setTextColor(Color.GREEN);
        } else {
            estadoTextView.setText("Denegado");
            estadoTextView.setTextColor(Color.RED);
        }
        tipoEventoextView.setText(tipoEvento);
//        if (position % 2 == 0) {
//            //   view.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
//            // Reemplaza "image_name" con el nombre del archivo de imagen en la carpeta "drawable"
//            view.setBackgroundResource(R.drawable.imgitem5);
//
//        } else {
//            //  view.setBackgroundColor(ContextCompat.getColor(context, R.color.combined_color));
//            view.setBackgroundResource(R.drawable.imgitem5);
//        }

        return view;
    }
}
