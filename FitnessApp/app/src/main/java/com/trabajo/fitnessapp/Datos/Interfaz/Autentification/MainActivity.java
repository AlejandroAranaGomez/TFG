package com.trabajo.fitnessapp.Datos.Interfaz.Autentification;

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

public class MainActivity extends AppCompatActivity {

    private Button botonIrARegistro;
    private Button getBotonIrALogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        botonIrARegistro = findViewById(R.id.botonPruebaRegistro);
        getBotonIrALogin = findViewById(R.id.botonPruebaLogin);

        View mainView = findViewById(R.id.main);
        ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        botonIrARegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 4. Crear la "intención" de ir de esta pantalla (MainActivity.this)
                //    a la pantalla de destino (RegisterActivity.class)
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);

                // 5. Ejecuta la intención (abre la nueva pantalla)
                startActivity(intent);
            }
        });

        getBotonIrALogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 4. Crear la "intención" de ir de esta pantalla (MainActivity.this)
                //    a la pantalla de destino (RegisterActivity.class)
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);

                // 5. Ejecuta la intención (abre la nueva pantalla)
                startActivity(intent);
            }
        });
    }
}