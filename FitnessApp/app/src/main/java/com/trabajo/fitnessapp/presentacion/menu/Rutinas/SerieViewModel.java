package com.trabajo.fitnessapp.presentacion.menu.Rutinas;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.trabajo.fitnessapp.datos.Utils.Result;
import com.trabajo.fitnessapp.datos.dto.SerieDTO;
import com.trabajo.fitnessapp.datos.repository.SerieRepository;

import java.util.ArrayList;
import java.util.List;

public class SerieViewModel extends ViewModel {

    private final MutableLiveData<String> mensajeError = new MutableLiveData<>();
    public LiveData<String> getMensajeError() {
        return mensajeError;
    }

    private final MutableLiveData<List<SerieDTO>> series = new MutableLiveData<>();
    public LiveData<List<SerieDTO>> getSeries() {
        return series;
    }

    private final MutableLiveData<Boolean> serieBorrada = new MutableLiveData<>();
    public LiveData<Boolean> getSerieBorrada() {
        return serieBorrada;
    }

    private final MutableLiveData<SerieDTO> serieCreada = new MutableLiveData<>();
    public LiveData<SerieDTO> getSerieCreada() {
        return serieCreada;
    }

    private final MutableLiveData<SerieDTO> serieActualizada = new MutableLiveData<>();
    public LiveData<SerieDTO> getSerieActualizada() {
        return serieActualizada;
    }

    private SerieRepository serieRepository;

    public SerieViewModel() {
        this.serieRepository = new SerieRepository();
    }

    public void obtenerSeries(Long idEjercicioEnDiaRutina) {
        if (idEjercicioEnDiaRutina == null) {
            mensajeError.setValue("Ejercicio no encontrado");
            return;
        }

        serieRepository.obtenerSeries(idEjercicioEnDiaRutina).observeForever(result -> {
            if (result instanceof Result.Success) {
                List<SerieDTO> lista = ((Result.Success<List<SerieDTO>>) result).datos;
                if (lista != null && !lista.isEmpty()) {
                    series.setValue(lista);
                } else {
                    series.setValue(new ArrayList<>());
                }
            } else if (result instanceof Result.Error) {
                mensajeError.setValue("Error al cargar las series");
            }
        });
    }

    public void crearSerie(Long idEjercicioEnDiaRutina, SerieDTO serieDTO) {
        if (idEjercicioEnDiaRutina == null) {
            mensajeError.setValue("Ejercicio no encontrado");
            return;
        }

        serieRepository.crearSerie(idEjercicioEnDiaRutina, serieDTO).observeForever(result -> {
            if (result instanceof Result.Success) {
                serieCreada.setValue(((Result.Success<SerieDTO>) result).datos);
                obtenerSeries(idEjercicioEnDiaRutina);
            } else if (result instanceof Result.Error) {
                mensajeError.setValue(((Result.Error<SerieDTO>) result).error);
            }
        });
    }

    public void actualizarSerie(Long idSerie, Long idEjercicioEnDiaRutina, SerieDTO serieDTO) {
        if (idSerie == null) {
            mensajeError.setValue("Serie no encontrada");
        }

        serieRepository.actualizarSerie(idSerie, serieDTO).observeForever(result -> {
            if (result instanceof Result.Success) {
                serieActualizada.setValue(((Result.Success<SerieDTO>) result).datos);
                obtenerSeries(idEjercicioEnDiaRutina);
            } else if (result instanceof Result.Error) {
                mensajeError.setValue(((Result.Error<SerieDTO>) result).error);
            }
        });
    }

    public void borrarSerie(Long idSerie, Long idEjercicioEnDiaRutina) {
        if (idSerie == null) {
            mensajeError.setValue("Serie no encontrada");
        }

        serieRepository.borrarSerie(idSerie).observeForever(result -> {
            if (result instanceof Result.Success) {
                serieBorrada.setValue(true);
                obtenerSeries(idEjercicioEnDiaRutina);
            } else if (result instanceof Result.Error) {
                mensajeError.setValue(((Result.Error<Boolean>) result).error);
                serieBorrada.setValue(false);
            }
        });

    }

}
