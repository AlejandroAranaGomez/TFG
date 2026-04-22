package com.trabajo.fitnessapp.presentacion.menu.Progreso;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.trabajo.fitnessapp.datos.Utils.Result;
import com.trabajo.fitnessapp.datos.dto.HistorialPesoDTO;
import com.trabajo.fitnessapp.datos.dto.PesoDTO;
import com.trabajo.fitnessapp.datos.dto.UsuarioPerfilDTO;
import com.trabajo.fitnessapp.datos.repository.UsuarioRepository;

import java.util.List;

public class ProgresoViewModel extends ViewModel {

    private UsuarioRepository usuarioRepository;

    public ProgresoViewModel() {
        usuarioRepository = new UsuarioRepository();
    }

    private final MutableLiveData<String> mensajeError = new MutableLiveData<>();
    private final MutableLiveData<List<HistorialPesoDTO>> historialPeso = new MutableLiveData<>();
    private final MutableLiveData<UsuarioPerfilDTO> pesoActualizado = new MutableLiveData<>();

    public LiveData<String> getMensajeError() {
        return mensajeError;
    }
    public LiveData<List<HistorialPesoDTO>> getHistorialPeso() {
        return historialPeso;
    }
    public LiveData<UsuarioPerfilDTO> getPesoActualizado() {
        return pesoActualizado;
    }

    public void cargarHistorial(Long idUsuario) {
        usuarioRepository.obtenerHistorialPeso(idUsuario).observeForever(result -> {
            if(result instanceof Result.Success){
                historialPeso.setValue(((Result.Success<List<HistorialPesoDTO>>) result).datos);
            }

            else if(result instanceof Result.Error){
                mensajeError.setValue(((Result.Error<?>) result).error);
            }
        });
    }

    public void actualizarPeso(Long idUsuario, PesoDTO pesoDTO) {

        if(pesoDTO.getPeso() <= 30 || pesoDTO.getPeso() >= 300) {
            mensajeError.setValue("El peso debe ser mayor que 30 y menor que 300");
            return;
        }

        usuarioRepository.actualizarPeso(idUsuario, pesoDTO).observeForever(result -> {

            if(result instanceof Result.Success){
                pesoActualizado.setValue(((Result.Success<UsuarioPerfilDTO>) result).datos);

                // recargar historial para actualizar la gráfica
                cargarHistorial(idUsuario);
            }

            else if (result instanceof Result.Error) {
                mensajeError.setValue(((Result.Error<?>) result).error);
            }

        });

    }

}
