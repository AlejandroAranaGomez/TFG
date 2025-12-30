package com.trabajo.fitnessapp.presentacion.menu.Rutinas;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.trabajo.fitnessapp.R;

public class MenuEjercicios extends AppCompatActivity {

    private ImageButton botonVolverActividad;

    private Long idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_ejercicios);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        idUsuario = getIntent().getLongExtra("ID_USUARIO", -1l);

        enlazarVistas();

        configurarBotones();
    }

    private void enlazarVistas() {
        botonVolverActividad = findViewById(R.id.botonVolverActividad);
    }

    private void configurarBotones() {
        botonVolverActividad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}