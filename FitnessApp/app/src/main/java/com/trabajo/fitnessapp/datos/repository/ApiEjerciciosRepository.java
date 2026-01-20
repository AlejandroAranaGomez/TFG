package com.trabajo.fitnessapp.datos.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.trabajo.fitnessapp.datos.api.ApiEjerciciosService;
import com.trabajo.fitnessapp.datos.api.EjerciciosRetrofit;
import com.trabajo.fitnessapp.datos.dto.ApiEjercicioDTO;
import com.trabajo.fitnessapp.datos.dto.RespuestaEjerciciosDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiEjerciciosRepository {

    private static final String APP_KEY = "4451dfe8a5mshfb27bdef97ae35ep143379jsn27500955e5f1";

    private final ApiEjerciciosService apiEjerciciosService;

    public ApiEjerciciosRepository() {
        this.apiEjerciciosService = EjerciciosRetrofit.getClient().create(ApiEjerciciosService.class);
    }

    public LiveData<List<ApiEjercicioDTO>> obtenerEjercicios() {
        MutableLiveData<List<ApiEjercicioDTO>> listaEjercicios = new MutableLiveData<>();

        apiEjerciciosService.obtenerEjercicios(1, 1000).enqueue(new Callback<RespuestaEjerciciosDTO>() {
            @Override
            public void onResponse(Call<RespuestaEjerciciosDTO> call, Response<RespuestaEjerciciosDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaEjercicios.postValue(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<RespuestaEjerciciosDTO> call, Throwable t) {
                t.printStackTrace();
            }
        });
        return listaEjercicios;
    }
}
