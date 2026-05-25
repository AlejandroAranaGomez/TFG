package com.trabajo.fitnessapp.datos.api;

import com.trabajo.fitnessapp.datos.dto.IngredienteDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface IngredienteService {

    @GET("/api/usuarios/{idUsuario}/dietas/{idDieta}/{diaDeLaSemana}/comidas/{idComida}/ingredientes")
    Call<List<IngredienteDTO>> obtenerIngredientes(@Path("idComida") Long idComida);

    @POST("/api/usuarios/{idUsuario}/dietas/{idDieta}/{diaDeLaSemana}/comidas/{idComida}/ingredientes")
    Call<IngredienteDTO> crearIngrediente(@Path("idComida") Long idComida, @Body IngredienteDTO ingredienteDTO);

    @PUT("/api/usuarios/{idUsuario}/dietas/{idDieta}/{diaDeLaSemana}/comidas/{idComida}/ingredientes/{idIngrediente}")
    Call<IngredienteDTO> editarIngrediente( @Path("idIngrediente") Long idIngrediente, @Path("idComida") Long idComida, @Body IngredienteDTO ingredienteDTO);

    @DELETE("/api/usuarios/{idUsuario}/dietas/{idDieta}/{diaDeLaSemana}/comidas/{idComida}/ingredientes/{idIngrediente}")
    Call<Void> borrarIngrediente(@Path("idIngrediente") Long idIngrediente, @Path("idComida") Long idComida);
}
