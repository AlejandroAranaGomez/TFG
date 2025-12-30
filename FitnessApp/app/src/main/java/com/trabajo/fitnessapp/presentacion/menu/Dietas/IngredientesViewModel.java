package com.trabajo.fitnessapp.presentacion.menu.Dietas;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.trabajo.fitnessapp.datos.GestionMensajes.Result;
import com.trabajo.fitnessapp.datos.dto.IngredienteDTO;
import com.trabajo.fitnessapp.datos.repository.IngredienteRepository;

import java.util.List;

public class IngredientesViewModel extends ViewModel {
    private MutableLiveData<List<IngredienteDTO>> ingredientes = new MutableLiveData<>();
    private MutableLiveData<String> mensajeError = new MutableLiveData<>();

    public LiveData<List<IngredienteDTO>> getIngredientes() {

        return ingredientes;
    }

    public LiveData<String> getMensajeError() {

        return mensajeError;
    }

    private final MutableLiveData<Boolean> ingredienteCreadoConExito = new MutableLiveData<>();
    public LiveData<Boolean> getIngredienteCreadoConExito() {

        return ingredienteCreadoConExito;
    }
    private final MutableLiveData<Boolean> ingredienteActualizadoExito = new MutableLiveData<>();
    public LiveData<Boolean> getIngredienteActualizadoExito() {

        return ingredienteActualizadoExito;
    }

    private final MutableLiveData<Boolean> ingredienteBorradoExito = new MutableLiveData<>();
    public LiveData<Boolean> getIngredienteBorradoExito() {

        return ingredienteBorradoExito;
    }

    private IngredienteRepository ingredienteRepository;

    public IngredientesViewModel() {
        ingredienteRepository = new IngredienteRepository();
    }

    public void obtenerIngredientes(Long idComida) {
        if (idComida == null) {
            mensajeError.setValue("Comida no encontrada");
            return;
        }

        ingredienteRepository.obtenerIngrediente(idComida).observeForever(result -> {
            if (result instanceof Result.Success) {
                ingredientes.setValue(((Result.Success<List<IngredienteDTO>>) result).datos);
            } else if (result instanceof Result.Error) {
                mensajeError.setValue(((Result.Error<List<IngredienteDTO>>) result).error);
            }
        });
    }

    public void crearIngrediente(Long idComida, IngredienteDTO ingredienteDTO) {
        if (idComida == null) {
            mensajeError.setValue("Comida no encontrada");
            return;
        }

        ingredienteRepository.crearIngrediente(idComida, ingredienteDTO).observeForever(result -> {
            if (result instanceof Result.Success) {
                ingredienteCreadoConExito.setValue(true);
                obtenerIngredientes(idComida);
            } else if (result instanceof Result.Error) {
                mensajeError.setValue(((Result.Error<IngredienteDTO>) result).error);
                ingredienteCreadoConExito.setValue(false);
            }
        });

    }

    public void editarIngrediente(Long IdIngrediente, Long idComida, IngredienteDTO ingredienteDTO) {
        if (idComida == null) {
            mensajeError.setValue("Comida no encontrada");
            return;
        }

        if (IdIngrediente == null) {
            mensajeError.setValue("Ingrediente no encontrado");
            return;
        }

        if (ingredienteDTO.getCantidadEnGramos() <= 0) {
            mensajeError.setValue("La cantidad debe ser mayor que 0");
            return;
        }

        ingredienteRepository.editarIngrediente(IdIngrediente, idComida, ingredienteDTO).observeForever(result -> {
            if (result instanceof Result.Success) {
                ingredienteActualizadoExito.setValue(true);
                obtenerIngredientes(idComida);
            } else if (result instanceof Result.Error) {
                mensajeError.setValue(((Result.Error<IngredienteDTO>) result).error);
                ingredienteActualizadoExito.setValue(false);
            }
        });
    }

    public void borrarIngrediente(Long idComida, Long idIngrediente) {
        if (idComida == null) {
            mensajeError.setValue("Comida no encontrada");
            return;
        }

        if (idIngrediente == null) {
            mensajeError.setValue("Ingrediente no encontrado");
            return;
        }

        ingredienteRepository.borrarIngrediente(idIngrediente, idComida).observeForever(result -> {
            if (result instanceof Result.Success) {
                ingredienteBorradoExito.setValue(true);
                obtenerIngredientes(idComida);
            } else if (result instanceof Result.Error) {
                mensajeError.setValue(((Result.Error<?>) result).error);
                ingredienteBorradoExito.setValue(false);
            }
        });
    }

}
