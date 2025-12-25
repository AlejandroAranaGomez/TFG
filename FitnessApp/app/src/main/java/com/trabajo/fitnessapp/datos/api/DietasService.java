package com.trabajo.fitnessapp.datos.api;

import com.trabajo.fitnessapp.datos.dto.DietaCompletaDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface DietasService {

    @GET("api/dietasCompletas/usuarios/{idUsuario}")
    Call<List<DietaCompletaDTO>> obtenerDietaUsuario(@Path("idUsuario") Long idUsuario);

    @PUT("api/dietasCompletas/{idDietaCompleta}/usuarios/{idUsuario}")
    Call<DietaCompletaDTO> editarDieta(
            @Path("idDietaCompleta") Long idDietaCompleta,
            @Path("idUsuario") Long idUsuario,
            @Body DietaCompletaDTO dietaCompletaDTO
    );

    @POST("api/dietasCompletas/usuarios/{idUsuario}")
    Call<DietaCompletaDTO> crearDieta(
            @Path("idUsuario") Long idUsuario,
            @Body DietaCompletaDTO dietaCompletaDTO
    );

    @DELETE("api/dietasCompletas/{idDietaCompleta}/usuarios/{idUsuario}")
    Call<Void> borrarDieta(
            @Path("idDietaCompleta") Long idDietaCompleta,
            @Path("idUsuario") Long idUsuario
    );

}
