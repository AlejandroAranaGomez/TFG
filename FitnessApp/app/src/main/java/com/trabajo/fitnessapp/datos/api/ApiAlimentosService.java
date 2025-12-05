package com.trabajo.fitnessapp.datos.api;

import com.trabajo.fitnessapp.datos.dto.ApiAlimentosResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiAlimentosService {

    @GET("api/food-database/v2/parser")
    Call<ApiAlimentosResponse> buscarAlimentos (
        @Query("app_id") String appId,
        @Query("app_key") String appKey,
        @Query("ingr") String query
    );


}
