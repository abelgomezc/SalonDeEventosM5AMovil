package xyz.abelgomez.navigationdrawer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import xyz.abelgomez.navigationdrawer.model.Usuario;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String PREF_NAME = "MiPreferencia";
    private static final String KEY_USUARIO = "usuario";
    private DrawerLayout drawerLayout;

    Usuario usuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String usuarioJson = sharedPreferences.getString(KEY_USUARIO, "");
        Gson gson = new Gson();
        usuario = gson.fromJson(usuarioJson, Usuario.class);


        NavigationView navigationView = findViewById(R.id.nav_view);
        TextView txtUsuario = navigationView.getHeaderView(0).findViewById(R.id.txtUsuario);
        txtUsuario.setText(usuario.getUsuNombreUsuario());


        // Inicializar Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);

        // Establecer Toolbar como la action bar de la actividad
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        // NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav,
                R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                break;

            case R.id.nav_tusReservas:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ReservasFragment()).commit();
                break;
            case R.id.nav_tusCotizaciones:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TusCotizacionesFragment()).commit();
                break;

            case R.id.nav_sobrenosotros:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AboutFragment()).commit();
                break;


            case R.id.nav_logout:
                mostrarConfirmacionCerrarSesion();
               // startActivity(new Intent(this, LoginActivity.class));
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
//    @Override
//    public void onBackPressed() {
//        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
//            drawerLayout.closeDrawer(GravityCompat.START);
//        } else {
//            // Mostrar la confirmación de cierre de sesión
//            mostrarConfirmacionCerrarSesion();
//        }
//    }

//    @Override
//    public void onBackPressed() {
//        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
//            drawerLayout.closeDrawer(GravityCompat.START);
//        } else {
//            // Si el fragmento Home no está visible, mostrarlo
//            if (!(getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof HomeFragment)) {
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
//                NavigationView navigationView = findViewById(R.id.nav_view);
//                navigationView.setCheckedItem(R.id.nav_home);
//            } else {
//                // Si el fragmento Home está visible, mostrar la confirmación de cierre de sesión
//                mostrarConfirmacionCerrarSesion();
//            }
//        }
//    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            // Si el fragmento Home no está visible, mostrarlo
            if (!(getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof HomeFragment)) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                NavigationView navigationView = findViewById(R.id.nav_view);
                navigationView.setCheckedItem(R.id.nav_home);
            } else {
                // Si el fragmento Home está visible, mostrar la confirmación de cierre de sesión
                mostrarConfirmacionCerrarSesion();
            }
        }
    }



    private void mostrarConfirmacionCerrarSesion() {
        new AlertDialog.Builder(this)
                .setTitle("Cerrar Sesión")
                .setMessage("¿Estás seguro de que deseas cerrar sesión?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cerrarSesion();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void cerrarSesion() {
        // Elimina los datos del usuario de las preferencias compartidas
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_USUARIO);
        editor.apply();
        Toast.makeText(this, "Logout!....  ADIOS  "+usuario.getUsuNombreUsuario() , Toast.LENGTH_SHORT).show();
        // Redirige al LoginActivity
        startActivity(new Intent(this, LoginActivity.class));
        finish(); // Cierra la actividad actual para evitar que el usuario regrese presionando el botón "Atrás"
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 1) {
//            // Verificar si el resultado es OK (reserva guardada exitosamente)
//            if (resultCode == RESULT_OK) {
//                // Realizar la transacción para dirigirte al HomeFragment
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ReservasFragment()).commit();
//                NavigationView navigationView = findViewById(R.id.nav_tusReservas);
//                navigationView.setCheckedItem(R.id.nav_tusReservas);
//            }
//        }
//    }


}