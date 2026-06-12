package com.trabajo.fitnessapp.datos.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.trabajo.fitnessapp.datos.api.ApiEjerciciosService;
import com.trabajo.fitnessapp.datos.api.RetrofitClient;
import com.trabajo.fitnessapp.datos.dto.ApiEjercicioDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiEjerciciosRepository {

    private final ApiEjerciciosService apiEjerciciosService;

    public ApiEjerciciosRepository() {
        this.apiEjerciciosService = RetrofitClient.getClient().create(ApiEjerciciosService.class);
    }

    public LiveData<List<ApiEjercicioDTO>> obtenerEjercicios() {
        MutableLiveData<List<ApiEjercicioDTO>> listaEjercicios = new MutableLiveData<>();

        apiEjerciciosService.buscarEjercicios().enqueue(new Callback<List<ApiEjercicioDTO>>() {
            @Override
            public void onResponse(Call<List<ApiEjercicioDTO>> call, Response<List<ApiEjercicioDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaEjercicios.postValue(response.body());
                } else {
                    listaEjercicios.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<ApiEjercicioDTO>> call, Throwable t) {
                listaEjercicios.setValue(null);
            }
        });

        return listaEjercicios;
    }
}
