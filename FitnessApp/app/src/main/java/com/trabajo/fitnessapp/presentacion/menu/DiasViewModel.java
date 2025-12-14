package com.trabajo.fitnessapp.presentacion.menu;

import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.MutableLiveData;

import com.trabajo.fitnessapp.datos.GestionErrores.Result;
import com.trabajo.fitnessapp.datos.dto.DiaEnDietaDTO;
import com.trabajo.fitnessapp.datos.repository.DiasEnDietaRepository;

import java.util.List;

public class DiasViewModel extends ViewModel {

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

    public DiasViewModel() {
        this.diasEnDietaRepository = new DiasEnDietaRepository();
    }
    public void obtenerLosDias(Long idDietaCompleta) {
        if (idDietaCompleta == null) {
            mensajeError.setValue("Dieta no encontrada");
            return;
        }

        diasEnDietaRepository.obtenerDiasEnDieta(idDietaCompleta).observeForever(result -> {
            if (result instanceof Result.Success) {
                dias.setValue(((Result.Success<List<DiaEnDietaDTO>>) result).datos);
            } else if (result instanceof Result.Error) {
                mensajeError.setValue(((Result.Error<List<DiaEnDietaDTO>>) result).error);
            }
        });
    }
    public void crearDia(Long idDietaCompleta, DiaEnDietaDTO diaEnDietaDTO) {
        if (idDietaCompleta == null) {
            mensajeError.setValue("Dieta no encontrada");
            return;
        }

        if (diaEnDietaDTO.getNombre().isEmpty()) {
            mensajeError.setValue("El nombre del dia no puede estar vacío");
            return;
        }

        diasEnDietaRepository.crearDia(idDietaCompleta, diaEnDietaDTO).observeForever(result -> {
            if (result instanceof Result.Success) {
                diaCreadoExito.setValue(true);
                obtenerLosDias(idDietaCompleta); // recarga días
            } else if (result instanceof Result.Error) {
                mensajeError.setValue(((Result.Error<DiaEnDietaDTO>) result).error);
                diaCreadoExito.setValue(false);
            }
        });
    }
    public void editarDia(Long idDiaEnDieta, Long idDietaCompleta, DiaEnDietaDTO diaEnDietaDTO) {
        if (idDietaCompleta == null) {
            mensajeError.setValue("Dieta no encontrada");
            return;
        }

        if (idDiaEnDieta == null) {
            mensajeError.setValue("Dia no encontrado");
            return;
        }

        if (diaEnDietaDTO.getNombre().trim().isEmpty()) {
            mensajeError.setValue("El nombre del dia no puede estar vacío");
            return;
        }

        diasEnDietaRepository.editarDia(idDiaEnDieta, idDietaCompleta, diaEnDietaDTO).observeForever(result -> {
            if (result instanceof Result.Success) {
                diaActualizadoExito.setValue(true);
                obtenerLosDias(idDietaCompleta);
            } else if (result instanceof Result.Error) {
                mensajeError.setValue(((Result.Error<DiaEnDietaDTO>) result).error);
                diaActualizadoExito.setValue(false);
            }
        });
    }

    public void borrarDia(Long idDiaEnDieta, Long idDietaCompleta) {
        if (idDietaCompleta == null) {
            mensajeError.setValue("Dieta no encontrada");
            return;
        }

        if (idDiaEnDieta == null) {
            mensajeError.setValue("Dia no encontrado");
            return;
        }

        diasEnDietaRepository.borrarDia(idDiaEnDieta, idDietaCompleta).observeForever(result -> {
            if (result instanceof Result.Success) {
                diaBorradoExito.setValue(true);
                obtenerLosDias(idDietaCompleta);
            } else if (result instanceof Result.Error) {
                mensajeError.setValue(((Result.Error<Boolean>) result).error);
                diaBorradoExito.setValue(false);
            }
        });
    }
}
