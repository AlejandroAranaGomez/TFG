package com.trabajo.fitnessapp.presentacion.autentification;

import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.trabajo.fitnessapp.datos.Utils.Result;
import com.trabajo.fitnessapp.datos.dto.UsuarioDTO;

import com.trabajo.fitnessapp.datos.dto.RegistroDTO;
import com.trabajo.fitnessapp.datos.repository.AutentificationRepository;

public class RegisterViewModel extends ViewModel {

    private final AutentificationRepository autentificationRepository;

    // Variable si hay mensaje de error.
    private final MutableLiveData<String> mensajeError = new MutableLiveData<>();

    // Variable si la respuesta es buena.
    private final MutableLiveData<UsuarioDTO> registrarExito = new MutableLiveData<>();


    public RegisterViewModel() {
        this.autentificationRepository = new AutentificationRepository();
    }

    public LiveData<String> getMensajeError() {
        return mensajeError;
    }

    public LiveData<UsuarioDTO> getRegistrarExito() {
        return registrarExito;
    }

    public void registrar(RegistroDTO registroDTO) {

        if (registroDTO.getNombre().isEmpty() || registroDTO.getApellido1().isEmpty() || registroDTO.getEmail().isEmpty() || registroDTO.getContrasenha().isEmpty() ||
                registroDTO.getTelefono().isEmpty() || registroDTO.getFechaNacimiento().isEmpty()) {

            mensajeError.setValue("Por favor, rellena todos los campos");
            return;
        }

        if (!registroDTO.getFechaNacimiento().matches("\\d{4}-\\d{2}-\\d{2}")) {
            mensajeError.setValue("Formato de fecha incorrecto (YYYY-MM-DD)");
            return;
        }

        if (registroDTO.getGenero().equals("Genero")) {
            mensajeError.setValue("Por favor, seleccione un genero");
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(registroDTO.getEmail()).matches()) {
            mensajeError.setValue("Formato de email incorrecto");
            return;
        }

        if (!registroDTO.getTelefono().matches("\\d{9}")) {
            mensajeError.setValue("El telefono debe tener 9 digitos");
            return;
        }

        if (registroDTO.getAltura() < 100 || registroDTO.getAltura() > 300) {
            mensajeError.setValue("La altura debe estar entre 100 y 300");
            return;
        }

        if (registroDTO.getPeso() < 30 || registroDTO.getPeso() > 300) {
            mensajeError.setValue("El peso debe estar entre 30 y 200");
            return;
        }

        // Devolvemos true o el string del error dependiendo el resultado al registrar el Usuario

        autentificationRepository.registrarUsuario(registroDTO).observeForever(result -> {
            if (result instanceof Result.Success) {
                registrarExito.setValue(((Result.Success<UsuarioDTO>) result).datos);
            } else if (result instanceof Result.Error) {
                mensajeError.setValue(((Result.Error<?>) result).error);
            }
        });

    }

}
