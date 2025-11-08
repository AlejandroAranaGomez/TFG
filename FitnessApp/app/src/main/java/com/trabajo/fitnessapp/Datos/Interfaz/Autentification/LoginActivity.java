package com.trabajo.fitnessapp.Datos.Interfaz.Autentification;

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

import com.trabajo.fitnessapp.Datos.Api.AutorizacionService;
import com.trabajo.fitnessapp.Datos.Api.RetrofitClient;
import com.trabajo.fitnessapp.Datos.DTO.InicioSesionDTO;
import com.trabajo.fitnessapp.Datos.Interfaz.Menu.MenuPrincipalActivity;
import com.trabajo.fitnessapp.Datos.Model.UsuarioDTO;
import com.trabajo.fitnessapp.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText editEmailSesion, editContrasenhaSesion;

    private Button botonIniciarSesion;

    private ImageButton botonVolverInicio;

    private AutorizacionService autorizacionService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        botonVolverInicio = findViewById(R.id.botonVolverInicio);

        botonVolverInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                startActivity(intent);
            }
        });

        autorizacionService = RetrofitClient.getClient().create(AutorizacionService.class);

        enlazarVistasLogin();

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
    }

    private void iniciarSesionDesdeFormulario() {
        String contrasenha = editContrasenhaSesion.getText().toString().trim();
        String email = editEmailSesion.getText().toString().trim();

        if (email.isEmpty() || contrasenha.isEmpty()) {

            Toast.makeText(this, "Por favor, rellena todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        InicioSesionDTO dto = new InicioSesionDTO();
        try {
            dto.setContrasenha(contrasenha);
            dto.setEmail(email);
        } catch (Exception e) {
            Log.e("LOGIN_ERROR", "Error al crear DTO", e);
            Toast.makeText(this, "Error al preparar los datos", Toast.LENGTH_SHORT).show();
            return;
        }

        Call<UsuarioDTO> call = autorizacionService.iniciarSesion(dto);
        call.enqueue(new Callback<UsuarioDTO>() {
            @Override
            public void onResponse(Call<UsuarioDTO> call, Response<UsuarioDTO> response) {
                if (response.isSuccessful()) {
                    // ÉXITO (201 Created)
                    Toast.makeText(LoginActivity.this, "¡Registro exitoso!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(LoginActivity.this, MenuPrincipalActivity.class);
                    startActivity(intent);

                } else {
                    String errorMensaje = "Correo o contrasenha incorrectos.";

                    if (response.errorBody() != null) {
                        try {
                            errorMensaje = response.errorBody().string();
                        } catch (Exception e) {
                            Log.e("API_ERROR", "Error al parsear errorBody", e);
                        }
                    }

                    Log.e("API_ERROR", "Error: " + response.code() + errorMensaje);
                    Toast.makeText(LoginActivity.this, errorMensaje, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UsuarioDTO> call, Throwable t) {
                // ERROR DE RED (Sin conexión, IP mal, Server caído)
                Log.e("API_FAILURE", "Fallo de conexión", t);
                Toast.makeText(LoginActivity.this, "Fallo de conexión con el servidor", Toast.LENGTH_SHORT).show();
            }
        });
    }
}