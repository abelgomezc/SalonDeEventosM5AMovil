package xyz.abelgomez.navigationdrawer.model;

import android.os.AsyncTask;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import xyz.abelgomez.navigationdrawer.api.ConfigApi;

public class FileUploadTask extends AsyncTask<List<File>, Void, Void> {

    @Override
    protected Void doInBackground(List<File>... params) {
        List<File> files = params[0];
        enviarArchivoAlServidor(files);
        return null;
    }

    private void enviarArchivoAlServidor(List<File> files) {
        //  String url = "http://192.168.18.4:9999/file/upload";
        String url = ConfigApi.baseUrlE+"/file/upload";



        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            String boundary = "===" + System.currentTimeMillis() + "===";
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            DataOutputStream request = new DataOutputStream(connection.getOutputStream());

            for (File file : files) {
                request.writeBytes("--" + boundary + "\r\n");
                request.writeBytes("Content-Disposition: form-data; name=\"files\"; filename=\"" + file.getName() + "\"\r\n");
                request.writeBytes("Content-Type: " + URLConnection.guessContentTypeFromName(file.getName()) + "\r\n");
                request.writeBytes("\r\n");

                FileInputStream inputStream = new FileInputStream(file);
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    request.write(buffer, 0, bytesRead);
                }
                request.writeBytes("\r\n");
                inputStream.close();
            }

            request.writeBytes("--" + boundary + "--\r\n");
            request.flush();
            request.close();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream responseStream = connection.getInputStream();
                // Leer la respuesta del servidor si es necesario
                // ...
                responseStream.close();
            } else {
                // Manejar error en la respuesta del servidor si es necesario
                // ...
            }

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

