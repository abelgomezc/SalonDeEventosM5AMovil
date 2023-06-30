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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;

import xyz.abelgomez.navigationdrawer.adapters.AdapterHome;
import xyz.abelgomez.navigationdrawer.model.Salon;


public class HomeFragment extends Fragment {

    private ArrayList<Salon> salones;
    private AdapterHome adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ListView listView = view.findViewById(R.id.listaView);
        salones = new ArrayList<>();

        // Agregar objetos Salon de prueba
        Salon salon2 = new Salon();
        salon2.setNombre("Salón 2");
        salon2.setDescripccion("Descripción del Salón 2");

        InputStream inputStream = requireContext().getResources().openRawResource(R.raw.img1);
        File file = null;
        try {
            file = createTempFile(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        salon2.setImagen(file);
        salones.add(salon2);/////

        Salon salon1 = new Salon();
        salon1.setNombre("Salón 1");
        salon1.setDescripccion("Descripción del Salón 1");

        InputStream inputStream1 = requireContext().getResources().openRawResource(R.raw.img2);
        File file1 = null;
        try {
            file1 = createTempFile(inputStream1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        salon1.setImagen(file1);
        salones.add(salon1);///


        Salon salon3 = new Salon();
        salon3.setNombre("Salón 3");
        salon3.setDescripccion("Descripción del Salón 3");

        InputStream inputStream3 = requireContext().getResources().openRawResource(R.raw.img3);
        File file3 = null;
        try {
            file3 = createTempFile(inputStream3);
        } catch (IOException e) {
            e.printStackTrace();
        }

        salon3.setImagen(file3);
        salones.add(salon3);//
        Salon salon4 = new Salon();
        salon4.setNombre("Salón 4");
        salon4.setDescripccion("Descripción del Salón 4");

        InputStream inputStream4 = requireContext().getResources().openRawResource(R.raw.img4);
        File file4 = null;
        try {
            file4 = createTempFile(inputStream4);
        } catch (IOException e) {
            e.printStackTrace();
        }

        salon4.setImagen(file4);
        salones.add(salon4);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Obtener el objeto Salon seleccionado
                Salon salon = salones.get(position);

                // Crear una instancia del fragmento DetalleSalonFragment
                DetalleSalonFragment detalleSalonFragment = new DetalleSalonFragment();

                // Pasar los datos del objeto Salon al fragmento DetalleSalonFragment
                Bundle bundle = new Bundle();
                bundle.putSerializable("salon",salon);
                detalleSalonFragment.setArguments(bundle);

                // Reemplazar el fragmento actual por el fragmento DetalleSalonFragment
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, detalleSalonFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });





        adapter = new AdapterHome(getActivity(), salones);
        listView.setAdapter(adapter);

        return view;
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