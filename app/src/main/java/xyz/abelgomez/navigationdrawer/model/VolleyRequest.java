package xyz.abelgomez.navigationdrawer.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class VolleyRequest {
    private final Context context;
    private final RequestQueue requestQueue;

    public VolleyRequest(Context context) {
        this.context = context;
        this.requestQueue = Volley.newRequestQueue(context);
    }

    public void uploadImageToServer(String serverUrl, Bitmap bitmap, final Response.Listener<String> successListener, final Response.ErrorListener errorListener) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, serverUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        successListener.onResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        errorListener.onErrorResponse(error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("image", bitmapToString(bitmap));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private String bitmapToString(Bitmap bitmap) {
        int MAX_IMAGE_SIZE = 1024 * 1024; // 1 MB (ajusta el tamaño máximo según tus necesidades)

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();

        if (imageBytes.length > MAX_IMAGE_SIZE) {
            // Redimensiona o comprime la imagen si excede el tamaño máximo
            float ratio = (float) MAX_IMAGE_SIZE / imageBytes.length;
            bitmap = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() * ratio), (int) (bitmap.getHeight() * ratio), true);

            byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            imageBytes = byteArrayOutputStream.toByteArray();
        }

        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }
}
