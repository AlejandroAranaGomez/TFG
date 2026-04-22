package trabajo.aplicacionSaludable.Dtos;

import lombok.Getter;
import lombok.Setter;
import trabajo.aplicacionSaludable.Dominio.RutinaCompleta;

import java.util.List;

@Setter
@Getter

public class RutinaCompletaDTO {

    private Long idRutinaCompleta;

    private String nombreRutinaCompleta;
    private String resumen;

    private List<DiaEnRutinaDTO> dias;

    public RutinaCompletaDTO() {

    }

    public RutinaCompletaDTO(Long idRutinaCompleta, String nombreRutinaCompleta, String resumen) {
        this.idRutinaCompleta = idRutinaCompleta;
        this.nombreRutinaCompleta = nombreRutinaCompleta;
        this.resumen = resumen;
    }

}
