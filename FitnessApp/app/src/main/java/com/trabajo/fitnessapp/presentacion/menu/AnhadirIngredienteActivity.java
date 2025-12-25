package com.trabajo.fitnessapp.presentacion.menu;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.trabajo.fitnessapp.R;
import com.trabajo.fitnessapp.datos.dto.AlimentoDTO;
import com.trabajo.fitnessapp.datos.dto.IngredienteDTO;
import com.trabajo.fitnessapp.presentacion.adaptador.AlimentosAdapter;
import com.trabajo.fitnessapp.presentacion.adaptador.AlimentosPersonalizadosAdapter;

import java.util.ArrayList;

public class AnhadirIngredienteActivity extends AppCompatActivity {

    private AlimentosViewModel alimentosViewModel;
    private IngredientesViewModel ingredientesViewModel;
    private Button botonBuscar, botondespliegaAlimentos, botonAgregarIngrediente;
    private ImageButton botonVolverAtras;
    private boolean desplegado = false;
    private EditText buscadorAlimentos;
    private AlimentosAdapter alimentosAdapter;
    private AlimentosPersonalizadosAdapter misAlimentosAdapter;
    private RecyclerView recyclerView;
    private RecyclerView recyclerAlimentosPropios;
    private Long idUsuario;
    private Long idComida;
    private TextView textPropiedades;
    private EditText editCantidad;
    private float calorias = 0f;
    private float proteinas = 0f;
    private float carbohidratos = 0f;
    private float grasas = 0f;
    private String nombreAlimento;
    private float gramosSeleccionados = 0f;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_anhadir_ingrediente);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        idUsuario = getIntent().getLongExtra("ID_USUARIO", -1l);
        idComida = getIntent().getLongExtra("ID_COMIDA", -1L);

        enlazarVistas();

        editCantidad.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(android.text.Editable s) {
                if (s.toString().isEmpty()) {
                    mostrarPropiedades100g(calorias, proteinas, carbohidratos, grasas);
                    return;
                }

                try {
                    float gramos = Float.parseFloat(s.toString());
                    actualizarPorCantidad(gramos);
                } catch (NumberFormatException e) {

                }
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        alimentosAdapter = new AlimentosAdapter();
        recyclerView.setAdapter(alimentosAdapter);
        recyclerAlimentosPropios.setLayoutManager(new LinearLayoutManager(this));
        misAlimentosAdapter = new AlimentosPersonalizadosAdapter();
        misAlimentosAdapter.setMostrarBotones(false);
        recyclerAlimentosPropios.setAdapter(misAlimentosAdapter);

        alimentosAdapter.setOnItemClickListener(alimento -> {
            seleccionarAlimento(
                    alimento.getNombre(),
                    alimento.getCalorias(),
                    alimento.getProteinas(),
                    alimento.getCarbohidratos(),
                    alimento.getGrasas()
            );
        });

        misAlimentosAdapter.setOnItemClickListener(new AlimentosPersonalizadosAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(AlimentoDTO alimento) {
                seleccionarAlimento(
                        alimento.getNombre(),
                        alimento.getCalorias(),
                        alimento.getProteinas(),
                        alimento.getCarbohidratos(),
                        alimento.getGrasas()
                );
            }

            @Override
            public void onEditarClick(AlimentoDTO alimento) {
            }

            @Override
            public void onBorrarClick(AlimentoDTO alimento) {

            }
        });

        alimentosViewModel = new ViewModelProvider(this).get(AlimentosViewModel.class);
        ingredientesViewModel = new ViewModelProvider(this).get(IngredientesViewModel.class);

        cargarDatos();
        observarAlimentos();
        observarIngredientes();
        configurarBotones();
    }

    private void enlazarVistas() {
        recyclerView = findViewById(R.id.recyclerAlimentos);
        botonBuscar = findViewById(R.id.botonBuscarAlimento);
        buscadorAlimentos = findViewById(R.id.buscadorAlimentos);
        botondespliegaAlimentos = findViewById(R.id.botonDesplegarMisAlimentos);
        recyclerAlimentosPropios = findViewById(R.id.recyclerMisAlimentos);
        textPropiedades = findViewById(R.id.textPropiedades);
        editCantidad = findViewById(R.id.editCantidad);
        botonAgregarIngrediente = findViewById(R.id.botonAgregar);
        botonVolverAtras = findViewById(R.id.botonVolverActividad);
    }

    private void configurarBotones() {
        botonBuscar.setOnClickListener(v -> {
            String query = buscadorAlimentos.getText().toString();
            if (!query.isEmpty()) {
                buscarEnApi(query);
            } else {
                Toast.makeText(this, "Debes escribir un alimento para poder buscar.", Toast.LENGTH_SHORT).show();
            }
        });

        botondespliegaAlimentos.setOnClickListener(v -> {
            if (desplegado) {
                recyclerAlimentosPropios.setVisibility(View.GONE);
                botondespliegaAlimentos.setText("▼ Mis Alimentos Personalizados");
                desplegado = false;
            } else {
                recyclerAlimentosPropios.setVisibility(View.VISIBLE);
                botondespliegaAlimentos.setText("▲ Mis Alimentos Personalizados");
                desplegado = true;
            }
        });

        botonAgregarIngrediente.setOnClickListener(v -> {
            if (nombreAlimento == null || nombreAlimento.isEmpty()) {
                Toast.makeText(this, "Debes seleccionar un alimento primero", Toast.LENGTH_SHORT).show();
                return;
            }

            String cantidadTexto = editCantidad.getText().toString().trim();
            if (cantidadTexto.isEmpty()) {
                Toast.makeText(this, "Introduce la cantidad en gramos", Toast.LENGTH_SHORT).show();
                return;
            }
            gramosSeleccionados = Float.parseFloat(editCantidad.getText().toString());

            float calFinal = redondear2Decimales((calorias * gramosSeleccionados) / 100f);
            float proFinal = redondear2Decimales((proteinas * gramosSeleccionados) / 100f);
            float carbFinal = redondear2Decimales((carbohidratos * gramosSeleccionados) / 100f);
            float grasaFinal = redondear2Decimales((grasas * gramosSeleccionados) / 100f);

            IngredienteDTO ingrediente = new IngredienteDTO();
            ingrediente.setNombre(nombreAlimento);
            ingrediente.setCantidadEnGramos(gramosSeleccionados);
            ingrediente.setCaloriasTotales(calFinal);
            ingrediente.setProteinas(proFinal);
            ingrediente.setCarbohidratos(carbFinal);
            ingrediente.setGrasas(grasaFinal);

            ingredientesViewModel.crearIngrediente(idComida, ingrediente);
        });

        botonVolverAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private float redondear2Decimales(float valor) {
        return Math.round(valor * 100f) / 100f;
    }

    private void buscarEnApi(String query) {
        alimentosViewModel.buscarAlimentosGlobales(query).observe(this, lista -> {
            if (lista != null && !lista.isEmpty()) {
                alimentosAdapter.setLista(lista);
            } else {
                Toast.makeText(this, "No se encontro el alimento que estas intentando buscar.", Toast.LENGTH_SHORT).show();
                alimentosAdapter.setLista(new ArrayList<>());
            }
        });
    }

    private void cargarDatos() {
        if (idUsuario == null || idUsuario == -1L) {
            Toast.makeText(this, "Error: Usuario no encontrado", Toast.LENGTH_LONG).show();
        }

        alimentosViewModel.obtenerLosAlimentos(idUsuario);
    }

    private void mostrarPropiedades100g(float cal, float pro, float carb, float grasa) {
        String texto = String.format(
                "Cal: %.2f kcal | P: %.2f g | C: %.2f g | G: %.2f g",
                cal, pro, carb, grasa
        );
        textPropiedades.setText(texto);
    }

    private void actualizarPorCantidad(float gramos) {

        float cal = (calorias * gramos) / 100f;
        float pro = (proteinas * gramos) / 100f;
        float carb = (carbohidratos * gramos) / 100f;
        float grasa = (grasas * gramos) / 100f;

        String texto = String.format(
                "Cal: %.2f kcal | P: %.2f g | C: %.2f g | G: %.2f g",
                cal, pro, carb, grasa
        );

        textPropiedades.setText(texto);
    }

    private void seleccionarAlimento(String nombre, float cal, float pro, float carb, float grasa) {

        nombreAlimento = nombre;

        calorias = cal;
        proteinas = pro;
        carbohidratos = carb;
        grasas = grasa;

        mostrarPropiedades100g(cal, pro, carb, grasa);

        editCantidad.setText("");
    }

    private void observarAlimentos() {
        alimentosViewModel.getAlimentos().observe(this, lista -> {
            misAlimentosAdapter.setLista(lista != null ? lista : new ArrayList<>());
        });

        alimentosViewModel.getMensajeError().observe(this, mensaje -> {
            if (mensaje != null && !mensaje.isEmpty()) {
                Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void observarIngredientes() {
        ingredientesViewModel.getIngredienteCreadoConExito().observe(this, exito -> {
            if (exito != null && exito) {
                Toast.makeText(this, "Alimentos creado correctamente", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
}