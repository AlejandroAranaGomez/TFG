package com.trabajo.fitnessapp.datos.api;

import com.trabajo.fitnessapp.datos.dto.ApiAlimentosDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiAlimentosService {
    @GET("api/alimentos/buscar")
    Call<List<ApiAlimentosDTO>> buscarAlimentos(
            @Query("query") String query
    );
}
