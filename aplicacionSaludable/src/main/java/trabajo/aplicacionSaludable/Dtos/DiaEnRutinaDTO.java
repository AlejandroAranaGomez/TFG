package trabajo.aplicacionSaludable.Dtos;

import lombok.Getter;
import lombok.Setter;
import trabajo.aplicacionSaludable.Dominio.DiaDeLaSemana;

@Getter
@Setter
public class DiaEnRutinaDTO {

    private Long idDiaEnRutina;
    private String nombre;
    private DiaDeLaSemana diaDeLaSemana;

    public DiaEnRutinaDTO() {

    }

    public DiaEnRutinaDTO(Long idDiaEnRutina, String nombre, DiaDeLaSemana diaDeLaSemana) {
        this.idDiaEnRutina = idDiaEnRutina;
        this.nombre = nombre;
        this.diaDeLaSemana = diaDeLaSemana;
    }
}
