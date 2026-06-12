package com.trabajo.fitnessapp.presentacion.menu.Inicio;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.trabajo.fitnessapp.datos.Utils.Result;
import com.trabajo.fitnessapp.datos.dto.ComidaSeguimientoDTO;
import com.trabajo.fitnessapp.datos.dto.RegistroComidaDiariaDTO;
import com.trabajo.fitnessapp.datos.repository.SeguimientoRepository;

import java.util.ArrayList;
import java.util.List;

public class SeguimientoViewModel extends ViewModel {

    private final MutableLiveData<String> mensajeError = new MutableLiveData<>();

    private final MutableLiveData<List<ComidaSeguimientoDTO>> comidasExito = new MutableLiveData<>();
    private final MutableLiveData<RegistroComidaDiariaDTO> comidaRegistradaExito = new MutableLiveData<>();
    private final MutableLiveData<Boolean> comidaRealizadaBorradaExito = new MutableLiveData<>();

    public LiveData<String> getMensajeError() {
        return mensajeError;
    }

    public LiveData<List<ComidaSeguimientoDTO>> getComidasExito() {
        return comidasExito;
    }

    public LiveData<RegistroComidaDiariaDTO> getComidaRegistradaExito() {
        return comidaRegistradaExito;
    }

    public LiveData<Boolean> getComidaRealizadaBorradaExito() {
        return comidaRealizadaBorradaExito;
    }

    private final SeguimientoRepository seguimientoRepository;

    public SeguimientoViewModel() {
        this.seguimientoRepository = new SeguimientoRepository();
    }

    public void obtenerComidasHoy(Long idUsuario) {
        seguimientoRepository.obtenerComidasHoy(idUsuario).observeForever(result -> {
            if (result instanceof Result.Success) {
                List<ComidaSeguimientoDTO> nuevaLista = new ArrayList<>(((Result.Success<List<ComidaSeguimientoDTO>>) result).datos);
                comidasExito.setValue(nuevaLista);
            } else if (result instanceof Result.Error) {
                mensajeError.setValue(((Result.Error<?>) result).error);
            }
        });
    }

    public void registrarComidaRealizada(Long idUsuario, Long idComida) {
        if (idUsuario == null) {
            mensajeError.setValue("Usuario no encontrado");
            return;
        }

        if (idComida == null) {
            mensajeError.setValue("Comida no encontrada");
            return;
        }

        seguimientoRepository.registrarComidaRealizada(idUsuario, idComida).observeForever(result -> {
            if (result instanceof Result.Success) {
                comidaRegistradaExito.setValue(((Result.Success<RegistroComidaDiariaDTO>) result).datos);
            } else if (result instanceof Result.Error) {
                mensajeError.setValue(((Result.Error<?>) result).error);
            }
        });
    }

    public void eliminarComidaRealizada(Long idUsuario, Long idComida) {
        if (idUsuario == null) {
            mensajeError.setValue("Usuario no encontrado");
            return;
        }

        if (idComida == null) {
            mensajeError.setValue("Comida no encontrada");
            return;
        }

        seguimientoRepository.eliminarComidaRealizada(idUsuario, idComida).observeForever(result -> {
            if (result instanceof Result.Success) {
                comidaRealizadaBorradaExito.setValue(true);
            } else if (result instanceof Result.Error) {
                mensajeError.setValue(((Result.Error<?>) result).error);
                comidaRealizadaBorradaExito.setValue(false);
            }
        });
    }
}
