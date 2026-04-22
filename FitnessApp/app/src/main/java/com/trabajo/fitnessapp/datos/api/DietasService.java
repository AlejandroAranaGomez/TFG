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

    @GET("api/usuarios/{idUsuario}/dietas")
    Call<List<DietaCompletaDTO>> obtenerDietaUsuario(@Path("idUsuario") Long idUsuario);

    @PUT("api/usuarios/{idUsuario}/dietas/{idDieta}")
    Call<DietaCompletaDTO> editarDieta(
            @Path("idDieta") Long idDietaCompleta,
            @Path("idUsuario") Long idUsuario,
            @Body DietaCompletaDTO dietaCompletaDTO
    );

    @POST("api/usuarios/{idUsuario}/dietas")
    Call<DietaCompletaDTO> crearDieta(
            @Path("idUsuario") Long idUsuario,
            @Body DietaCompletaDTO dietaCompletaDTO
    );

    @DELETE("api/usuarios/{idUsuario}/dietas/{idDieta}")
    Call<Void> borrarDieta(
            @Path("idDieta") Long idDietaCompleta,
            @Path("idUsuario") Long idUsuario
    );

    @GET("api/usuarios/{idUsuario}/dietas/{idDieta}")
    Call<DietaCompletaDTO> obtenerDieta(
            @Path("idUsuario") Long idUsuario,
            @Path("idDieta") Long idDieta
    );

    @PUT("api/usuarios/{idUsuario}/dietas/{idDieta}/activar")
    Call<Void> activarDieta(
            @Path("idUsuario") Long idUsuario,
            @Path("idDieta") Long idDieta
    );

}
