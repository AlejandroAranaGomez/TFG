package com.trabajo.fitnessapp.datos.api;

import com.trabajo.fitnessapp.datos.dto.UsuarioPerfilDTO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface UsuarioService {

    @GET("api/usuarios/{idUsuario}/perfil")
    Call<UsuarioPerfilDTO> obtenerPerfil(@Path("idUsuario") Long idUsuario);

}
