package com.trabajo.fitnessapp.Datos.Interfaz.Autentification;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log; // Para ver logs de error
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast; // Para mostrar mensajes al usuario

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.trabajo.fitnessapp.R;
// --- Imports de tus paquetes de Datos ---
import com.trabajo.fitnessapp.Datos.Api.AutorizacionService;
import com.trabajo.fitnessapp.Datos.Api.RetrofitClient;
import com.trabajo.fitnessapp.Datos.DTO.RegistroDTO;
import com.trabajo.fitnessapp.Datos.Model.UsuarioDTO;
import com.trabajo.fitnessapp.Datos.Model.Generos;

import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    // --- 1. Declarar todos los campos de la UI ---
    // (Asegúrate que los IDs de activity_register.xml coinciden)
    private EditText editNombre, editApellido1, editApellido2,  editEmail, editContrasenha,
            editTelefono, editFechaNacimiento, editPeso, editAltura;

    private Spinner spinnerGenero;
    private Button botonRegistrar;
    private ImageButton botonVolverInicio;

    // El servicio de tu API (usando el nombre de tu clase)
    private AutorizacionService autorizacionService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        // Carga el layout del formulario
        setContentView(R.layout.activity_register);

        // Boton para volver atras

        botonVolverInicio = findViewById(R.id.botonVolverInicio);

        botonVolverInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);

                startActivity(intent);
            }
        });

        // Inicializar el servicio de la API (Retrofit)
        autorizacionService = RetrofitClient.getClient().create(AutorizacionService.class);

        // --- 2. Enlazar la UI ---
        enlazarVistas();

        // Configurar el listener de padding para EdgeToEdge
        // (Asegúrate que tu XML tiene un layout con android:id="@+id/main")
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainRegister), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // --- 3. Asignar la acción al botón ---
        botonRegistrar.setOnClickListener(v -> {
            // Llama al método que hace el trabajo
            registrarUsuarioDesdeFormulario();
        });
    }

    /**
     * Enlaza las variables de Java con los IDs del archivo XML
     */
    private void enlazarVistas() {
        editNombre = findViewById(R.id.editNombre);
        editApellido1 = findViewById(R.id.editApellido1);
        editApellido2 = findViewById(R.id.editApellido2);
        editEmail = findViewById(R.id.editEmail);
        editContrasenha = findViewById(R.id.editContrasenha);
        editTelefono = findViewById(R.id.editTelefono);
        editFechaNacimiento = findViewById(R.id.editFechaNacimiento);

        spinnerGenero = findViewById(R.id.spinnerGenero);

        List<String> opcinesGenero = new ArrayList<>();
        opcinesGenero.add("Genero");
        for (Generos generos : Generos.values()) {
            opcinesGenero.add(generos.name());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, opcinesGenero) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view;

                if (position == 0) {
                    tv.setTextColor(Color.parseColor("#757474"));
                }else {
                    tv.setTextColor(Color.parseColor("#FFFFFF"));
                }
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                view.setBackgroundColor(Color.parseColor("#1E1E1E"));
                tv.setTextColor(Color.WHITE);

                return view;
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGenero.setAdapter(adapter);

        editPeso = findViewById(R.id.editPeso);
        editAltura = findViewById(R.id.editAltura);
        botonRegistrar = findViewById(R.id.botonRegistrar);
    }

    /**
     * Se ejecuta al pulsar el botón de Registrar.
     * Lee, valida y envía los datos a la API.
     */
    private void registrarUsuarioDesdeFormulario() {
        // --- 4. Leer los datos del formulario ---
        String nombre = editNombre.getText().toString().trim();
        String apellido1 = editApellido1.getText().toString().trim();
        String apellido2 = editApellido2.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String contrasenha = editContrasenha.getText().toString().trim();
        String telefono = editTelefono.getText().toString().trim();
        String fechaStr = editFechaNacimiento.getText().toString().trim();
        String generoSelecionado = (String) spinnerGenero.getSelectedItem();
        String pesoStr = editPeso.getText().toString().trim();
        String alturaStr = editAltura.getText().toString().trim();

        // --- 5. Validar los datos (simple) ---
        if (nombre.isEmpty() || apellido1.isEmpty() || email.isEmpty() || contrasenha.isEmpty() ||
                telefono.isEmpty() || fechaStr.isEmpty() ||
                pesoStr.isEmpty() || alturaStr.isEmpty()) {

            Toast.makeText(this, "Por favor, rellena todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!fechaStr.matches("\\d{4}-\\d{2}-\\d{2}")) {
            Toast.makeText(this, "Formato de fecha incorrecto (YYYY-MM-DD)", Toast.LENGTH_SHORT).show();
            return;
        }

        if (generoSelecionado.equals("Genero")) {
            Toast.makeText(this, "Por favor, seleccione un genero", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Formato de email incorrecto.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!telefono.matches("\\d{9}")) {
            Toast.makeText(this, "El telefono debe tener 9 digitos.", Toast.LENGTH_SHORT).show();
            return;
        }


        RegistroDTO dto = new RegistroDTO();
        try {
            // --- 6. Crear el DTO ---
            dto.setNombre(nombre);
            dto.setApellido1(apellido1);
            dto.setApellido2(apellido2); // (Tu DTO debe tener este campo)
            dto.setEmail(email);
            dto.setContrasenha(contrasenha);
            dto.setTelefono(telefono);

            // Convertir String a LocalDate (requiere API 26+)
            dto.setFechaNacimiento(fechaStr);

            dto.setGenero(Generos.valueOf(generoSelecionado));

            // Convertir String a float
            dto.setPeso(Float.parseFloat(pesoStr));
            dto.setAltura(Float.parseFloat(alturaStr));
        } catch (Exception e) {
            Log.e("REGISTRO_ERROR", "Error al crear DTO", e);
            Toast.makeText(this, "Error al preparar los datos", Toast.LENGTH_SHORT).show();
            return;
        }

        // --- 7. Llamar a la API ---
        Call<UsuarioDTO> call = autorizacionService.registrarUsuario(dto);
        call.enqueue(new Callback<UsuarioDTO>() {
            @Override
            public void onResponse(Call<UsuarioDTO> call, Response<UsuarioDTO> response) {
                if (response.isSuccessful()) {
                    // ÉXITO (201 Created)
                    Toast.makeText(RegisterActivity.this, "¡Registro exitoso!", Toast.LENGTH_SHORT).show();

                    // Cierra esta pantalla y vuelve a la anterior (MainActivity)
                    //finish();
                } else {
                    // ERROR DEL SERVIDOR (ej. 400 - Email ya existe)
                    Log.e("API_ERROR", "Error: " + response.code());
                    Toast.makeText(RegisterActivity.this, "Error: El email ya existe", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UsuarioDTO> call, Throwable t) {
                // ERROR DE RED (Sin conexión, IP mal, Server caído)
                Log.e("API_FAILURE", "Fallo de conexión", t);
                Toast.makeText(RegisterActivity.this, "Fallo de conexión con el servidor", Toast.LENGTH_SHORT).show();
            }
        });
    }
}