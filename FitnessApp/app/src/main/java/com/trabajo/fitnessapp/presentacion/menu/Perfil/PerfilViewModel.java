package com.trabajo.fitnessapp.presentacion.menu.Perfil;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.trabajo.fitnessapp.datos.Utils.Result;
import com.trabajo.fitnessapp.datos.dto.UsuarioPerfilDTO;
import com.trabajo.fitnessapp.datos.repository.UsuarioRepository;

public class PerfilViewModel extends ViewModel {

    private final UsuarioRepository usuarioRepository;

    private final MutableLiveData<String> mensajeError = new MutableLiveData<>();
    private final MutableLiveData<UsuarioPerfilDTO> perfilExito = new MutableLiveData<>();

    public LiveData<String> getMensajeError() {
        return mensajeError;
    }

    public LiveData<UsuarioPerfilDTO> getPerfilExito() {
        return perfilExito;
    }

    public PerfilViewModel() {
        this.usuarioRepository = new UsuarioRepository();
    }

    public void cargarPerfil(Long idUsuario) {

        usuarioRepository.obtenerPerfil(idUsuario).observeForever(result -> {
            if (result instanceof Result.Success) {
                perfilExito.setValue(((Result.Success<UsuarioPerfilDTO>) result).datos);
            } else if (result instanceof Result.Error) {
                mensajeError.setValue(((Result.Error<?>) result).error);
            }
        });
    }

}
