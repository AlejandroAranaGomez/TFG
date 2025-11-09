package com.trabajo.fitnessapp.presentacion.autentification;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.trabajo.fitnessapp.datos.api.AutorizacionService;
import com.trabajo.fitnessapp.datos.api.RetrofitClient;
import com.trabajo.fitnessapp.datos.dto.InicioSesionDTO;
import com.trabajo.fitnessapp.presentacion.menu.MenuPrincipalActivity;
import com.trabajo.fitnessapp.datos.dto.UsuarioDTO;
import com.trabajo.fitnessapp.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel viewModel;
    private EditText editEmailSesion, editContrasenhaSesion;

    private Button botonIniciarSesion;

    private ImageButton botonVolverInicio;

    private AutorizacionService autorizacionService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        enlazarVistasLogin();

        botonVolverInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mensajeAlIniciarSesion();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        botonIniciarSesion.setOnClickListener(v -> {
            // Llama al método que hace el trabajo
            iniciarSesionDesdeFormulario();
        });

    }
    private void enlazarVistasLogin() {
        editEmailSesion = findViewById(R.id.editEmailSesion);
        editContrasenhaSesion = findViewById(R.id.editContrasenhaSesion);

        botonIniciarSesion = findViewById(R.id.botonIniciarSesion);
        botonVolverInicio = findViewById(R.id.botonVolverInicio);
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

        viewModel.getRegistrarExito().observe(this, mensaje -> {
            Toast.makeText(this, "¡Bienvnido de nuevo!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(LoginActivity.this, MenuPrincipalActivity.class);
            startActivity(intent);
            finish();
        });
    }


}