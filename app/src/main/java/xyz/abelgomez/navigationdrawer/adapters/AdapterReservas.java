package xyz.abelgomez.navigationdrawer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import xyz.abelgomez.navigationdrawer.R;
import xyz.abelgomez.navigationdrawer.model.Reserva;
import xyz.abelgomez.navigationdrawer.model.Salon;

public class AdapterReservas extends BaseAdapter {

    private Context context;
    private ArrayList<Reserva> reservas;

    public AdapterReservas(Context context, ArrayList<Reserva> reservas) {
        this.context = context;
        this.reservas = reservas;
    }

    @Override
    public int getCount() {
        return reservas.size();
    }

    @Override
    public Object getItem(int position) {
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
            view = inflater.inflate(R.layout.item_reserva, null);
        } else {
            view = convertView;
        }
   return  view;
    }
}
