package com.trabajo.fitnessapp.presentacion.menu.Rutinas;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.trabajo.fitnessapp.datos.Utils.Result;
import com.trabajo.fitnessapp.datos.dto.ApiEjercicioDTO;
import com.trabajo.fitnessapp.datos.dto.EjercicioDTO;
import com.trabajo.fitnessapp.datos.repository.ApiEjerciciosRepository;
import com.trabajo.fitnessapp.datos.repository.EjercicioRepository;
import com.trabajo.fitnessapp.dominio.DiaDeLaSemana;

import java.util.List;

public class EjerciciosViewModel extends ViewModel {

    private final ApiEjerciciosRepository apiEjerciciosRepository;
    private final EjercicioRepository ejercicioRepository;

    private final MutableLiveData<String> mensajeError = new MutableLiveData<>();

    public LiveData<String> getMensajeError() {
        return mensajeError;
    }

    private final MutableLiveData<List<EjercicioDTO>> ejerciciosRutina = new MutableLiveData<>();
    public LiveData<List<EjercicioDTO>> getEjerciciosRutina() {
        return ejerciciosRutina;
    }
    private final MutableLiveData<Boolean> ejercicioAnhadido = new MutableLiveData<>();
    public LiveData<Boolean> getEjercicioAnhadido() {
        return ejercicioAnhadido;
    }
    private final MutableLiveData<Boolean> ejercicioBorrado = new MutableLiveData<>();
    public LiveData<Boolean> getEjercicioBorrado() {
        return ejercicioBorrado;
    }

    private LiveData<List<ApiEjercicioDTO>> ejercicios;

    public EjerciciosViewModel() {
        apiEjerciciosRepository = new ApiEjerciciosRepository();
        ejercicioRepository = new EjercicioRepository();
    }

    public LiveData<List<ApiEjercicioDTO>> ejerciciosGlobales() {
        if (ejercicios == null) {
            ejercicios = apiEjerciciosRepository.obtenerEjercicios();
        }
        return ejercicios;
    }

    public void obtenerLosEjercicios(Long idRutina, DiaDeLaSemana diaDeLaSemana) {
        if (diaDeLaSemana == null) {
            mensajeError.setValue("Dia no encontrado");
        }

        ejercicioRepository.obtenerEjercicios(idRutina, diaDeLaSemana).observeForever(result -> {
            if (result instanceof Result.Success) {
                ejerciciosRutina.setValue(((Result.Success<List<EjercicioDTO>>) result).datos);
            } else if (result instanceof Result.Error) {
                mensajeError.setValue(((Result.Error<List<EjercicioDTO>>) result).error);
            }
        });
    }

    public void anhadirEjercicio(Long idRutina, DiaDeLaSemana diaDeLaSemana, EjercicioDTO ejercicioDTO) {
        if (diaDeLaSemana == null) {
            mensajeError.setValue("Dia no encontrado");
        }

        ejercicioRepository.anhadirEjercicio(idRutina, diaDeLaSemana, ejercicioDTO).observeForever(result -> {
            if (result instanceof Result.Success) {
                ejercicioAnhadido.postValue(true);
                obtenerLosEjercicios(idRutina, diaDeLaSemana);
            } else if (result instanceof Result.Error) {
                mensajeError.setValue(((Result.Error<Boolean>) result).error);
                ejercicioAnhadido.postValue(false);
            }
        });
    }

    public void borrarEjercicio(Long idRutina, DiaDeLaSemana diaDeLaSemana, Long idEjercicio) {
        if (diaDeLaSemana == null) {
            mensajeError.setValue("Dia no encontrado");
        }

        if (idEjercicio == null) {
            mensajeError.setValue("Ejercicio no encontrado");
        }

        ejercicioRepository.borrarEjercicio(idRutina, diaDeLaSemana, idEjercicio).observeForever(result -> {
            if (result instanceof Result.Success) {
                ejercicioBorrado.setValue(true);
                obtenerLosEjercicios(idRutina, diaDeLaSemana);
            } else if (result instanceof Result.Error) {
                mensajeError.setValue(((Result.Error<Boolean>) result).error);
                ejercicioBorrado.setValue(false);
            }
        });
    }

}
