package com.trabajo.fitnessapp.presentacion.menu.Rutinas;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.trabajo.fitnessapp.datos.Utils.Result;
import com.trabajo.fitnessapp.datos.dto.RutinaCompletaDTO;
import com.trabajo.fitnessapp.datos.repository.RutinasRepository;

import java.util.List;

public class RutinasViewModel extends ViewModel {
    private RutinasRepository rutinasRepository;

    private final MutableLiveData<String> mensajeError = new MutableLiveData<>();
    public LiveData<String> getMensajeError() {
        return mensajeError;
    }

    private final MutableLiveData<List<RutinaCompletaDTO>> rutinas = new MutableLiveData<>();
    public LiveData<List<RutinaCompletaDTO>> getRutinas() {
        return rutinas;
    }

    private final MutableLiveData<Boolean> rutinaBorrada = new MutableLiveData<>();
    public LiveData<Boolean> getRutinaBorrada() {
        return  rutinaBorrada;
    }

    private final MutableLiveData<RutinaCompletaDTO> rutinaCreada = new MutableLiveData<>();
    public LiveData<RutinaCompletaDTO> getRutinaCreada() {
        return rutinaCreada;
    }

    private final MutableLiveData<RutinaCompletaDTO> rutinaActualizada = new MutableLiveData<>();
    public LiveData<RutinaCompletaDTO> getRutinaActualizada() {
        return rutinaActualizada;
    }

    public RutinasViewModel() {
        this.rutinasRepository = new RutinasRepository();
    }

    public void obtenerLasRutinas(Long idUsuario) {
        if (idUsuario == null || idUsuario == -1L) {
            mensajeError.setValue("Usuario no encontrado");
            return;
        }

        rutinasRepository.obtenerRutinasUsuario(idUsuario).observeForever(result -> {
            if (result instanceof Result.Success) {
                List<RutinaCompletaDTO> lista = ((Result.Success<List<RutinaCompletaDTO>>) result).datos;
                if (lista != null && !lista.isEmpty()) {
                    rutinas.setValue(lista);
                } else {
                    mensajeError.setValue("No tienes ninguna rutina creada");
                }
            } else if (result instanceof Result.Error) {
                mensajeError.setValue("Error al cargar las rutinas");
            }
        });
    }

    public void crearRutina(Long idUsuario, RutinaCompletaDTO rutinaCompletaDTO) {
        if (idUsuario == null || idUsuario == -1L) {
            mensajeError.setValue("Usuario no encontrado");
            return;
        }

        if (rutinaCompletaDTO.getNombreRutinaCompleta().isEmpty()) {
            mensajeError.setValue("Debes ponerle un nombre a la rutina.");
            return;
        }

        rutinasRepository.crearRutina(idUsuario, rutinaCompletaDTO).observeForever(result -> {
            if (result instanceof Result.Success) {
                rutinaCreada.setValue(((Result.Success<RutinaCompletaDTO>) result).datos);
            } else if (result instanceof Result.Error) {
                mensajeError.setValue(((Result.Error<RutinaCompletaDTO>) result).error);
            }
        });
    }

    public void actualizarRutina(Long idUsuario, Long idRutinaCompleta, RutinaCompletaDTO rutinaCompletaDTO) {
        if (idUsuario == null || idUsuario == -1L) {
            mensajeError.setValue("Usuario no encontrado");
            return;
        }

        if (idRutinaCompleta == null) {
            mensajeError.setValue("Rutina no encontrada");
            return;
        }

        if (rutinaCompletaDTO.getNombreRutinaCompleta().isEmpty()) {
            mensajeError.setValue("Debes ponerle un nombre a la rutina");
            return;
        }

        rutinasRepository.actualizarRutina(idUsuario, idRutinaCompleta, rutinaCompletaDTO).observeForever(result -> {
            if (result instanceof Result.Success) {
                rutinaActualizada.setValue(((Result.Success<RutinaCompletaDTO>) result).datos);
            } else if (result instanceof Result.Error) {
                mensajeError.setValue(((Result.Error<RutinaCompletaDTO>) result).error);
            }
        });
    }

    public void borrarRutina(Long idUsuario, Long idRutinaCompleta) {
        if (idUsuario == null || idUsuario == -1L) {
            mensajeError.setValue("Usuario no encontrado");
            return;
        }
        if (idRutinaCompleta == null) {
            mensajeError.setValue("Rutina no encontrada");
            return;
        }

        rutinasRepository.borrarRutina(idUsuario, idRutinaCompleta).observeForever(result -> {
            if (result instanceof Result.Success) {
                rutinaBorrada.setValue(true);
            } else if (result instanceof Result.Error) {
                mensajeError.setValue(((Result.Error<Boolean>) result).error);
                rutinaBorrada.setValue(false);
            }
        });
    }
}
