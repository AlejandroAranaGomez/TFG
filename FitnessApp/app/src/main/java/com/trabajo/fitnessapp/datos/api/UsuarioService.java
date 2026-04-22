package com.trabajo.fitnessapp.datos.api;

import com.trabajo.fitnessapp.datos.dto.HistorialPesoDTO;
import com.trabajo.fitnessapp.datos.dto.PesoDTO;
import com.trabajo.fitnessapp.datos.dto.UsuarioActualizarDTO;
import com.trabajo.fitnessapp.datos.dto.UsuarioPerfilDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UsuarioService {

    @GET("api/usuarios/{idUsuario}")
    Call<UsuarioPerfilDTO> obtenerPerfil(@Path("idUsuario") Long idUsuario);

    @PUT("api/usuarios/{idUsuario}")
    Call<UsuarioPerfilDTO> actualizarPerfil(@Path("idUsuario") Long idUsuario, @Body UsuarioActualizarDTO dto);

    @GET("api/usuarios/{idUsuario}/historialPeso")
    Call<List<HistorialPesoDTO>> obtenerHistorialPeso(@Path("idUsuario") Long idUsuario);

    @PUT("api/usuarios/{idUsuario}/historialPeso")
    Call<UsuarioPerfilDTO> actualizarPeso(@Path("idUsuario") Long idUsuario, @Body PesoDTO nuevoPeso);

}
