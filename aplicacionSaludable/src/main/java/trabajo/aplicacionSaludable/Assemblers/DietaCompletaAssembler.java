package trabajo.aplicacionSaludable.Assemblers;

import org.springframework.stereotype.Component;
import trabajo.aplicacionSaludable.Dominio.DiaEnDieta;
import trabajo.aplicacionSaludable.Dominio.DietaCompleta;
import trabajo.aplicacionSaludable.Dominio.Usuario;
import trabajo.aplicacionSaludable.Dtos.DiaEnDietaDTO;
import trabajo.aplicacionSaludable.Dtos.DietaCompletaDTO;

@Component
public class DietaCompletaAssembler {

    public DietaCompleta dtoAEntidad(DietaCompletaDTO dto, Usuario usuario) {
        DietaCompleta dieta = new DietaCompleta();

        dieta.setNombre(dto.getNombre());
        dieta.setDescripcion(dto.getDescripcion());
        dieta.setCaloriasTotales(dto.getCaloriasTotales());
        dieta.setProteinas(dto.getProteinas());
        dieta.setCarbohidratos(dto.getCarbohidratos());
        dieta.setGrasas(dto.getGrasas());
        dieta.setActiva(dto.isActiva());
        dieta.setUsuario(usuario);

        return dieta;
    }

    public DietaCompletaDTO entidadADTO(DietaCompleta dieta) {
        DietaCompletaDTO dto = new DietaCompletaDTO();

        dto.setIdDietaCompleta(dieta.getIdDietaCompleta());
        dto.setNombre(dieta.getNombre());
        dto.setDescripcion(dieta.getDescripcion());
        dto.setCaloriasTotales(dieta.getCaloriasTotales());
        dto.setProteinas(dieta.getProteinas());
        dto.setCarbohidratos(dieta.getCarbohidratos());
        dto.setGrasas(dieta.getGrasas());
        dto.setActiva(dieta.isActiva());

        return dto;
    }

    public DiaEnDietaDTO diaADTO(DiaEnDieta dia) {
        DiaEnDietaDTO dto = new DiaEnDietaDTO();

        dto.setIdDiaEnDieta(dia.getIdDiaEnDieta());
        dto.setNombre(dia.getNombre());
        dto.setDiaDeLaSemana(dia.getDiaDeLaSemana());
        dto.setCaloriasTotales(dia.getCaloriasTotales());
        dto.setProteinas(dia.getProteinas());
        dto.setCarbohidratos(dia.getCarbohidratos());
        dto.setGrasas(dia.getGrasas());

        return dto;
    }

}
