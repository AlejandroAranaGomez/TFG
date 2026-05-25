package com.trabajo.fitnessapp.datos.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.trabajo.fitnessapp.datos.Utils.Result;
import com.trabajo.fitnessapp.datos.api.DiasEnDietaService;
import com.trabajo.fitnessapp.datos.api.RetrofitClient;
import com.trabajo.fitnessapp.datos.dto.DiaEnDietaDTO;
import com.trabajo.fitnessapp.dominio.DiaDeLaSemana;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class    DiasEnDietaRepository {

    private final DiasEnDietaService diasEnDietaService;

    public DiasEnDietaRepository() {
        this.diasEnDietaService = RetrofitClient.getClient().create(DiasEnDietaService.class);
    }
    public LiveData<Result<DiaEnDietaDTO>> editarDia(DiaDeLaSemana diaDeLaSemana, Long idDietaCompleta, DiaEnDietaDTO diaEnDietaDTO) {
        MutableLiveData<Result<DiaEnDietaDTO>> resultado = new MutableLiveData<>();

        diasEnDietaService.guardarDia(idDietaCompleta, diaDeLaSemana, diaEnDietaDTO).enqueue(new Callback<DiaEnDietaDTO>() {
            @Override
            public void onResponse(Call<DiaEnDietaDTO> call, Response<DiaEnDietaDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    resultado.setValue(new Result.Success<>(response.body()));
                } else {
                    String error = "Error al edtar el dia";
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
            public void onFailure(Call<DiaEnDietaDTO> call, Throwable t) {
                resultado.setValue(new Result.Error<>("Error de conexión con la base de datos: " + t.getMessage()));
            }
        });
        return resultado;
    }

    public LiveData<Result<Boolean>> borrarDia(Long idDietaCompleta, DiaDeLaSemana diaDeLaSemana) {
        MutableLiveData<Result<Boolean>> resultado = new MutableLiveData<>();

        diasEnDietaService.borrarDia(idDietaCompleta, diaDeLaSemana).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    resultado.setValue(new Result.Success<>(true));
                } else {
                    String error = "Error al borrar el dia";
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
