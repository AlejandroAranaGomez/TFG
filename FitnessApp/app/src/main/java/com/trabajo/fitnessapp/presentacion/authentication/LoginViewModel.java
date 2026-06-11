package com.trabajo.fitnessapp.presentacion.authentication;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.trabajo.fitnessapp.datos.Utils.Result;
import com.trabajo.fitnessapp.datos.dto.InicioSesionDTO;
import com.trabajo.fitnessapp.datos.dto.UsuarioDTO;
import com.trabajo.fitnessapp.datos.repository.AuthenticationRepository;

public class LoginViewModel extends ViewModel {

    private final AuthenticationRepository autentificationRepository;

    private final MutableLiveData<String> mensajeError = new MutableLiveData<>();
    private final MutableLiveData<UsuarioDTO> inicioSesionExito = new MutableLiveData<>();

    public LoginViewModel() {
        this.autentificationRepository = new AuthenticationRepository();
    }
    public LiveData<String> getMensajeError() {
        return mensajeError;
    }

    public LiveData<UsuarioDTO> getInicioSesionExito() {
        return inicioSesionExito;
    }

    public void login(InicioSesionDTO inicioSesionDTO) {
        if (inicioSesionDTO.getEmail().isEmpty() || inicioSesionDTO.getContrasenha().isEmpty()) {
            mensajeError.setValue("Por favor, rellena todos los campos");
            return;
        }

        autentificationRepository.iniciarSesion(inicioSesionDTO).observeForever(result -> {
            if (result instanceof Result.Success) {
                inicioSesionExito.setValue(((Result.Success<UsuarioDTO>) result).datos);
            } else if (result instanceof Result.Error) {
                mensajeError.setValue(((Result.Error<?>) result).error);
            }
        });
    }
}
