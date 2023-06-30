package xyz.abelgomez.navigationdrawer;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import xyz.abelgomez.navigationdrawer.model.Salon;


public class DetalleSalonFragment extends Fragment {

    private Salon salon;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Obtener los datos del objeto Salon pasado como argumento
        Bundle bundle = getArguments();
        if (bundle != null) {
            salon = (Salon) bundle.getSerializable("salon");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detalle_salon, container, false);

        // Obtener los elementos de la vista
        ImageView imageView = view.findViewById(R.id.imageView);
        TextView nombreTextView = view.findViewById(R.id.nombreTextView);
        TextView descripcionTextView = view.findViewById(R.id.descripcionTextView);
        // Otros elementos de la vista para mostrar los datos del Salon

        // Mostrar los datos del Salon en los elementos de la vista
        if (salon != null) {
            // Mostrar la imagen del Salon utilizando Glide u otra biblioteca de terceros
            Glide.with(requireContext())
                    .load(salon.getImagen())
                    .into(imageView);

            // Mostrar el nombre y descripción del Salon
            nombreTextView.setText(salon.getNombre());
            descripcionTextView.setText(salon.getDescripccion());

            // Otros elementos de la vista para mostrar los datos del Salon
        }

        return view;
    }
}