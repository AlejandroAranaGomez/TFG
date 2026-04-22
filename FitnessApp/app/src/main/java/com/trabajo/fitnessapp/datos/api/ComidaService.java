package com.trabajo.fitnessapp.datos.api;

import com.trabajo.fitnessapp.datos.dto.ComidaDTO;
import com.trabajo.fitnessapp.dominio.DiaDeLaSemana;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ComidaService {

    @GET("/api/usuarios/{idUsuario}/dietas/{idDieta}/{diaDeLaSemana}/comidas")
    Call<List<ComidaDTO>> obtenerComidas(@Path("idDieta") Long idDieta, @Path("diaDeLaSemana")DiaDeLaSemana diaDeLaSemana);

    @POST("/api/usuarios/{idUsuario}/dietas/{idDieta}/{diaDeLaSemana}/comidas")
    Call<ComidaDTO> crearComida(@Path("idDieta") Long idDieta, @Path("diaDeLaSemana")DiaDeLaSemana diaDeLaSemana, @Body ComidaDTO comidaDTO);

    @PUT("/api/usuarios/{idUsuario}/dietas/{idDieta}/{diaDeLaSemana}/comidas/{idComida}")
    Call<ComidaDTO> editarComida(@Path("idDieta") Long idDieta, @Path("idComida") Long idComida, @Path("diaDeLaSemana")DiaDeLaSemana diaDeLaSemana, @Body ComidaDTO comidaDTO);

    @DELETE("/api/usuarios/{idUsuario}/dietas/{idDieta}/{diaDeLaSemana}/comidas/{idComida}")
    Call<Void> borrarComida(@Path("idDieta") Long idDieta, @Path("idComida") Long idComida, @Path("diaDeLaSemana")DiaDeLaSemana diaDeLaSemana);

}
