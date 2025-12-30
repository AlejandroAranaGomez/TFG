package com.trabajo.fitnessapp.presentacion.menu.Dietas;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.trabajo.fitnessapp.datos.GestionMensajes.Result;
import com.trabajo.fitnessapp.datos.dto.DietaCompletaDTO;
import com.trabajo.fitnessapp.datos.repository.DietasRepository;

import java.util.List;

public class DietasViewModel extends ViewModel {

    private final DietasRepository dietasRepository;

    private final MutableLiveData<String> mensajeError = new MutableLiveData<>();
    public LiveData<String> getMensajeError() {
        return mensajeError;
    }

    private final MutableLiveData<List<DietaCompletaDTO>> dietas = new MutableLiveData<>();
    public LiveData<List<DietaCompletaDTO>> getDietas() {
        return dietas;
    }

    private final MutableLiveData<Boolean> dietaBorrada = new MutableLiveData<>();
    public LiveData<Boolean> getDietaBorrada() {
        return dietaBorrada;
    }

    private final MutableLiveData<DietaCompletaDTO> dietaCreada = new MutableLiveData<>();
    public LiveData<DietaCompletaDTO> getDietaCreada() {
        return dietaCreada;
    }

    private final MutableLiveData<DietaCompletaDTO> dietaActualizada = new MutableLiveData<>();
    public LiveData<DietaCompletaDTO> getDietaActualizada() {
        return dietaActualizada;
    }

    public DietasViewModel() {
        this.dietasRepository = new DietasRepository();
    }

    public void obtenerLasDietas(Long idUsuario) {
        if (idUsuario == null || idUsuario == -1L) {
            mensajeError.setValue("Usuario no encontrado");
            return;
        }

        dietasRepository.obtenerDietasUsuario(idUsuario).observeForever(result -> {
            if (result instanceof Result.Success) {
                List<DietaCompletaDTO> lista = ((Result.Success<List<DietaCompletaDTO>>) result).datos;
                if (lista != null && !lista.isEmpty()) {
                    dietas.setValue(lista);
                } else {
                    mensajeError.setValue("No tienes ninguna dieta creada");
                }
            } else if (result instanceof Result.Error) {
                mensajeError.setValue("Error al cargar las dietas");
            }
        });
    }

    public void crearDieta(Long idUsuario, DietaCompletaDTO dietaCompletaDTO) {
        if (idUsuario == null || idUsuario == -1L) {
            mensajeError.setValue("Usuario no encontrado");
            return;
        }

        if (dietaCompletaDTO.getNombre().isEmpty()) {
            mensajeError.setValue("Debes ponerle un nombre a la dieta");
            return;
        }

        dietasRepository.crearDieta(idUsuario, dietaCompletaDTO).observeForever(result -> {
            if (result instanceof Result.Success) {
                dietaCreada.setValue(((Result.Success<DietaCompletaDTO>) result).datos);
            } else if (result instanceof Result.Error) {
                mensajeError.setValue(((Result.Error<DietaCompletaDTO>) result).error);
            }
        });
    }

    public void actualizarDieta(Long idUsuario, Long idDietaCompleta, DietaCompletaDTO dietaCompletaDTO) {
        if (idUsuario == null || idUsuario == -1L) {
            mensajeError.setValue("Usuario no encontrado");
            return;
        }

        if (idDietaCompleta == null) {
            mensajeError.setValue("Dieta no encontrada");
            return;
        }

        if (dietaCompletaDTO.getNombre().isEmpty()) {
            mensajeError.setValue("Debes ponerle un nombre a la dieta");
            return;
        }

        dietasRepository.actualizarDieta(idUsuario, idDietaCompleta, dietaCompletaDTO).observeForever(result -> {
            if (result instanceof Result.Success) {
                dietaActualizada.setValue(((Result.Success<DietaCompletaDTO>) result).datos);
            } else if (result instanceof Result.Error) {
                mensajeError.setValue(((Result.Error<DietaCompletaDTO>) result).error);
            }
        });
    }

    public void borrarDieta(Long idDietaCompleta, Long idUsuario) {
        if (idUsuario == null || idUsuario == -1L) {
            mensajeError.setValue("Usuario no encontrado");
            return;
        }
        if (idDietaCompleta == null) {
            mensajeError.setValue("ID de dieta inválido");
            return;
        }

        dietasRepository.borrarDietaCompleta(idDietaCompleta, idUsuario).observeForever(result -> {
            if (result instanceof Result.Success) {
                dietaBorrada.setValue(true);
            } else if (result instanceof Result.Error) {
                mensajeError.setValue(((Result.Error<Boolean>) result).error);
                dietaBorrada.setValue(false);
            }
        });
    }

}
