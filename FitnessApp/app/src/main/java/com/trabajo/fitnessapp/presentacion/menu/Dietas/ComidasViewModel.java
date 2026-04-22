package com.trabajo.fitnessapp.presentacion.menu.Dietas;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.trabajo.fitnessapp.datos.Utils.Result;
import com.trabajo.fitnessapp.datos.dto.ComidaDTO;
import com.trabajo.fitnessapp.datos.repository.ComidasRepository;
import com.trabajo.fitnessapp.dominio.DiaDeLaSemana;

import java.util.ArrayList;
import java.util.List;

public class ComidasViewModel extends ViewModel {

    private MutableLiveData<List<ComidaDTO>> comidas = new MutableLiveData<>();
    private MutableLiveData<String> mensajeError = new MutableLiveData<>();

    public LiveData<List<ComidaDTO>> getComidas() {
        return comidas;
    }

    public LiveData<String> getMensajeError() {
        return mensajeError;
    }

    private final MutableLiveData<Boolean> comidaCreadaExito = new MutableLiveData<>();
    public LiveData<Boolean> getComidaCreadaExito() {

        return comidaCreadaExito;
    }

    private final MutableLiveData<Boolean> comidaActualizadaExito = new MutableLiveData<>();
    public LiveData<Boolean> getComidaActualizadaExito() {
        return comidaActualizadaExito;
    }

    private final MutableLiveData<Boolean> comidaBorradaExito = new MutableLiveData<>();
    public LiveData<Boolean> getComidaBorradaExito() {
        return comidaBorradaExito;
    }

    private ComidasRepository comidasRepository;

    public ComidasViewModel() {
        comidasRepository = new ComidasRepository();
    }


    public void obtenerComidas(Long idDieta, DiaDeLaSemana diaDeLaSemana) {
        if (diaDeLaSemana == null) {
            mensajeError.setValue("Dia no encontrado");
            return;
        }

        comidasRepository.obtenerComidas(idDieta, diaDeLaSemana).observeForever(result -> {
            if (result instanceof Result.Success) {
                List<ComidaDTO> nuevaLista = new ArrayList<>(((Result.Success<List<ComidaDTO>>) result).datos);
                comidas.setValue(nuevaLista);
            } else if (result instanceof Result.Error) {
                mensajeError.setValue(((Result.Error<List<ComidaDTO>>) result).error);
            }
        });
    }

    public void crearComida(Long idDieta, DiaDeLaSemana diaDeLaSemana, ComidaDTO comidaDTO) {
        if (diaDeLaSemana == null) {
            mensajeError.setValue("Dia no encontrado");
            return;
        }

        if (comidaDTO.getNombre().trim().isEmpty()) {
            mensajeError.setValue("El nombre de la comida no puede estar vacío");
            return;
        }

        comidasRepository.crearComida(idDieta, diaDeLaSemana, comidaDTO).observeForever(result -> {
            if (result instanceof Result.Success) {
                comidaCreadaExito.setValue(true);
                obtenerComidas(idDieta, diaDeLaSemana);
            } else if (result instanceof Result.Error) {
                mensajeError.setValue(((Result.Error<ComidaDTO>) result).error);
                comidaCreadaExito.setValue(false);
            }
        });
    }

    public void editarComida(Long idDieta, Long idComida, DiaDeLaSemana diaDeLaSemana, ComidaDTO comidaDTO) {
        if (diaDeLaSemana == null) {
            mensajeError.setValue("Dia no encontrado");
            return;
        }

        if (idComida == null) {
            mensajeError.setValue("Comida no encontrada");
            return;
        }

        if (comidaDTO.getNombre().trim().isEmpty()) {
            mensajeError.setValue("El nombre de la comida no puede estar vacío");
            return;
        }

        comidasRepository.editarComida(idDieta ,idComida, diaDeLaSemana, comidaDTO).observeForever(result -> {
            if (result instanceof Result.Success) {
                comidaActualizadaExito.setValue(true);
                obtenerComidas(idDieta, diaDeLaSemana);
            } else if (result instanceof Result.Error) {
                mensajeError.setValue(((Result.Error<ComidaDTO>) result).error);
                comidaActualizadaExito.setValue(false);
            }
        });
    }

    public void borrarComida(Long idDieta, Long idComida, DiaDeLaSemana diaDeLaSemana) {
        if (diaDeLaSemana == null) {
            mensajeError.setValue("Dia no encontrada");
            return;
        }

        if (idComida == null) {
            mensajeError.setValue("Comida no encontrada");
            return;
        }


        comidasRepository.borrarComida(idDieta ,idComida, diaDeLaSemana).observeForever(result -> {
            if (result instanceof Result.Success) {
                comidaBorradaExito.setValue(true);
                obtenerComidas(idDieta, diaDeLaSemana);
            } else if (result instanceof Result.Error) {
                mensajeError.setValue(((Result.Error<?>) result).error);
                comidaBorradaExito.setValue(false);
            }
        });
    }

}
