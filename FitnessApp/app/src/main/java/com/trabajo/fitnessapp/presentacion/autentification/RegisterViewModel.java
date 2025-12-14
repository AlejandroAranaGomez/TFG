package com.trabajo.fitnessapp.presentacion.autentification;

import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.trabajo.fitnessapp.datos.GestionErrores.Result;
import com.trabajo.fitnessapp.datos.dto.UsuarioDTO;

import com.trabajo.fitnessapp.datos.dto.RegistroDTO;
import com.trabajo.fitnessapp.datos.repository.AutentificationRepository;
import com.trabajo.fitnessapp.dominio.Generos;
import com.trabajo.fitnessapp.dominio.NivelDeActividad;
import com.trabajo.fitnessapp.dominio.Objetivos;

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

    public void registrar(String nombre, String apellido1, String apellido2, String email, String contrasenha, String telefono, String fechaNacimiento,
                          String genero, String peso, String altura, Objetivos objetivo, NivelDeActividad nivelDeActividad) {
        if (nombre.isEmpty() || apellido1.isEmpty() || email.isEmpty() || contrasenha.isEmpty() ||
                telefono.isEmpty() || fechaNacimiento.isEmpty() ||
                peso.isEmpty() || altura.isEmpty()) {

            mensajeError.setValue("Por favor, rellena todos los campos");
            return;
        }

        if (!fechaNacimiento.matches("\\d{4}-\\d{2}-\\d{2}")) {
            mensajeError.setValue("Formato de fecha incorrecto (YYYY-MM-DD)");
            return;
        }

        if (genero.equals("Genero")) {
            mensajeError.setValue("Por favor, seleccione un genero");
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mensajeError.setValue("Formato de email incorrecto");
            return;
        }

        if (!telefono.matches("\\d{9}")) {
            mensajeError.setValue("El telefono debe tener 9 digitos");
            return;
        }

        // Creo el dto con los datos.

        RegistroDTO dto = new RegistroDTO();
        try {
            dto.setNombre(nombre);
            dto.setApellido1(apellido1);
            dto.setApellido2(apellido2);
            dto.setEmail(email);
            dto.setContrasenha(contrasenha);
            dto.setTelefono(telefono);
            dto.setFechaNacimiento(fechaNacimiento);
            dto.setGenero(Generos.valueOf(genero));
            dto.setPeso(Float.parseFloat(peso));
            dto.setAltura(Float.parseFloat(altura));
            dto.setObjetivos(objetivo);
            dto.setNivelDeActividad(nivelDeActividad);
        } catch (Exception e) {
            mensajeError.setValue("Error al procesar los datos");
        }

        // Devolvemos true o el string del error dependiendo el resultado al registrar el Usuario

        autentificationRepository.registrarUsuario(dto).observeForever(result -> {
            if (result instanceof Result.Success) {
                registrarExito.setValue(((Result.Success<UsuarioDTO>) result).datos);
            } else if (result instanceof Result.Error) {
                mensajeError.setValue(((Result.Error<?>) result).error);
            }
        });

    }

}
