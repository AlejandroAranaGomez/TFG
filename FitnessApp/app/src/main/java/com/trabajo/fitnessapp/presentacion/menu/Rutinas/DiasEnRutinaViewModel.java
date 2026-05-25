package com.trabajo.fitnessapp.presentacion.menu.Rutinas;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.trabajo.fitnessapp.datos.Utils.Result;
import com.trabajo.fitnessapp.datos.dto.DiaEnRutinaDTO;
import com.trabajo.fitnessapp.datos.repository.DiasEnRutinaRepository;
import com.trabajo.fitnessapp.dominio.DiaDeLaSemana;

import java.util.List;

public class DiasEnRutinaViewModel extends ViewModel {
    private final MutableLiveData<String> mensajeError = new MutableLiveData<>();
    public LiveData<String> getMensajeError() {
        return mensajeError;
    }

    private final MutableLiveData<List<DiaEnRutinaDTO>> dias = new MutableLiveData<>();
    public LiveData<List<DiaEnRutinaDTO>> getDias() {
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

    private final DiasEnRutinaRepository diasEnRutinaRepository;

    public DiasEnRutinaViewModel() {
        this.diasEnRutinaRepository = new DiasEnRutinaRepository();
    }

    public void guardaDia(DiaDeLaSemana diaDeLaSemana, Long idRutinaCompleta, DiaEnRutinaDTO diaEnRutinaDTO) {
        if (idRutinaCompleta == null) {
            mensajeError.setValue("Rutina no encontrada");
            return;
        }

        if(diaDeLaSemana == null) {
            mensajeError.setValue("Dia no encontrado");
            return;
        }

        if (diaEnRutinaDTO.getNombre().isEmpty()) {
            mensajeError.setValue("El nombre del dia no puede estar vacío");
            return;
        }

        diasEnRutinaRepository.editarDia(diaDeLaSemana, idRutinaCompleta, diaEnRutinaDTO).observeForever(result -> {
            if (result instanceof Result.Success) {
                diaActualizadoExito.setValue(true);
            } else if (result instanceof Result.Error) {
                mensajeError.setValue(((Result.Error<DiaEnRutinaDTO>) result).error);
                diaActualizadoExito.setValue(false);
            }
        });
    }

    public void borrarDia(DiaDeLaSemana diaDeLaSemana, Long idRutinaCompleta) {
        if (idRutinaCompleta == null) {
            mensajeError.setValue("Rutina no encontrada");
            return;
        }

        if(diaDeLaSemana == null) {
            mensajeError.setValue("Dia no encontrado");
            return;
        }

        diasEnRutinaRepository.borrarDia(diaDeLaSemana, idRutinaCompleta).observeForever(result -> {
            if (result instanceof Result.Success) {
                diaBorradoExito.setValue(true);
            } else if (result instanceof Result.Error) {
                mensajeError.setValue(((Result.Error<Boolean>) result).error);
                diaBorradoExito.setValue(false);
            }
        });
    }


}
