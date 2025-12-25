package com.trabajo.fitnessapp.datos.api;

import com.trabajo.fitnessapp.datos.dto.AlimentoDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface AlimentosService {

    @GET("api/alimentos/usuarios/{idUsuario}")
    Call<List<AlimentoDTO>> obtenerAlimentosUsuario(@Path("idUsuario") Long idUsuario);

    @PUT("api/alimentos/{idAlimento}/usuarios/{idUsuario}")
    Call<AlimentoDTO> actualizarAlimento(
            @Path("idAlimento") Long idAlimento,
            @Path("idUsuario") Long idUsuario,
            @Body AlimentoDTO alimentoDTO
    );

    @DELETE("api/alimentos/{idAlimento}/usuarios/{idUsuario}")
    Call<Void> borrarAlimento(
            @Path("idAlimento") Long idAlimento,
            @Path("idUsuario") Long idUsuario
    );

    @POST("api/alimentos/usuarios/{idUsuario}")
    Call<AlimentoDTO> crearAlimento(
            @Path("idUsuario") Long idUsuario,
            @Body AlimentoDTO alimentoDTO
    );
}
