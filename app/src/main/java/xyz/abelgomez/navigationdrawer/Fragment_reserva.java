package xyz.abelgomez.navigationdrawer;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xyz.abelgomez.navigationdrawer.api.ConfigApi;

public class Fragment_reserva extends Fragment {


    private static final int REQUEST_PICK_IMAGE_OR_PDF = 1;
    private ImageView imageView;
    private Button selectButton;
    private Button uploadButton;
    private Uri selectedFileUri;
    FileModel filemodel=new FileModel();

    // URL del endpoint en tu servidor Spring
    //private static final String UPLOAD_URL = ConfigApi.baseUrlE + "/file/upload";
    private RequestQueue queue;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reserva, container, false);

        imageView = view.findViewById(R.id.imageView);
        selectButton = view.findViewById(R.id.btnsubirarchivo);
        uploadButton = view.findViewById(R.id.Guardarreserva);

        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImageOrPDF();
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if (selectedFileUri != null) {
                  //  uploadFileToServer(selectedFileUri);
                   // uploadFiles(selectedFileUri);
                    // Llama al método para realizar la solicitud POST
                    List<FileModel> archivos = obtenerArchivosDeTuAplicacion();
                    subirArchivosAlServidor(archivos);
                } else {
                    Toast.makeText(getActivity(), "Primero debes seleccionar un archivo.", Toast.LENGTH_SHORT).show();
                }*/
                List<FileModel> archivos = obtenerArchivosDeTuAplicacion();
                uploadFiles(archivos);
            }
        });
        queue = Volley.newRequestQueue(getActivity());
        return view;

    }

    private void pickImageOrPDF() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(Intent.createChooser(intent, "Seleccionar imagen o PDF"), REQUEST_PICK_IMAGE_OR_PDF);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PICK_IMAGE_OR_PDF && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                selectedFileUri = data.getData();
                String fileName = FileUtils.getFileNameFromUri(selectedFileUri, getActivity().getContentResolver());
                displaySelectedFile(selectedFileUri, fileName);
                filemodel.setName(fileName);
                filemodel.setUrl(String.valueOf(selectedFileUri));
            }
        }
    }

    private void displaySelectedFile(Uri uri, String fileName) {
        imageView.setVisibility(View.VISIBLE);
        imageView.setImageURI(uri);
    }


   private static final String TAG = "FileUploadManager";
    private static final String BASE_URL = ConfigApi.baseUrlE + "/file/upload";;


    private void subirArchivosAlServidor(List<FileModel> archivos) {
        JSONArray jsonArray = new JSONArray();
        for (FileModel archivo : archivos) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", archivo.getName());
                 jsonObject.put("url", archivo.getUrl());
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // Crear la solicitud POST
        VolleyMultipartRequest request = new VolleyMultipartRequest(Request.Method.POST, BASE_URL,
                response -> {
                    Toast.makeText(getActivity(), "exito", Toast.LENGTH_SHORT).show();
                }, error -> {
            // Aquí manejas los errores de la solicitud
            error.printStackTrace();
        });
        for (FileModel archivo : obtenerArchivosDeTuAplicacion()) {
            // Obtener la ruta real del archivo desde el Uri
            String filePath = FileUtils.getRealPathFromUri(getActivity(), Uri.parse(archivo.getUrl()));
            System.out.println(obtenerArchivosDeTuAplicacion().toArray().length);

            if (filePath != null) {
                // Agregar cada archivo a la solicitud
                request.addFile("files", new File(filePath));
            }
        }

        // Agregar la solicitud a la cola de solicitudes de Volley
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);
    }

    // Método para obtener la lista de archivos de tu aplicación (simulado para este ejemplo)
    private List<FileModel> obtenerArchivosDeTuAplicacion() {
        List<FileModel>lismodel=new ArrayList<>();
        lismodel.add(filemodel);
        System.out.println(filemodel.getName());
        return lismodel;

    }

    public void uploadFiles(List<FileModel> files) {
        String url = BASE_URL ;
        VolleyMultipartRequest request = new VolleyMultipartRequest(Request.Method.POST, url
                ) {

            @Override
            public Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                for (int i = 0; i < files.size(); i++) {
                    FileModel file = files.get(i);
                    String fileName = file.getName();
                    String contentType = getMimeType(fileName);
                    try {
                        byte[] fileData = getFileDataFromPath(file.getUrl());
                        params.put("files[" + i + "]", new DataPart(fileName, fileData, contentType));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return params;
            }

            private byte[] getFileDataFromPath(String path) throws IOException {
                File file = new File(path);
                int size = (int) file.length();
                byte[] bytes = new byte[size];
                try (InputStream inputStream = new FileInputStream(file)) {
                    inputStream.read(bytes);
                }
                return bytes;
            }

            private String getMimeType(String fileName) {
                // Implementa la obtención del tipo de MIME según la extensión del archivo si es necesario
                // Puedes encontrar ejemplos de cómo hacerlo en línea
                return "application/octet-stream";
            }
        };

        // Agregar la solicitud a la cola de solicitudes de Volley
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);
    }

}
