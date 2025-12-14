package com.trabajo.fitnessapp.datos.repository;

import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.trabajo.fitnessapp.datos.GestionErrores.Result;
import com.trabajo.fitnessapp.datos.api.DietasService;
import com.trabajo.fitnessapp.datos.api.RetrofitClient;
import com.trabajo.fitnessapp.datos.dto.DietaCompletaDTO;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DietasRepository {

    private final DietasService dietasService;

    public DietasRepository() {
        this.dietasService = RetrofitClient.getClient().create(DietasService.class);
    }

    public LiveData<Result<List<DietaCompletaDTO>>> obtenerDietasUsuario(Long idUsuario) {
        MutableLiveData<Result<List<DietaCompletaDTO>>> dietas = new MutableLiveData<>();

        dietasService.obtenerDietaUsuario(idUsuario).enqueue(new Callback<List<DietaCompletaDTO>>() {
            @Override
            public void onResponse(Call<List<DietaCompletaDTO>> call, Response<List<DietaCompletaDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    dietas.setValue(new Result.Success<>(response.body()));
                } else {
                    String error = "Error al obtener las dietas";
                    try {
                        if (response.errorBody() != null) {
                            error = response.errorBody().string();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    dietas.setValue(new Result.Error<>(error));
                }
            }

            @Override
            public void onFailure(Call<List<DietaCompletaDTO>> call, Throwable t) {
                dietas.setValue(new Result.Error<>("Error de conexión: " + t.getMessage()));
            }
        });
        return dietas;
    }

    public LiveData<Result<DietaCompletaDTO>> crearDieta(Long idUsuario, DietaCompletaDTO dietaCompletaDTO) {
        MutableLiveData<Result<DietaCompletaDTO>> resultado = new MutableLiveData<>();

        dietasService.crearDieta(idUsuario, dietaCompletaDTO).enqueue(new Callback<DietaCompletaDTO>() {
            @Override
            public void onResponse(Call<DietaCompletaDTO> call, Response<DietaCompletaDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    resultado.setValue(new Result.Success<>(response.body()));
                } else {
                    String error = "Error al crear la dieta";
                    try {
                        if (response.errorBody() != null) {
                            error = response.errorBody().string();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    resultado.setValue(new Result.Error<>(error));
                }
            }

            @Override
            public void onFailure(Call<DietaCompletaDTO> call, Throwable t) {
                resultado.setValue(new Result.Error<>("Error de conexión: " + t.getMessage()));
            }
        });
        return resultado;
    }

    public LiveData<Result<DietaCompletaDTO>> actualizarDieta(Long idUsuario, Long idDietaCompleta, DietaCompletaDTO dietaCompletaDTO) {
        MutableLiveData<Result<DietaCompletaDTO>> resultado = new MutableLiveData<>();

        dietasService.editarDieta(idUsuario, idDietaCompleta, dietaCompletaDTO).enqueue(new Callback<DietaCompletaDTO>() {
            @Override
            public void onResponse(Call<DietaCompletaDTO> call, Response<DietaCompletaDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    resultado.setValue(new Result.Success<>(response.body()));
                } else {
                    String error = "Error al actualizar la dieta";
                    try {
                        if (response.errorBody() != null) {
                            error = response.errorBody().string();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    resultado.setValue(new Result.Error<>(error));
                }
            }

            @Override
            public void onFailure(Call<DietaCompletaDTO> call, Throwable t) {
                resultado.setValue(new Result.Error<>("Error de conexión: " + t.getMessage()));
            }
        });
        return resultado;
    }

    public LiveData<Result<Boolean>> borrarDietaCompleta(Long idDietaCompleta, Long idUsuario) {
        MutableLiveData<Result<Boolean>> resultado = new MutableLiveData<>();

        dietasService.borrarDieta(idDietaCompleta, idUsuario).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    resultado.setValue(new Result.Success<>(true));
                } else {
                    String error = "Error al borrar la dieta";
                    try {
                        if (response.errorBody() != null) {
                            error = response.errorBody().string();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    resultado.setValue(new Result.Error<>(error));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                resultado.setValue(new Result.Error<>("Error de conexión: " + t.getMessage()));
            }
        });
        return resultado;
    }

}
