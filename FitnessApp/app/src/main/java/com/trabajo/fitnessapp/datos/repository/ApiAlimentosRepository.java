package com.trabajo.fitnessapp.datos.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.trabajo.fitnessapp.datos.api.ApiAlimentosService;
import com.trabajo.fitnessapp.datos.api.RetrofitClient;
import com.trabajo.fitnessapp.datos.dto.ApiAlimentosDTO;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.util.Log;

public class ApiAlimentosRepository {
    private final ApiAlimentosService apiAlimentosService;
    public ApiAlimentosRepository() {
        this.apiAlimentosService = RetrofitClient.getClient().create(ApiAlimentosService.class);
    }

    public LiveData<List<ApiAlimentosDTO>> buscarAlimentos(String query) {
        MutableLiveData<List<ApiAlimentosDTO>> listaAlimentosConsulta = new MutableLiveData<>();

        apiAlimentosService.buscarAlimentos(query).enqueue(new Callback<List<ApiAlimentosDTO>>() {

            @Override
            public void onResponse(Call<List<ApiAlimentosDTO>> call, Response<List<ApiAlimentosDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaAlimentosConsulta.setValue(response.body());
                } else {
                    listaAlimentosConsulta.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<ApiAlimentosDTO>> call, Throwable t) {
                listaAlimentosConsulta.setValue(null);
            }
        });
        return listaAlimentosConsulta;
    }

}
