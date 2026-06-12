package com.trabajo.fitnessapp.presentacion.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.trabajo.fitnessapp.R;

public class BienvenidaRegisterActivity extends AppCompatActivity {

    private Button botonComenzar;
    private ImageButton botonVolverInicio;
    private ProgressBar barraProgresoActividad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bienvenida_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        enlazarVistas();

        configurarBotones();

        barraProgresoActividad.setProgress(0);

    }

    private void enlazarVistas() {
        botonComenzar = findViewById(R.id.botonComenzar);
        botonVolverInicio = findViewById(R.id.botonVolverInicio);
        View barraProgreso = findViewById(R.id.barraProgreso);
        barraProgresoActividad = barraProgreso.findViewById(R.id.barraProgresoLayout);

    }

    private void configurarBotones() {
        botonComenzar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BienvenidaRegisterActivity.this, ObjetivosActivity.class);
                startActivity(intent);
            }
        });

        botonVolverInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}