package com.trabajo.fitnessapp.datos.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.trabajo.fitnessapp.datos.Utils.Result;
import com.trabajo.fitnessapp.datos.api.RetrofitClient;
import com.trabajo.fitnessapp.datos.api.SerieService;
import com.trabajo.fitnessapp.datos.dto.SerieDTO;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SerieRepository {

    private final SerieService serieService;

    public SerieRepository() {
        this.serieService= RetrofitClient.getClient().create(SerieService.class);
    }

    public LiveData<Result<List<SerieDTO>>> obtenerSeries(Long idEjercicioEnDiaRutina) {
        MutableLiveData<Result<List<SerieDTO>>> series = new MutableLiveData<>();

        serieService.obtenerSeries(idEjercicioEnDiaRutina).enqueue(new Callback<List<SerieDTO>>() {
            @Override
            public void onResponse(Call<List<SerieDTO>> call, Response<List<SerieDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    series.setValue(new Result.Success<>(response.body()));
                } else {
                    String error = "Error al obtener las series";
                    try {
                        if (response.errorBody() != null) {
                            error = response.errorBody().string();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    series.setValue(new Result.Error<>(error));
                }
            }

            @Override
            public void onFailure(Call<List<SerieDTO>> call, Throwable t) {
                series.setValue(new Result.Error<>("Error de conexión con la base de datos: " + t.getMessage()));
            }
        });
        return series;
    }

    public LiveData<Result<SerieDTO>> crearSerie(Long idEjercicioEnDiaRutina, SerieDTO serieDTO) {
        MutableLiveData<Result<SerieDTO>> resultado = new MutableLiveData<>();

        serieService.crearSerie(idEjercicioEnDiaRutina, serieDTO).enqueue(new Callback<SerieDTO>() {
            @Override
            public void onResponse(Call<SerieDTO> call, Response<SerieDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    resultado.setValue(new Result.Success<>(response.body()));
                } else {
                    String error = "Error al crear la serie";
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
            public void onFailure(Call<SerieDTO> call, Throwable t) {
                resultado.setValue(new Result.Error<>("Error de conexión con la base de datos: " + t.getMessage()));
            }
        });
        return  resultado;
    }

    public LiveData<Result<SerieDTO>> actualizarSerie(Long idSerie, SerieDTO serieDTO) {
        MutableLiveData<Result<SerieDTO>> resultado = new MutableLiveData<>();

        serieService.actualizarSerie(idSerie, serieDTO).enqueue(new Callback<SerieDTO>() {
            @Override
            public void onResponse(Call<SerieDTO> call, Response<SerieDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    resultado.setValue(new Result.Success<>(response.body()));
                } else {
                    String error = "Error al actualizar la serie";
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
            public void onFailure(Call<SerieDTO> call, Throwable t) {
                resultado.setValue(new Result.Error<>("Error de conexión con la base de datos: " + t.getMessage()));
            }
        });
        return  resultado;
    }

    public LiveData<Result<Boolean>> borrarSerie(Long idSerie) {
        MutableLiveData<Result<Boolean>> resultado = new MutableLiveData<>();

        serieService.borrarSerie(idSerie).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    resultado.setValue(new Result.Success<>(true));
                } else {
                    String error = "Error al borrar la rutina";
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
                resultado.setValue(new Result.Error<>("Error de conexión con la base de datos: " + t.getMessage()));
            }
        });
        return resultado;
    }

}
