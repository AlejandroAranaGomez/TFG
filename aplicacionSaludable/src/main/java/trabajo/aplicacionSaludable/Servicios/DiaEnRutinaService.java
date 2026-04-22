package trabajo.aplicacionSaludable.Servicios;

import org.springframework.stereotype.Service;
import trabajo.aplicacionSaludable.Dominio.DiaDeLaSemana;
import trabajo.aplicacionSaludable.Dominio.DiaEnRutina;
import trabajo.aplicacionSaludable.Dominio.RutinaCompleta;
import trabajo.aplicacionSaludable.Dtos.DiaEnRutinaDTO;
import trabajo.aplicacionSaludable.Repositorios.DiaEnRutinaRepository;
import trabajo.aplicacionSaludable.Repositorios.RutinaCompletaRepository;

import java.util.Optional;

@Service
public class DiaEnRutinaService {

    private final RutinaCompletaRepository rutinaCompletaRepository;
    private final DiaEnRutinaRepository diaEnRutinaRepository;

    public DiaEnRutinaService(DiaEnRutinaRepository diaEnRutinaRepository, RutinaCompletaRepository rutinaCompletaRepository) {
        this.diaEnRutinaRepository = diaEnRutinaRepository;
        this.rutinaCompletaRepository = rutinaCompletaRepository;
    }

    private DiaEnRutinaDTO EntidadaDTO(DiaEnRutina diaEnRutina) {
        DiaEnRutinaDTO diaEnRutinaDTO = new DiaEnRutinaDTO();
        diaEnRutinaDTO.setIdDiaEnRutina(diaEnRutina.getIdDiaEnRutina());
        diaEnRutinaDTO.setNombre(diaEnRutina.getNombre());
        diaEnRutinaDTO.setDiaDeLaSemana(diaEnRutina.getDiaDeLaSemana());
        return diaEnRutinaDTO;
    }

    public DiaEnRutinaDTO guardarDiaEnRutina(DiaEnRutinaDTO diaEnRutinaDTO, Long idRutinaCompleta, DiaDeLaSemana diaDeLaSemana) {
        RutinaCompleta rutinaCompleta = rutinaCompletaRepository.findById(idRutinaCompleta).orElse(null);

        if (rutinaCompleta == null) {
            return null;
        }
        Optional<DiaEnRutina> diaEnRutina = diaEnRutinaRepository.findByDiaDeLaSemanaAndRutinaCompleta(diaDeLaSemana, rutinaCompleta);
        DiaEnRutina dia;
        if (diaEnRutina.isPresent()) {
            dia = diaEnRutina.get();
        } else {
            dia = new DiaEnRutina();
            dia.setRutinaCompleta(rutinaCompleta);
            dia.setDiaDeLaSemana(diaDeLaSemana);
        }

        dia.setNombre(diaEnRutinaDTO.getNombre());
        DiaEnRutina guardado = diaEnRutinaRepository.save(dia);
        return EntidadaDTO(guardado);
    }

    public boolean borrarDia(DiaDeLaSemana diaDeLaSemana, Long idRutinaCompleta) {
        RutinaCompleta rutinaCompleta = rutinaCompletaRepository.findById(idRutinaCompleta).orElse(null);

        if (rutinaCompleta == null) {
            return false;
        }

        Optional<DiaEnRutina> diaEnRutina = diaEnRutinaRepository.findByDiaDeLaSemanaAndRutinaCompleta(diaDeLaSemana, rutinaCompleta);

        if (diaEnRutina.isEmpty()) {
            return false;
        }

        DiaEnRutina dia = diaEnRutina.get();
        diaEnRutinaRepository.delete(dia);
        return true;
    }
}
