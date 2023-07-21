package xyz.abelgomez.navigationdrawer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class pruebaActivity2 extends AppCompatActivity {
    private TextView textViewCotiId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba2);

        // Obtener el ID de cotización desde los extras
        Intent intent = getIntent();
        long cotiId = intent.getLongExtra("cotiId", -1);

        // Mostrar el ID de cotización en un TextView (o en cualquier otro elemento de la interfaz)
        textViewCotiId = findViewById(R.id.textViewprueba);
        textViewCotiId.setText("ID de cotización: " + cotiId);
    }
}