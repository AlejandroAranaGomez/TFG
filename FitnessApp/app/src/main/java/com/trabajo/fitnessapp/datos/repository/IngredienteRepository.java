package com.trabajo.fitnessapp.datos.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.trabajo.fitnessapp.datos.Utils.Result;
import com.trabajo.fitnessapp.datos.api.IngredienteService;
import com.trabajo.fitnessapp.datos.api.RetrofitClient;
import com.trabajo.fitnessapp.datos.dto.IngredienteDTO;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IngredienteRepository {

    private final IngredienteService ingredienteService;

    public IngredienteRepository() {
        this.ingredienteService = RetrofitClient.getClient().create(IngredienteService.class);
    }

    public LiveData<Result<List<IngredienteDTO>>> obtenerIngrediente(Long idComida) {
        MutableLiveData<Result<List<IngredienteDTO>>> ingredientes = new MutableLiveData<>();

        ingredienteService.obtenerIngredientes(idComida).enqueue(new Callback<List<IngredienteDTO>>() {

            @Override
            public void onResponse(Call<List<IngredienteDTO>> call, Response<List<IngredienteDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ingredientes.setValue(new Result.Success<>(response.body()));
                } else {
                    String error = "Error al cargar los ingredientes";
                    try {
                        if (response.errorBody() != null) {
                            error = response.errorBody().string();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ingredientes.setValue(new Result.Error<>(error));
                }
            }

            @Override
            public void onFailure(Call<List<IngredienteDTO>> call, Throwable t) {
                ingredientes.setValue(new Result.Error<>("Error de conexión con la base de datos: " + t.getMessage()));
            }
        });
        return ingredientes;
    }

    public LiveData<Result<IngredienteDTO>> crearIngrediente(Long idComida, IngredienteDTO ingredienteDTO) {
        MutableLiveData<Result<IngredienteDTO>> resultado = new MutableLiveData<>();

        ingredienteService.crearIngrediente(idComida, ingredienteDTO).enqueue(new Callback<IngredienteDTO>() {

            @Override
            public void onResponse(Call<IngredienteDTO> call, Response<IngredienteDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    resultado.setValue(new Result.Success<>(response.body()));
                } else {
                    String error = "Error al crear el ingrediente";
                    try {
                        if (response.errorBody() != null) {
                            error = response.errorBody().string();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    resultado.setValue(new Result.Error<>(error));
                }
            }

            @Override
            public void onFailure(Call<IngredienteDTO> call, Throwable t) {
                resultado.setValue(new Result.Error<>("Error de conexión con la base de datos: " + t.getMessage()));
            }
        });
        return resultado;
    }

    public LiveData<Result<IngredienteDTO>> editarIngrediente(Long idIngrediente, Long idComida, IngredienteDTO ingredienteDTO) {
        MutableLiveData<Result<IngredienteDTO>> resultado = new MutableLiveData<>();

        ingredienteService.editarIngrediente(idIngrediente, idComida, ingredienteDTO).enqueue(new Callback<IngredienteDTO>() {


            @Override
            public void onResponse(Call<IngredienteDTO> call, Response<IngredienteDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    resultado.setValue(new Result.Success<>(response.body()));
                } else {
                    String error = "Error al edtar el ingrediente";
                    try {
                        if (response.errorBody() != null) {
                            error = response.errorBody().string();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    resultado.setValue(new Result.Error<>(error));
                }
            }

            @Override
            public void onFailure(Call<IngredienteDTO> call, Throwable t) {
                resultado.setValue(new Result.Error<>("Error de conexión con la base de datos: " + t.getMessage()));
            }
        });
        return resultado;
    }

    public LiveData<Result<Boolean>> borrarIngrediente(Long idIngrediente, Long idComida) {
        MutableLiveData<Result<Boolean>> resultado = new MutableLiveData<>();

        ingredienteService.borrarIngrediente(idIngrediente, idComida).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    resultado.setValue(new Result.Success<>(true));
                } else {
                    String error = "Error al borrar el ingrediente";
                    try {
                        if (response.errorBody() != null) {
                            error = response.errorBody().string();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    resultado.setValue(new Result.Error<>(error));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                resultado.setValue(new Result.Error<>("Error de conexión con la base de datos: " + t.getMessage()));
            }
        });
        return resultado;
    }


}
