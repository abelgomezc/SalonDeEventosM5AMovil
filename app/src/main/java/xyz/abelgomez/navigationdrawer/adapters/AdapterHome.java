package xyz.abelgomez.navigationdrawer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

import xyz.abelgomez.navigationdrawer.R;
import xyz.abelgomez.navigationdrawer.model.Salon;
public class AdapterHome extends BaseAdapter {

    private Context context;
    private ArrayList<Salon> salones;

    public AdapterHome(Context context, ArrayList<Salon> salones) {
        this.context = context;
        this.salones = salones;
    }

    @Override
    public int getCount() {
        return salones.size();
    }

    @Override
    public Salon getItem(int position) {
        return salones.get(position);
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
            view = inflater.inflate(R.layout.item_salon, null);
        } else {
            view = convertView;
        }

        ImageView imageView = view.findViewById(R.id.imagen);
        TextView nombreTextView = view.findViewById(R.id.nombre);
        TextView descripcionTextView = view.findViewById(R.id.descricion);

        Salon salon = salones.get(position);

        // Carga la imagen desde la URL utilizando una biblioteca de terceros o métodos personalizados
        Glide.with(context)
                .load(salon.getUrlImagen())
                .centerCrop()
                .placeholder(R.drawable.imgnull)
                .into(imageView);

        nombreTextView.setText(salon.getId_salon()+"   Nombre :"+salon.getNombre());
        descripcionTextView.setText(salon.getDireccion());

        // Cambiar el color de fondo de los elementos de la ListView
        if (position % 2 == 0) {
            view.setBackgroundColor(ContextCompat.getColor(context, R.color.combined_color));
        } else {
            view.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
        }


        return view;
    }

    public int getSalonId(int position) {
        Salon salon = salones.get(position);
        return salon.getId_salon();
    }
}



//        // Carga la imagen desde el archivo utilizando una biblioteca de terceros o métodos personalizados
//        Glide.with(context)
//                .load(salon.getImagen())
//                .centerCrop()
//                .placeholder(R.drawable.s)
//                .into(imageView);








//    @Override
//    public long getItemId(int position) {
//        return salones.get(position).getId_salon() ;
//    }

    //    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        View view;
//
//        if (convertView == null) {
//            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            view = inflater.inflate(R.layout.item_salon, null);
//        } else {
//            view = convertView;
//        }
//
//        ImageView imageView = view.findViewById(R.id.imagen);
//        TextView nombreTextView = view.findViewById(R.id.nombre);
//        TextView descripcionTextView = view.findViewById(R.id.descricion);
////        TextView capacidadTextView = view.findViewById(R.id.capacidad);
////        TextView costoTextView = view.findViewById(R.id.costo);
//
//        Salon salon = salones.get(position);
//
//        Glide.with(context)
//                .load(salon.getImagne())  // Asegúrate de tener un método getImagen() en tu clase Salon
//                .centerCrop()
//                .placeholder(R.drawable.s)
//                .into(imageView);
//
//        nombreTextView.setText(salon.getNombre());
//        descripcionTextView.setText(salon.getDescripccion());
////        capacidadTextView.setText("Capacidad: " + salon.getCapacidad());
////        costoTextView.setText("Costo por hora: $" + salon.getCostoHora());
//
//        return view;
//    }

