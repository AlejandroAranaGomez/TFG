package com.trabajo.fitnessapp.presentacion.menu.Rutinas;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.trabajo.fitnessapp.datos.Utils.Result;
import com.trabajo.fitnessapp.datos.dto.DiaEnRutinaDTO;
import com.trabajo.fitnessapp.datos.repository.DiasEnRutinaRepository;

import java.util.ArrayList;
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

    public void obtenerDias(Long idRutinaCompleta) {
        if (idRutinaCompleta == null) {
            mensajeError.setValue("Rutina no encontrada");
            return;
        }

        diasEnRutinaRepository.obtenerDiasEnRutina(idRutinaCompleta).observeForever(result -> {
            if (result instanceof Result.Success) {
                List<DiaEnRutinaDTO> nuevaLista = new ArrayList<>(((Result.Success<List<DiaEnRutinaDTO>>) result).datos);
                dias.setValue(nuevaLista);
            } else if (result instanceof Result.Error) {
                mensajeError.setValue(((Result.Error<List<DiaEnRutinaDTO>>) result).error);
            }
        });
    }

    public void crearDia(Long idRutinaCompleta, DiaEnRutinaDTO diaEnRutinaDTO) {
        if (idRutinaCompleta == null) {
            mensajeError.setValue("Rutina no encontrada");
            return;
        }

        if (diaEnRutinaDTO.getNombre().isEmpty()) {
            mensajeError.setValue("El nombre del dia no puede estar vacío");
            return;
        }

        diasEnRutinaRepository.crearDia(idRutinaCompleta, diaEnRutinaDTO).observeForever(result -> {
            if (result instanceof Result.Success) {
                diaCreadoExito.setValue(true);
                obtenerDias(idRutinaCompleta);
            } else if (result instanceof Result.Error) {
                mensajeError.setValue(((Result.Error<DiaEnRutinaDTO>) result).error);
                diaCreadoExito.setValue(false);
            }
        });
    }

    public void editaDia(Long idDiaEnRutina, Long idRutinaCompleta, DiaEnRutinaDTO diaEnRutinaDTO) {
        if (idRutinaCompleta == null) {
            mensajeError.setValue("Rutina no encontrada");
            return;
        }

        if(idDiaEnRutina == null) {
            mensajeError.setValue("Dia no encontrado");
            return;
        }

        if (diaEnRutinaDTO.getNombre().isEmpty()) {
            mensajeError.setValue("El nombre del dia no puede estar vacío");
            return;
        }

        diasEnRutinaRepository.editarDia(idDiaEnRutina, idRutinaCompleta, diaEnRutinaDTO).observeForever(result -> {
            if (result instanceof Result.Success) {
                diaActualizadoExito.setValue(true);
                obtenerDias(idRutinaCompleta);
            } else if (result instanceof Result.Error) {
                mensajeError.setValue(((Result.Error<DiaEnRutinaDTO>) result).error);
                diaActualizadoExito.setValue(false);
            }
        });
    }

    public void borrarDia(Long idDiaEnRutina, Long idRutinaCompleta) {
        if (idRutinaCompleta == null) {
            mensajeError.setValue("Rutina no encontrada");
            return;
        }

        if(idDiaEnRutina == null) {
            mensajeError.setValue("Dia no encontrado");
            return;
        }

        diasEnRutinaRepository.borrarDia(idDiaEnRutina, idRutinaCompleta).observeForever(result -> {
            if (result instanceof Result.Success) {
                diaBorradoExito.setValue(true);
                obtenerDias(idRutinaCompleta);
            } else if (result instanceof Result.Error) {
                mensajeError.setValue(((Result.Error<Boolean>) result).error);
                diaBorradoExito.setValue(false);
            }
        });
    }


}
