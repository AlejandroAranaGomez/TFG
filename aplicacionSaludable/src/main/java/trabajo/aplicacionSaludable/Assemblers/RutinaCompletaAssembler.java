package trabajo.aplicacionSaludable.Assemblers;

import org.springframework.stereotype.Component;
import trabajo.aplicacionSaludable.Dominio.DiaEnRutina;
import trabajo.aplicacionSaludable.Dominio.RutinaCompleta;
import trabajo.aplicacionSaludable.Dominio.Usuario;
import trabajo.aplicacionSaludable.Dtos.DiaEnRutinaDTO;
import trabajo.aplicacionSaludable.Dtos.RutinaCompletaDTO;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RutinaCompletaAssembler {

    public RutinaCompleta dtoAEntidad(RutinaCompletaDTO dto, Usuario usuario) {
        RutinaCompleta rutina = new RutinaCompleta();

        rutina.setIdRutinaCompleta(dto.getIdRutinaCompleta());
        rutina.setNombreRutinaCompleta(dto.getNombreRutinaCompleta());
        rutina.setResumen(dto.getResumen());
        rutina.setUsuario(usuario);

        return rutina;
    }

    public RutinaCompletaDTO entidadADTO(RutinaCompleta rutina) {
        RutinaCompletaDTO dto = new RutinaCompletaDTO();

        dto.setIdRutinaCompleta(rutina.getIdRutinaCompleta());
        dto.setNombreRutinaCompleta(rutina.getNombreRutinaCompleta());
        dto.setResumen(rutina.getResumen());

        return dto;
    }

    public DiaEnRutinaDTO diaEntidadADTO(DiaEnRutina dia) {
        DiaEnRutinaDTO dto = new DiaEnRutinaDTO();

        dto.setIdDiaEnRutina(dia.getIdDiaEnRutina());
        dto.setNombre(dia.getNombre());
        dto.setDiaDeLaSemana(dia.getDiaDeLaSemana());

        return dto;
    }

    public RutinaCompletaDTO entidadADTOConDias(RutinaCompleta rutina) {
        RutinaCompletaDTO dto = entidadADTO(rutina);

        List<DiaEnRutinaDTO> dias = rutina.getDiaEnRutinas()
                .stream()
                .map(this::diaEntidadADTO)
                .collect(Collectors.toList());

        dto.setDias(dias);

        return dto;
    }
}
