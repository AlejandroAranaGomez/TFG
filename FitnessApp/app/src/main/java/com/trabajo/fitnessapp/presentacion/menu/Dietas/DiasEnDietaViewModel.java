package com.trabajo.fitnessapp.presentacion.menu.Dietas;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.MutableLiveData;

import com.trabajo.fitnessapp.datos.Utils.Result;
import com.trabajo.fitnessapp.datos.dto.DiaEnDietaDTO;
import com.trabajo.fitnessapp.datos.repository.DiasEnDietaRepository;
import com.trabajo.fitnessapp.dominio.DiaDeLaSemana;

import java.util.ArrayList;
import java.util.List;

public class DiasEnDietaViewModel extends ViewModel {

    private final MutableLiveData<String> mensajeError = new MutableLiveData<>();
    public LiveData<String> getMensajeError() {
        return mensajeError;
    }

    private final MutableLiveData<List<DiaEnDietaDTO>> dias = new MutableLiveData<>();
    public LiveData<List<DiaEnDietaDTO>> getDias() {
        return dias;
    }

    private final MutableLiveData<Boolean> diaCreadoExito = new MutableLiveData<>();
    public LiveData<Boolean> getDiaCreadoExito() {
        return diaCreadoExito;
    }

    private final MutableLiveData<Boolean> diaActualizadoExito = new MutableLiveData<>();
    public LiveData<Boolean> getDiaActualizadoExito() {
        return diaActualizadoExito;
    }

    private final MutableLiveData<Boolean> diaBorradoExito = new MutableLiveData<>();
    public LiveData<Boolean> getDiaBorradoExito() {
        return diaBorradoExito;
    }

    private final DiasEnDietaRepository diasEnDietaRepository;

    public DiasEnDietaViewModel() {
        this.diasEnDietaRepository = new DiasEnDietaRepository();
    }

    public void editarDia(DiaDeLaSemana diaDeLaSemana, Long idDietaCompleta, DiaEnDietaDTO diaEnDietaDTO) {
        if (idDietaCompleta == null) {
            mensajeError.setValue("Dieta no encontrada");
            return;
        }

        if (diaDeLaSemana == null) {
            mensajeError.setValue("Dia no encontrado");
            return;
        }

        if (diaEnDietaDTO.getNombre().trim().isEmpty()) {
            mensajeError.setValue("El nombre del dia no puede estar vacío");
            return;
        }

        diasEnDietaRepository.editarDia(diaDeLaSemana, idDietaCompleta, diaEnDietaDTO).observeForever(result -> {
            if (result instanceof Result.Success) {
                diaActualizadoExito.setValue(true);
            } else if (result instanceof Result.Error) {
                mensajeError.setValue(((Result.Error<DiaEnDietaDTO>) result).error);
                diaActualizadoExito.setValue(false);
            }
        });
    }

    public void borrarDia(DiaDeLaSemana diaDeLaSemana, Long idDietaCompleta) {
        if (idDietaCompleta == null) {
            mensajeError.setValue("Dieta no encontrada");
            return;
        }

        if (diaDeLaSemana == null) {
            mensajeError.setValue("Dia no encontrado");
            return;
        }

        diasEnDietaRepository.borrarDia(idDietaCompleta, diaDeLaSemana).observeForever(result -> {
            if (result instanceof Result.Success) {
                diaBorradoExito.setValue(true);
            } else if (result instanceof Result.Error) {
                mensajeError.setValue(((Result.Error<Boolean>) result).error);
                diaBorradoExito.setValue(false);
            }
        });
    }
}
