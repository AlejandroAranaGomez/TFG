package com.trabajo.fitnessapp.presentacion.autentification;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import com.trabajo.fitnessapp.presentacion.menu.MenuPrincipalActivity;
import com.trabajo.fitnessapp.R;


public class LoginActivity extends AppCompatActivity {

    private LoginViewModel viewModel;
    private EditText editEmailSesion, editContrasenhaSesion;

    private Button botonIniciarSesion;

    private ImageButton botonVolverInicio;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        enlazarVistasLogin();

        configurarBotones();

        mensajeAlIniciarSesion();

    }
    private void enlazarVistasLogin() {
        editEmailSesion = findViewById(R.id.editEmailSesion);
        editContrasenhaSesion = findViewById(R.id.editContrasenhaSesion);

        botonIniciarSesion = findViewById(R.id.botonIniciarSesion);
        botonVolverInicio = findViewById(R.id.botonVolverInicio);
    }

    private void configurarBotones() {

        botonVolverInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        botonIniciarSesion.setOnClickListener(v -> {
            iniciarSesionDesdeFormulario();
        });
    }

    private void iniciarSesionDesdeFormulario() {
        viewModel.login(editEmailSesion.getText().toString().trim(),
                editContrasenhaSesion.getText().toString().trim()
        );
    }

    private void mensajeAlIniciarSesion() {
        viewModel.getMensajeError().observe(this, mensaje -> {
            Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
        });

        viewModel.getRegistrarExito().observe(this, usuarioDTO -> {
            Toast.makeText(this, "¡Bienvenido " + usuarioDTO.getNombre() + "!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(LoginActivity.this, MenuPrincipalActivity.class);
            intent.putExtra("ID_USUARIO",usuarioDTO.getIdUsuario());
            startActivity(intent);
            finish();
        });
    }
}