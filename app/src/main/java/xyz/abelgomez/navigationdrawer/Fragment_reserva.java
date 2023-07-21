package xyz.abelgomez.navigationdrawer;

import static android.app.Activity.RESULT_OK;

<<<<<<< Updated upstream
import static xyz.abelgomez.navigationdrawer.R.id.btnsubirarchivo;

=======
import android.app.Activity;
>>>>>>> Stashed changes
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
<<<<<<< Updated upstream

import androidx.fragment.app.Fragment;
import androidx.loader.content.CursorLoader;

=======
>>>>>>> Stashed changes
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
<<<<<<< Updated upstream
=======
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.drive.DriveClient;
import com.google.android.gms.drive.DriveResourceClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
>>>>>>> Stashed changes

import java.io.File;

public class Fragment_reserva extends Fragment {
    View view;

    private File f;

    private Button btnSubirIma;
    private File f;
    private Button buttonSelectPdf;
    private Button buttonSelectImage;


<<<<<<< Updated upstream

=======
    @Nullable
>>>>>>> Stashed changes
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reserva, container, false);

<<<<<<< Updated upstream
=======
    /*  // Inflate the layout for this fragment


        imageView = view.findViewById(R.id.imageView);
        selectButton = view.findViewById(R.id.btnsubirarchivo);
        uploadButton = view.findViewById(R.id.Guardarreserva);
>>>>>>> Stashed changes

        btnSubirIma=getView().findViewById(btnsubirarchivo);


        btnSubirIma.setOnClickListener(new View.OnClickListener() {

            @Override
<<<<<<< Updated upstream
            public void onClick(View view) {

                    seleccionarArchivo();


            }
        });

=======
            public void onClick(View v) {

               // pickImageOrPDF();
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedFileUri != null) {
                  //  uploadFileToServer(selectedFileUri);
                   // uploadFiles(selectedFileUri);
                    // Llama al método para realizar la solicitud POST
                   // List<FileModel> archivos = obtenerArchivosDeTuAplicacion();
                   // subirArchivosAlServidor(archivos);
                } else {
                    Toast.makeText(getActivity(), "Primero debes seleccionar un archivo.", Toast.LENGTH_SHORT).show();
                }
              /*  List<FileModel> archivos = obtenerArchivosDeTuAplicacion();
                uploadFiles(archivos);
            }
        });
        queue = Volley.newRequestQueue(getActivity());*/
>>>>>>> Stashed changes
        return view;

    }

<<<<<<< Updated upstream

    private static final int REQUEST_CODE_PICK_FILE = 1;

    public void seleccionarArchivo() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, REQUEST_CODE_PICK_FILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
=======
  /*  private void cargarImagen() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.setType("image/");
        startActivityForResult(Intent.createChooser(i, "Seleccione la Aplicación"), 10);
    }*/

    /*@Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            final String realPath = getRealPathFromURI(uri);
            this.f = new File(realPath);
           // this.imageUser.setImageURI(uri);
        }
    }*/

  /*  private String getRealPathFromURI(Uri contentUri) {
        String result;
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            result = contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

 /*   private void pickImageOrPDF() {
       /* Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*//*");
        startActivityForResult(Intent.createChooser(intent, "Seleccionar imagen o PDF"), REQUEST_PICK_IMAGE_OR_PDF);


        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_PICK_IMAGE_OR_PDF);
    }


  /*  @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
>>>>>>> Stashed changes
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PICK_FILE && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            String path = getPathFromUri(uri);
            // enviarArchivoAlServidor(path);
        }
    }*/
 /* @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
      super.onActivityResult(requestCode, resultCode, data);

    /*  if (requestCode == REQUEST_PICK_IMAGE_OR_PDF && resultCode == RESULT_OK && data != null) {
          if (data != null) {
              selectedFileUri = data.getData();
              String fileName = FileUtils.getFileNameFromUri(selectedFileUri, getActivity().getContentResolver());
              displaySelectedFile(selectedFileUri, fileName);
              filemodel.setName(fileName);
              filemodel.setUrl(String.valueOf(selectedFileUri));
              Uri imageUri = data.getData();
              imageView.setImageURI(imageUri);
          }


      }

      if (requestCode == REQUEST_PICK_IMAGE_OR_PDF && resultCode == RESULT_OK && data != null) {
          Uri imageUri = data.getData();

          if (imageUri != null && "content".equals(imageUri.getScheme())) {
              imageView.setImageURI(imageUri);
              if (data != null) {

                  selectedFileUri = data.getData();
                  String fileName = FileUtils.getFileNameFromUri(selectedFileUri, getActivity().getContentResolver());
                  displaySelectedFile(selectedFileUri, fileName);
                  filemodel.setName(fileName);
                  filemodel.setUrl(String.valueOf(selectedFileUri));
                 // imageUri = data.getData();
                  imageView.setImageURI(imageUri);
              }
          } else {
              // Manejar caso de URI no válida
          }
      }
  }

    private String getPathFromUri(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getActivity().getApplicationContext(), uri, projection, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }


<<<<<<< Updated upstream

    private void enviarReserva() {

    }
}
=======
   private static final String TAG = "FileUploadManager";
    private static final String BASE_URL = ConfigApi.baseUrlE + "/file/upload";


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
        System.out.println(filemodel.getName());
        lismodel.add(filemodel);
        return lismodel;

    }

    /*public void uploadFiles(List<FileModel> files) {
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
*/


}
>>>>>>> Stashed changes
