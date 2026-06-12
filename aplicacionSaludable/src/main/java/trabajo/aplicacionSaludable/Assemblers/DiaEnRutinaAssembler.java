package trabajo.aplicacionSaludable.Assemblers;

import org.springframework.stereotype.Component;
import trabajo.aplicacionSaludable.Dominio.DiaEnRutina;
import trabajo.aplicacionSaludable.Dtos.DiaEnRutinaDTO;

@Component
public class DiaEnRutinaAssembler {

    public DiaEnRutinaDTO entidadADTO(DiaEnRutina diaEnRutina) {
        DiaEnRutinaDTO dto = new DiaEnRutinaDTO();

        dto.setIdDiaEnRutina(diaEnRutina.getIdDiaEnRutina());
        dto.setNombre(diaEnRutina.getNombre());
        dto.setDiaDeLaSemana(diaEnRutina.getDiaDeLaSemana());

        return dto;
    }

}
