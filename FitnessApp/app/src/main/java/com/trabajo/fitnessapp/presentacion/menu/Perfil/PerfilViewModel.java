package com.trabajo.fitnessapp.presentacion.menu.Perfil;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.trabajo.fitnessapp.datos.Utils.Result;
import com.trabajo.fitnessapp.datos.dto.UsuarioActualizarDTO;
import com.trabajo.fitnessapp.datos.dto.UsuarioPerfilDTO;
import com.trabajo.fitnessapp.datos.repository.UsuarioRepository;

public class PerfilViewModel extends ViewModel {

    private final UsuarioRepository usuarioRepository;

    private final MutableLiveData<String> mensajeError = new MutableLiveData<>();
    private final MutableLiveData<UsuarioPerfilDTO> perfilExito = new MutableLiveData<>();
    private final MutableLiveData<UsuarioPerfilDTO> perfilActualizado = new MutableLiveData<>();

    public LiveData<String> getMensajeError() {
        return mensajeError;
    }

    public LiveData<UsuarioPerfilDTO> getPerfilExito() {
        return perfilExito;
    }

    public LiveData<UsuarioPerfilDTO> getPerfilActualizado() {
        return perfilActualizado;
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

    public void editarPerfil(Long idUsuario, UsuarioActualizarDTO usuarioActualizarDTO) {

        if(usuarioActualizarDTO.getNombre().isEmpty() || usuarioActualizarDTO.getApellido1().isEmpty()
                || usuarioActualizarDTO.getTelefono().isEmpty() || usuarioActualizarDTO.getFechaNacimiento().isEmpty()){

            mensajeError.setValue("Por favor, rellena todos los campos obligatorios");
            return;
        }

        if (!usuarioActualizarDTO.getFechaNacimiento().matches("\\d{4}-\\d{2}-\\d{2}")) {
            mensajeError.setValue("Formato de fecha incorrecto (YYYY-MM-DD)");
            return;
        }

        if (!usuarioActualizarDTO.getTelefono().matches("\\d{9}")) {
            mensajeError.setValue("El telefono debe tener 9 digitos");
            return;
        }

        if (usuarioActualizarDTO.getAltura() < 100 || usuarioActualizarDTO.getAltura() > 300) {
            mensajeError.setValue("La altura debe estar entre 100 y 300 cm");
            return;
        }

        usuarioRepository.actualizarPerfil(idUsuario, usuarioActualizarDTO).observeForever(result -> {
            if (result instanceof Result.Success) {
                perfilActualizado.setValue(((Result.Success<UsuarioPerfilDTO>) result).datos);
            } else if (result instanceof Result.Error) {
                mensajeError.setValue(((Result.Error<?>) result).error);
            }
        });
    }

}
