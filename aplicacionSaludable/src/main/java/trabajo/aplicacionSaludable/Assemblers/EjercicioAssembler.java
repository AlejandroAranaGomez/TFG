package trabajo.aplicacionSaludable.Assemblers;

import org.springframework.stereotype.Component;
import trabajo.aplicacionSaludable.Dominio.Ejercicio;
import trabajo.aplicacionSaludable.Dominio.EjercicioEnDiaRutina;
import trabajo.aplicacionSaludable.Dtos.EjercicioDTO;

@Component
public class EjercicioAssembler {

    public EjercicioDTO entidadADTO(EjercicioEnDiaRutina ejercicioEnDiaRutina) {
        EjercicioDTO dto = new EjercicioDTO();

        dto.setIdEjercicioEnDiaRutina(ejercicioEnDiaRutina.getIdEjercicioEnDiaRutina());
        dto.setIdEjercicio(ejercicioEnDiaRutina.getEjercicio().getIdEjercicio());
        dto.setNombre(ejercicioEnDiaRutina.getEjercicio().getNombre());
        dto.setMusculoEnfocado(ejercicioEnDiaRutina.getEjercicio().getMusculoEnfocado());
        dto.setIdApi(ejercicioEnDiaRutina.getEjercicio().getIdApi());

        return dto;
    }

    public Ejercicio dtoAEntidad(EjercicioDTO dto) {
        Ejercicio ejercicio = new Ejercicio();

        ejercicio.setNombre(dto.getNombre());
        ejercicio.setIdApi(dto.getIdApi());
        ejercicio.setMusculoEnfocado(dto.getMusculoEnfocado());

        return ejercicio;
    }

}
