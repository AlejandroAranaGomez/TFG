package com.trabajo.fitnessapp.datos.api;

import com.trabajo.fitnessapp.datos.dto.ComidaDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ComidaService {

    @GET("api/comidas/dias/{idDiaEnDieta}")
    Call<List<ComidaDTO>> obtenerComidas(@Path("idDiaEnDieta") Long idDiaEnDieta);

    @POST("api/comidas/dias/{idDiaEnDieta}")
    Call<ComidaDTO> crearComida(@Path("idDiaEnDieta") Long idDiaEnDieta, @Body ComidaDTO comidaDTO);

    @PUT("api/comidas/{idComida}/dias/{idDiaEnDieta}")
    Call<ComidaDTO> editarComida(@Path("idComida") Long idComida, @Path("idDiaEnDieta") Long idDiaEnDieta, @Body ComidaDTO comidaDTO);

    @DELETE("api/comidas/{idComida}/dias/{idDiaEnDieta}")
    Call<Void> borrarComida(@Path("idComida") Long idComida, @Path("idDiaEnDieta") Long idDiaEnDieta);

}
