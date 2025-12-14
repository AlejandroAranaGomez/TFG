package com.trabajo.fitnessapp.presentacion.autentification;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.trabajo.fitnessapp.datos.GestionErrores.Result;
import com.trabajo.fitnessapp.datos.dto.InicioSesionDTO;
import com.trabajo.fitnessapp.datos.dto.UsuarioDTO;
import com.trabajo.fitnessapp.datos.repository.AutentificationRepository;

public class LoginViewModel extends ViewModel {

    private final AutentificationRepository autentificationRepository;

    private final MutableLiveData<String> mensajeError = new MutableLiveData<>();
    private final MutableLiveData<UsuarioDTO> inicioSesionExito = new MutableLiveData<>();

    public LoginViewModel() {
        this.autentificationRepository = new AutentificationRepository();
    }
    public LiveData<String> getMensajeError() {
        return mensajeError;
    }

    public LiveData<UsuarioDTO> getRegistrarExito() {
        return inicioSesionExito;
    }

    public void login(String email, String contrasenha) {
        if (email.isEmpty() || contrasenha.isEmpty()) {
            mensajeError.setValue("Por favor, rellena todos los campos");
            return;
        }

        InicioSesionDTO dto = new InicioSesionDTO();

        try {
            dto.setEmail(email);
            dto.setContrasenha(contrasenha);
        } catch (Exception e) {
            mensajeError.setValue("Error al procesar los datos");
        }

        autentificationRepository.iniciarSesion(dto).observeForever(result -> {
            if (result instanceof Result.Success) {
                inicioSesionExito.setValue(((Result.Success<UsuarioDTO>) result).datos);
            } else if (result instanceof Result.Error) {
                mensajeError.setValue(((Result.Error<?>) result).error);
            }
        });
    }

}
