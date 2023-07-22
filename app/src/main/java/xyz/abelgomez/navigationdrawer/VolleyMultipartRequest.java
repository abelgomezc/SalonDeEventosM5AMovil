package xyz.abelgomez.navigationdrawer;
import android.graphics.Bitmap;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
public class VolleyMultipartRequest extends Request<NetworkResponse> {
    private final String twoHyphens = "--";
    private final String lineEnd = "\r\n";
    private final String boundary = "apiclient-" + System.currentTimeMillis();

    private Response.Listener<NetworkResponse> mListener;
    private Response.ErrorListener mErrorListener;
    private Map<String, String> mHeaders;

    public VolleyMultipartRequest(int method, String url,
                                  Response.Listener<NetworkResponse> listener,
                                  Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mListener = listener;
        this.mErrorListener = errorListener;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return (mHeaders != null) ? mHeaders : super.getHeaders();
    }

    @Override
    public String getBodyContentType() {
        return "multipart/form-data;boundary=" + boundary;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            buildMultipartEntity(bos);
        } catch (IOException e) {
            Log.e("VolleyMultipartRequest", "IOException writing to ByteArrayOutputStream");
        }
        return bos.toByteArray();
    }

    private void buildMultipartEntity(ByteArrayOutputStream bos) throws IOException {
        for (Map.Entry<String, VolleyMultipartRequestData> entry : params.entrySet()) {
            if (entry.getValue().getContent() != null) {
                buildTextPart(bos, entry.getKey(), entry.getValue().getContent());
            }
        }

        for (Map.Entry<String, DataPart> entry : byteData.entrySet()) {
            buildDataPart(bos, entry.getKey(), entry.getValue());
        }

        bos.write((twoHyphens + boundary + twoHyphens + lineEnd).getBytes());
    }

    public void setHeaders(Map<String, String> headers) {
        mHeaders = headers;
    }

    @Override
    protected Response<NetworkResponse> parseNetworkResponse(NetworkResponse response) {
        return Response.success(
                response,
                HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(NetworkResponse response) {
        mListener.onResponse(response);
    }

    @Override
    public void deliverError(VolleyError error) {
        mErrorListener.onErrorResponse(error);
    }

    private void buildTextPart(ByteArrayOutputStream bos, String parameterName, String parameterValue) throws IOException {
        bos.write((twoHyphens + boundary + lineEnd).getBytes());
        bos.write(("Content-Disposition: form-data; name=\"" + parameterName + "\"" + lineEnd).getBytes());
        bos.write((lineEnd + parameterValue + lineEnd).getBytes());
    }

    private void buildDataPart(ByteArrayOutputStream bos, String parameterName, DataPart dataPart) throws IOException {
        bos.write((twoHyphens + boundary + lineEnd).getBytes());
        bos.write(("Content-Disposition: form-data; name=\"" + parameterName + "\"; filename=\""
                + dataPart.getFileName() + "\"" + lineEnd).getBytes());
        if (dataPart.getType() != null && !dataPart.getType().trim().isEmpty()) {
            bos.write(("Content-Type: " + dataPart.getType() + lineEnd).getBytes());
        }
        bos.write((lineEnd).getBytes());

        if (dataPart.getContent() != null) {
            bos.write(dataPart.getContent());
        } else if (dataPart.getFileName() != null) {
            bos.write(getBytesFromFile(new File(dataPart.getFileName())));
        }

        bos.write((lineEnd).getBytes());
    }

    private byte[] getBytesFromFile(File file) throws IOException {
        // Convertir el archivo a un arreglo de bytes para enviar en la solicitud
        // Implementa esto según tus necesidades específicas
        // Puedes encontrar ejemplos de cómo hacerlo en línea
        return null;
    }

    private Map<String, DataPart> byteData = new HashMap<>();
    private Map<String, VolleyMultipartRequestData> params = new HashMap<>();

    public void addFile(String paramName, File file) {
        byteData.put(paramName, new DataPart(file));
    }

    public void addStringParam(String paramName, String value) {
        params.put(paramName, new VolleyMultipartRequestData(value));
    }

    public static class DataPart {
        private String fileName;
        private byte[] content;
        private String type;

        public DataPart(String fileName, byte[] content) {
            this.fileName = fileName;
            this.content = content;
        }

        public DataPart(String fileName, byte[] content, String type) {
            this.fileName = fileName;
            this.content = content;
            this.type = type;
        }

        public DataPart(File file) {
            // Implementa esto para obtener el nombre y contenido del archivo
            // Puedes encontrar ejemplos de cómo hacerlo en línea
        }

        public String getFileName() {
            return fileName;
        }

        public byte[] getContent() {
            return content;
        }

        public String getType() {
            return type;
        }
    }
}
