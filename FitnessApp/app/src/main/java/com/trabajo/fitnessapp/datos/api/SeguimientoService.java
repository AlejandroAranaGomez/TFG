package com.trabajo.fitnessapp.datos.api;

import com.trabajo.fitnessapp.datos.dto.ComidaSeguimientoDTO;
import com.trabajo.fitnessapp.datos.dto.RegistroComidaDiariaDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface SeguimientoService {

    @GET("/api/seguimiento/{idUsuario}")
    Call<List<ComidaSeguimientoDTO>> obtenerComidasHoy(@Path("idUsuario") Long idUsuario);

    @POST("/api/seguimiento/{idUsuario}/comida/{idComida}")
    Call<RegistroComidaDiariaDTO> registrarComidaRealizada(@Path("idUsuario") Long idUsuario, @Path("idComida") Long idComida);

    @DELETE("/api/seguimiento/{idUsuario}/comida/{idComida}")
    Call<Void> eliminarComidaRealizada(@Path("idUsuario") Long idUsuario, @Path("idComida") Long idComida);

}
