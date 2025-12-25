package com.trabajo.fitnessapp.presentacion.menu;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.trabajo.fitnessapp.datos.GestionMensajes.Result;
import com.trabajo.fitnessapp.datos.dto.AlimentoDTO;
import com.trabajo.fitnessapp.datos.dto.ApiAlimentosDTO;
import com.trabajo.fitnessapp.datos.repository.AlimentosRepository;
import com.trabajo.fitnessapp.datos.repository.ApiAlimentosRepository;

import java.util.List;

public class AlimentosViewModel extends ViewModel {

    private final MutableLiveData<String> mensajeError = new MutableLiveData<>();
    public LiveData<String> getMensajeError() {
        return mensajeError;
    }

    private final MutableLiveData<List<AlimentoDTO>> alimentos = new MutableLiveData<>();
    public LiveData<List<AlimentoDTO>> getAlimentos() {
        return alimentos;
    }

    private final MutableLiveData<Boolean> alimentoCreado = new MutableLiveData<>();
    public LiveData<Boolean> getAlimentoCreado() {
        return alimentoCreado;
    }

    private final MutableLiveData<Boolean> alimentoActualizado = new MutableLiveData<>();
    public LiveData<Boolean> getAlimentoActualizado() {
        return alimentoActualizado;
    }

    private final MutableLiveData<Boolean> alimentoBorrado = new MutableLiveData<>();
    public LiveData<Boolean> getAlimentoBorrado() {
        return alimentoBorrado;
    }

    private final AlimentosRepository alimentosRepository;
    private final ApiAlimentosRepository apiAlimentosRepository;

    public AlimentosViewModel() {
        this.alimentosRepository = new AlimentosRepository();
        this.apiAlimentosRepository = new ApiAlimentosRepository();
    }

    public LiveData<List<ApiAlimentosDTO>> buscarAlimentosGlobales(String nombre) {
        return apiAlimentosRepository.buscarAlimentos(nombre);
    }

    public void obtenerLosAlimentos(Long idUsuario) {
        if (idUsuario == null || idUsuario == -1L) {
            mensajeError.setValue("Usuario no encontrado");
            return;
        }

        alimentosRepository.obtenerAlimentosUsuario(idUsuario).observeForever(result -> {
            if (result instanceof Result.Success) {
                alimentos.setValue(((Result.Success<List<AlimentoDTO>>) result).datos);
            } else if (result instanceof Result.Error) {
                mensajeError.setValue(((Result.Error<List<AlimentoDTO>>) result).error);
            }
        });
    }

    public void actualizarAlimento(Long idAlimento, Long idUsuario, AlimentoDTO alimentoDTO) {
        if (idUsuario == null || idUsuario == -1L) {
            mensajeError.setValue("Usuario no encontrado");
            return;
        }

        if (idAlimento == null) {
            mensajeError.setValue("Alimento no encontrado");
            return;
        }

        if (alimentoDTO.getNombre().isEmpty()) {
            mensajeError.setValue("Debes ponerle un nombre al alimento");
            return;
        }

        alimentosRepository.actualizarAlimento(idAlimento, idUsuario, alimentoDTO).observeForever(result -> {
            if (result instanceof Result.Success) {
                alimentoActualizado.setValue(true);
                obtenerLosAlimentos(idUsuario);
            } else if (result instanceof Result.Error) {
                mensajeError.setValue(((Result.Error<AlimentoDTO>) result).error);
                alimentoActualizado.setValue(false);
            }
        });
    }

    public void borrarAlimento(Long idAlimento, Long idUsuario) {
        if (idUsuario == null || idUsuario == -1L) {
            mensajeError.setValue("Usuario no encontrado");
            return;
        }

        if (idAlimento == null) {
            mensajeError.setValue("Alimento no encontrado");
            return;
        }

        alimentosRepository.borrarAlimento(idAlimento, idUsuario).observeForever(result -> {
            if (result instanceof Result.Success) {
                alimentoBorrado.setValue(true);
                obtenerLosAlimentos(idUsuario);
            } else if (result instanceof Result.Error) {
                mensajeError.setValue(((Result.Error<Boolean>) result).error);
                alimentoBorrado.setValue(false);
            }
        });
    }



    public void crearAlimento(Long idUsuario, AlimentoDTO alimentoDTO) {
        if (idUsuario == null || idUsuario == -1L) {
            mensajeError.setValue("Usuario no encontrado");
            return;
        }

        if (alimentoDTO.getNombre().isEmpty()) {
            mensajeError.setValue("Debes ponerle un nombre al alimento");
            return;
        }

        alimentosRepository.crearAlimento(idUsuario, alimentoDTO).observeForever(result -> {
            if (result instanceof Result.Success) {
                alimentoCreado.setValue(true);
                obtenerLosAlimentos(idUsuario);
            } else if (result instanceof Result.Error) {
                mensajeError.setValue(((Result.Error<AlimentoDTO>) result).error);
                alimentoCreado.setValue(false);
            }
        });
    }
}
