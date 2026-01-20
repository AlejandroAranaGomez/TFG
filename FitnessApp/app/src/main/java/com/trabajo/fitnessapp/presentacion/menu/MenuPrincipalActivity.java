package com.trabajo.fitnessapp.presentacion.menu;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.trabajo.fitnessapp.R;
import com.trabajo.fitnessapp.presentacion.fragmentosMenu.FragmentoDietas;
import com.trabajo.fitnessapp.presentacion.fragmentosMenu.FragmentoInicio;
import com.trabajo.fitnessapp.presentacion.fragmentosMenu.FragmentoPerfil;
import com.trabajo.fitnessapp.presentacion.fragmentosMenu.FragmentoRutinas;

public class MenuPrincipalActivity extends AppCompatActivity {

    private BottomNavigationView barraMenu;
    private Long usuarioRegistrado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_principal);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        usuarioRegistrado = getIntent().getLongExtra("ID_USUARIO", -1L);

        enlazarVistas();

        // Para cuando se carga por primera vez el menu
        if (savedInstanceState == null) {
            cargarFragmento(new FragmentoInicio());
        }

        barraMenu.setOnItemSelectedListener(item -> {
            Fragment fragmentoSeleccionado = null;
            int id = item.getItemId();

            if (id == R.id.btnMenuInicio) {
                fragmentoSeleccionado = new FragmentoInicio();
            } else if (id == R.id.btnMenuDietas) {
                fragmentoSeleccionado = new FragmentoDietas();
                // Le paso el id del usuario
                Bundle argumentos = new Bundle();
                argumentos.putLong("ID_USUARIO", usuarioRegistrado);
                fragmentoSeleccionado.setArguments(argumentos);

            } else if (id == R.id.btnMenuRutinas) {
                fragmentoSeleccionado = new FragmentoRutinas();
                Bundle argumentos = new Bundle();
                argumentos.putLong("ID_USUARIO", usuarioRegistrado);
                fragmentoSeleccionado.setArguments(argumentos);

            } else if (id == R.id.btnMenuPerfil) {
                fragmentoSeleccionado = new FragmentoPerfil();
                Bundle argumentos = new Bundle();
                argumentos.putLong("ID_USUARIO", usuarioRegistrado);
                fragmentoSeleccionado.setArguments(argumentos);
            }

            if (fragmentoSeleccionado != null) {
                cargarFragmento(fragmentoSeleccionado);
                return true;
            }
            return false;
        });

    }

    private void enlazarVistas() {
        barraMenu = findViewById(R.id.barraMenu);
    }

    private void cargarFragmento(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.fragmentoVacio, fragment);
        transaction.commit();
    }
}