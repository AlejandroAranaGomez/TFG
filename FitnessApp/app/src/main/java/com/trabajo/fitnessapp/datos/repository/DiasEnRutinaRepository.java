package com.trabajo.fitnessapp.datos.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.trabajo.fitnessapp.datos.Utils.Result;
import com.trabajo.fitnessapp.datos.api.DiasEnRutinaService;
import com.trabajo.fitnessapp.datos.api.RetrofitClient;
import com.trabajo.fitnessapp.datos.dto.DiaEnRutinaDTO;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DiasEnRutinaRepository {

    private final DiasEnRutinaService diasEnRutinaService;

    public DiasEnRutinaRepository() {
        this.diasEnRutinaService = RetrofitClient.getClient().create(DiasEnRutinaService.class);
    }

    public LiveData<Result<List<DiaEnRutinaDTO>>> obtenerDiasEnRutina(Long idRutinaCompleta) {
        MutableLiveData<Result<List<DiaEnRutinaDTO>>> dias = new MutableLiveData<>();

        diasEnRutinaService.obtenerDiasEnRutina(idRutinaCompleta).enqueue(new Callback<List<DiaEnRutinaDTO>>() {
            @Override
            public void onResponse(Call<List<DiaEnRutinaDTO>> call, Response<List<DiaEnRutinaDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    dias.setValue(new Result.Success<>(response.body()));
                } else {
                    String error = "Error al cargar los días";
                    try {
                        if (response.errorBody() != null) {
                            error = response.errorBody().string();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    dias.setValue(new Result.Error<>(error));
                }
            }

            @Override
            public void onFailure(Call<List<DiaEnRutinaDTO>> call, Throwable t) {
                dias.setValue(new Result.Error<>("Error de conexión con la base de datos: " + t.getMessage()));
            }
        });
        return dias;
    }

    public LiveData<Result<DiaEnRutinaDTO>> crearDia(Long idRutinaCompleta, DiaEnRutinaDTO diaEnRutinaDTO) {
        MutableLiveData<Result<DiaEnRutinaDTO>> resultado = new MutableLiveData<>();

        diasEnRutinaService.crearDia(idRutinaCompleta, diaEnRutinaDTO).enqueue(new Callback<DiaEnRutinaDTO>() {
            @Override
            public void onResponse(Call<DiaEnRutinaDTO> call, Response<DiaEnRutinaDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    resultado.setValue(new Result.Success<>(response.body()));
                } else {
                    String error = "Error al crear el dia";
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
            public void onFailure(Call<DiaEnRutinaDTO> call, Throwable t) {
                resultado.setValue(new Result.Error<>("Error de conexión con la base de datos: " + t.getMessage()));
            }
        });
        return resultado;
    }

    public LiveData<Result<DiaEnRutinaDTO>> editarDia(Long idDiaEnRutina, Long idRutinaCompleta, DiaEnRutinaDTO diaEnRutinaDTO) {
        MutableLiveData<Result<DiaEnRutinaDTO>> resultado = new MutableLiveData<>();

        diasEnRutinaService.editarDia(idDiaEnRutina, idRutinaCompleta, diaEnRutinaDTO).enqueue(new Callback<DiaEnRutinaDTO>() {
            @Override
            public void onResponse(Call<DiaEnRutinaDTO> call, Response<DiaEnRutinaDTO> response) {
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
            public void onFailure(Call<DiaEnRutinaDTO> call, Throwable t) {
                resultado.setValue(new Result.Error<>("Error de conexión con la base de datos: " + t.getMessage()));
            }
        });
        return resultado;
    }

    public LiveData<Result<Boolean>> borrarDia(Long idDiaEnRutina, Long idRutinaCompleta) {
        MutableLiveData<Result<Boolean>> resultado = new MutableLiveData<>();

        diasEnRutinaService.borrarDia(idDiaEnRutina, idRutinaCompleta).enqueue(new Callback<Void>() {
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
        return  resultado;
    }
}
