package com.trabajo.fitnessapp.presentacion.autentification;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.trabajo.fitnessapp.R;
import com.trabajo.fitnessapp.presentacion.menu.MenuPrincipalActivity;

public class MainActivity extends AppCompatActivity {

    private Button botonIrARegistro;
    private Button botonIrALogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        botonIrARegistro = findViewById(R.id.botonPruebaRegistro);
        botonIrALogin = findViewById(R.id.botonPruebaLogin);


        View mainView = findViewById(R.id.main);
        ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        configurarBotones();

    }

    private void configurarBotones() {
        botonIrARegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BienvenidaRegisterActivity.class);
                startActivity(intent);
            }
        });

        botonIrALogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

}