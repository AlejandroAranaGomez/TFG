package trabajo.aplicacionSaludable.Servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import trabajo.aplicacionSaludable.Dominio.DiaEnDieta;
import trabajo.aplicacionSaludable.Dominio.DiaEnRutina;
import trabajo.aplicacionSaludable.Dominio.RutinaCompleta;
import trabajo.aplicacionSaludable.Dtos.DiaEnRutinaDTO;
import trabajo.aplicacionSaludable.Excepciones.ExcepcionesDiaEnRutina.DiaPerteneceAOtraRutinaException;
import trabajo.aplicacionSaludable.Excepciones.ExcepcionesDiaEnRutina.DiaRutinaYaCreadoException;
import trabajo.aplicacionSaludable.Repositorios.DiaEnRutinaRepository;
import trabajo.aplicacionSaludable.Repositorios.RutinaCompletaRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DiaEnRutinaService {

    private final RutinaCompletaRepository rutinaCompletaRepository;
    private final DiaEnRutinaRepository diaEnRutinaRepository;

    @Autowired
    public DiaEnRutinaService(DiaEnRutinaRepository diaEnRutinaRepository, RutinaCompletaRepository rutinaCompletaRepository) {
        this.diaEnRutinaRepository = diaEnRutinaRepository;
        this.rutinaCompletaRepository = rutinaCompletaRepository;
    }

    private DiaEnRutina DTOaEntidad(DiaEnRutinaDTO diaEnRutinaDTO, RutinaCompleta rutinaCompleta) {
        DiaEnRutina diaEnRutina = new DiaEnRutina();
        diaEnRutina.setNombre(diaEnRutinaDTO.getNombre());
        diaEnRutina.setDiaDeLaSemana(diaEnRutinaDTO.getDiaDeLaSemana());
        diaEnRutina.setRutinaCompleta(rutinaCompleta);
        return diaEnRutina;
    }

    private DiaEnRutinaDTO EntidadaDTO(DiaEnRutina diaEnRutina) {
        DiaEnRutinaDTO diaEnRutinaDTO = new DiaEnRutinaDTO();
        diaEnRutinaDTO.setIdDiaEnRutina(diaEnRutina.getIdDiaEnRutina());
        diaEnRutinaDTO.setNombre(diaEnRutina.getNombre());
        diaEnRutinaDTO.setDiaDeLaSemana(diaEnRutina.getDiaDeLaSemana());
        return diaEnRutinaDTO;
    }

    public List<DiaEnRutinaDTO> listaDiaEnRutina(Long idRutinaCompleta) {
        RutinaCompleta rutinaCompleta = rutinaCompletaRepository.findById(idRutinaCompleta).orElse(null);

        if (rutinaCompleta == null) {
            return null;
        }

        List<DiaEnRutina> diasEnRutina = diaEnRutinaRepository.findByRutinaCompleta(rutinaCompleta);


        List<DiaEnRutina> diasOrdenados = diasEnRutina.stream()
                .sorted(Comparator.comparing(DiaEnRutina::getDiaDeLaSemana))
                .collect(Collectors.toList());

        return diasOrdenados.stream().map(this::EntidadaDTO).collect(Collectors.toList());
    }

    public DiaEnRutinaDTO crearDiaEnRutina(DiaEnRutinaDTO diaEnRutinaDTO, Long idRutinaCompleta) {
        RutinaCompleta rutinaCompleta = rutinaCompletaRepository.findById(idRutinaCompleta).orElse(null);

        if (rutinaCompleta == null) {
            return null;
        }

        if (diaEnRutinaRepository.findByDiaDeLaSemanaAndRutinaCompleta(diaEnRutinaDTO.getDiaDeLaSemana(), rutinaCompleta).isPresent()) {
            throw new DiaRutinaYaCreadoException();
        }

        DiaEnRutina nuevoDiaEnRutina = DTOaEntidad(diaEnRutinaDTO, rutinaCompleta);
        DiaEnRutina diaGuardado = diaEnRutinaRepository.save(nuevoDiaEnRutina);

        return EntidadaDTO(diaGuardado);
    }

    public DiaEnRutinaDTO editarDiaEnRutina(DiaEnRutinaDTO diaEnRutinaDTO, Long idRutinaCompleta, Long idDiaEnRutina) {
        DiaEnRutina diaExiste = diaEnRutinaRepository.findById(idDiaEnRutina).orElse(null);

        if (diaExiste == null) {
            return null;
        }

        if (!diaExiste.getRutinaCompleta().getIdRutinaCompleta().equals(idRutinaCompleta)) {
            throw new DiaPerteneceAOtraRutinaException();
        }

        diaExiste.setNombre(diaEnRutinaDTO.getNombre());

        DiaEnRutina actualizado = diaEnRutinaRepository.save(diaExiste);
        return  EntidadaDTO(actualizado);
    }

    public boolean borrarDia(Long idDiaEnRutina, Long idRutinaCompleta) {
        DiaEnRutina diaEnRutina = diaEnRutinaRepository.findById(idDiaEnRutina).orElse(null);

        if (diaEnRutina == null) {
            return false;
        }

        if (!diaEnRutina.getRutinaCompleta().getIdRutinaCompleta().equals(idRutinaCompleta)) {
            throw new DiaPerteneceAOtraRutinaException();
        }

        diaEnRutinaRepository.deleteById(idDiaEnRutina);
        return true;
    }
}
