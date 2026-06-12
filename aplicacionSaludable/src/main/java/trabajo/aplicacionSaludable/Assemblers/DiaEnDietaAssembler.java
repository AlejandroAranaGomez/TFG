package trabajo.aplicacionSaludable.Assemblers;

import org.springframework.stereotype.Component;
import trabajo.aplicacionSaludable.Dominio.DiaEnDieta;
import trabajo.aplicacionSaludable.Dtos.DiaEnDietaDTO;

@Component
public class DiaEnDietaAssembler {

    public DiaEnDietaDTO entidadADTO(DiaEnDieta diaEnDieta) {
        DiaEnDietaDTO dto = new DiaEnDietaDTO();

        dto.setIdDiaEnDieta(diaEnDieta.getIdDiaEnDieta());
        dto.setNombre(diaEnDieta.getNombre());
        dto.setCaloriasTotales(diaEnDieta.getCaloriasTotales());
        dto.setProteinas(diaEnDieta.getProteinas());
        dto.setCarbohidratos(diaEnDieta.getCarbohidratos());
        dto.setGrasas(diaEnDieta.getGrasas());
        dto.setDiaDeLaSemana(diaEnDieta.getDiaDeLaSemana());

        return dto;
    }

}
