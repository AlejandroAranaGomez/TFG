package trabajo.aplicacionSaludable.Servicios;

import org.springframework.stereotype.Service;
import trabajo.aplicacionSaludable.Assemblers.DiaEnRutinaAssembler;
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
    private final DiaEnRutinaAssembler diaEnRutinaAssembler;

    public DiaEnRutinaService(DiaEnRutinaRepository diaEnRutinaRepository,
                              RutinaCompletaRepository rutinaCompletaRepository,
                              DiaEnRutinaAssembler diaEnRutinaAssembler) {
        this.diaEnRutinaRepository = diaEnRutinaRepository;
        this.rutinaCompletaRepository = rutinaCompletaRepository;
        this.diaEnRutinaAssembler = diaEnRutinaAssembler;
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
        return diaEnRutinaAssembler.entidadADTO(guardado);
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
