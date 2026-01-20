package com.trabajo.fitnessapp.datos.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.trabajo.fitnessapp.datos.Utils.Result;
import com.trabajo.fitnessapp.datos.api.EjercicioService;
import com.trabajo.fitnessapp.datos.api.RetrofitClient;
import com.trabajo.fitnessapp.datos.dto.EjercicioDTO;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EjercicioRepository {

    private final EjercicioService ejercicioService;

    public EjercicioRepository() {
        this.ejercicioService = RetrofitClient.getClient().create(EjercicioService.class);
    }

    public LiveData<Result<List<EjercicioDTO>>> obtenerEjercicios(Long idDiaEnRutina) {
        MutableLiveData<Result<List<EjercicioDTO>>> ejercicios = new MutableLiveData<>();

        ejercicioService.obtenerEjercicios(idDiaEnRutina).enqueue(new Callback<List<EjercicioDTO>>() {
            @Override
            public void onResponse(Call<List<EjercicioDTO>> call, Response<List<EjercicioDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ejercicios.setValue(new Result.Success<>(response.body()));
                } else {
                    String error = "Error al cargar los ejercicios";
                    try {
                        if (response.errorBody() != null) {
                            error = response.errorBody().string();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ejercicios.setValue(new Result.Error<>(error));
                }
            }

            @Override
            public void onFailure(Call<List<EjercicioDTO>> call, Throwable t) {
                ejercicios.setValue(new Result.Error<>("Error de conexión con la base de datos: " + t.getMessage()));
            }
        });
        return ejercicios;
    }

    public LiveData<Result<Boolean>> anhadirEjercicio(Long idDiaEnRutina, EjercicioDTO ejercicioDTO) {
        MutableLiveData<Result<Boolean>> resultado = new MutableLiveData<>();


        ejercicioService.anhadirEjercicio(idDiaEnRutina, ejercicioDTO).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    resultado.setValue(new Result.Success<>(true));
                } else {
                    String error = "Error al anhadir el ejercicio";
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

    public LiveData<Result<Boolean>> borrarEjercicio(Long idEjercicio, Long idDiaEnRutina) {
        MutableLiveData<Result<Boolean>> resultado = new MutableLiveData<>();

        ejercicioService.borrarEjercicio(idEjercicio, idDiaEnRutina).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    resultado.setValue(new Result.Success<>(true));
                } else {
                    String error = "Error al borrar el ejercicio";
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
