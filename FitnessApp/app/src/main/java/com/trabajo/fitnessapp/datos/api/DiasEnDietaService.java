package com.trabajo.fitnessapp.datos.api;

import com.trabajo.fitnessapp.datos.dto.DiaEnDietaDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface DiasEnDietaService {

    @GET("api/diasEnDieta/dietas/{idDietaCompleta}")
    Call<List<DiaEnDietaDTO>> obtenerDiasDeDieta(@Path("idDietaCompleta") Long idDietaCompleta);

    @PUT("api/diasEnDieta/{idDiaDieta}/dietas/{idDietaCompleta}")
    Call<DiaEnDietaDTO> editarDia(@Path("idDiaDieta") Long idDiaDieta,
                         @Path("idDietaCompleta") Long idDietaCompleta,
                         @Body DiaEnDietaDTO diaEnDietaDTO);

    @POST("api/diasEnDieta/dietas/{idDietaCompleta}")
    Call<DiaEnDietaDTO> crearDia(@Path("idDietaCompleta") Long idDietaCompleta,
                        @Body DiaEnDietaDTO diaEnDietaDTO);

    @DELETE("api/diasEnDieta/{idDiaDieta}/dietas/{idDietaCompleta}")
    Call<Void> borrarDia(@Path("idDiaDieta") Long idDiaDieta,
                         @Path("idDietaCompleta") Long idDietaCompleta);

}
