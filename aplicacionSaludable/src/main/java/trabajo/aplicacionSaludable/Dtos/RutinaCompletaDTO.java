package trabajo.aplicacionSaludable.Dtos;

import lombok.Getter;
import lombok.Setter;
import trabajo.aplicacionSaludable.Dominio.RutinaCompleta;

@Setter
@Getter

public class RutinaCompletaDTO {

    private Long idRutinaCompleta;

    private String nombreRutinaCompleta;
    private String resumen;

    public RutinaCompletaDTO() {

    }

    public RutinaCompletaDTO(Long idRutinaCompleta, String nombreRutinaCompleta, String resumen) {
        this.idRutinaCompleta = idRutinaCompleta;
        this.nombreRutinaCompleta = nombreRutinaCompleta;
        this.resumen = resumen;
    }

}
