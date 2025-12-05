package com.trabajo.fitnessapp.presentacion.menu;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.trabajo.fitnessapp.datos.dto.AlimentoDTO;
import com.trabajo.fitnessapp.datos.dto.ApiAlimentosDTO;
import com.trabajo.fitnessapp.datos.repository.AlimentosRepository;
import com.trabajo.fitnessapp.datos.repository.ApiAlimentosRepository;

import java.util.List;

public class AlimentosViewModel extends ViewModel {

    private final AlimentosRepository alimentosRepository;
    private final ApiAlimentosRepository foodFactsRepository;

    public AlimentosViewModel() {
        this.alimentosRepository = new AlimentosRepository();
        this.foodFactsRepository = new ApiAlimentosRepository();
    }

    public LiveData<List<AlimentoDTO>> obtenerLosAlimentos(Long usuarioId) {
        return alimentosRepository.obtenerAlimentosUsuario(usuarioId);
    }

    public LiveData<Boolean> actualizarAlimento(Long idAlimento, Long idUsuario, AlimentoDTO alimentoDTO) {
        return alimentosRepository.actualizarAlimento(idAlimento, idUsuario, alimentoDTO);
    }

    public LiveData<Boolean> borrarAlimento(Long idAlimento, Long idUsuario) {
        return alimentosRepository.borrarAlimento(idAlimento, idUsuario);
    }

    public LiveData<List<ApiAlimentosDTO>> buscarAlimentosGlobales(String nombre) {
        return foodFactsRepository.buscarAlimentos(nombre);
    }

    public LiveData<Boolean> crearAlimento(Long idUsuario, AlimentoDTO alimentoDTO) {
        return alimentosRepository.crearAlimento(idUsuario, alimentoDTO);
    }
}
