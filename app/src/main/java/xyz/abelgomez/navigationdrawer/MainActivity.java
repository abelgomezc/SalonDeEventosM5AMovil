package xyz.abelgomez.navigationdrawer;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
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


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String PREF_NAME = "MiPreferencia";
    private static final String KEY_USUARIO = "usuario";
    private DrawerLayout drawerLayout;
    String nombreUsuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
       nombreUsuario = sharedPreferences.getString(KEY_USUARIO, "");

        NavigationView navigationView = findViewById(R.id.nav_view);
        TextView txtUsuario = navigationView.getHeaderView(0).findViewById(R.id.txtUsuario);
        txtUsuario.setText(nombreUsuario);


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
            case R.id.nav_ajustes:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AjustesFragment()).commit();
                break;
            case R.id.nav_sobrenosotros:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AboutFragment()).commit();
                break;
            case R.id.nav_reserva:
                Intent intent = new Intent(MainActivity.this, Activity_reserva.class);
                startActivity(intent);
                break;

            case R.id.nav_logout:
                Toast.makeText(this, "Logout!....  ADIOS  "+nombreUsuario , Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LoginActivity.class));
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}