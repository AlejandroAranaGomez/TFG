package trabajo.aplicacionSaludable.Servicios;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import trabajo.aplicacionSaludable.Dominio.DiaEnRutina;
import trabajo.aplicacionSaludable.Dominio.Ejercicio;
import trabajo.aplicacionSaludable.Dtos.EjercicioDTO;
import trabajo.aplicacionSaludable.Repositorios.DiaEnRutinaRepository;
import trabajo.aplicacionSaludable.Repositorios.EjercicioRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EjercicioService {

    private final EjercicioRepository ejercicioRepository;
    private final DiaEnRutinaRepository diaEnRutinaRepository;

    public EjercicioService(DiaEnRutinaRepository diaEnRutinaRepository, EjercicioRepository ejercicioRepository) {
        this.diaEnRutinaRepository = diaEnRutinaRepository;
        this.ejercicioRepository = ejercicioRepository;
    }

    private EjercicioDTO EntidadaDTO(Ejercicio ejercicio) {
        EjercicioDTO dto = new EjercicioDTO();
        dto.setIdEjercicio(ejercicio.getIdEjercicio());
        dto.setNombre(ejercicio.getNombre());
        dto.setMusculoEnfocado(ejercicio.getMusculoEnfocado());
        dto.setIdApi(ejercicio.getIdApi());
        return dto;
    }


    @Transactional(readOnly = true)
    public List<EjercicioDTO> listaEjercicios(Long idDiaEnRutina) {
        DiaEnRutina dia = diaEnRutinaRepository.findById(idDiaEnRutina).orElse(null);

        if (dia == null) {
            return null;
        }

        return dia.getEjercicios().stream().map(this::EntidadaDTO).collect(Collectors.toList());
    }

    @Transactional
    public void anhadirEjercicio(Long idDiaEnRutina, EjercicioDTO ejercicioDTO) {
        DiaEnRutina dia = diaEnRutinaRepository.findById(idDiaEnRutina).orElse(null);

        if (dia == null) {
            return;
        }

        Optional<Ejercicio> ejercicioExiste = ejercicioRepository.findByIdApi(ejercicioDTO.getIdApi());

        Ejercicio ejercicio;
        if (ejercicioExiste.isPresent()) {
            ejercicio = ejercicioExiste.get();
        } else {
            ejercicio = new Ejercicio();
            ejercicio.setNombre(ejercicioDTO.getNombre());
            ejercicio.setIdApi(ejercicioDTO.getIdApi());
            ejercicio.setMusculoEnfocado(ejercicioDTO.getMusculoEnfocado());
            ejercicio = ejercicioRepository.save(ejercicio);
        }
        dia.getEjercicios().add(ejercicio);

    }

    @Transactional
    public boolean borrarEjercicio(Long idDiaEnRutina, Long idEjercicio) {
        DiaEnRutina dia = diaEnRutinaRepository.findById(idDiaEnRutina).orElse(null);

        Ejercicio ejercicio = ejercicioRepository.findById(idEjercicio).orElse(null);

        if (dia == null || ejercicio == null) {
            return false;
        }

        dia.getEjercicios().remove(ejercicio);
        return true;
    }

}
