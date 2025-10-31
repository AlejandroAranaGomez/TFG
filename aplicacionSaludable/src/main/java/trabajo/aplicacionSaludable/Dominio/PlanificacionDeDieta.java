package trabajo.aplicacionSaludable.Dominio;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter

public class PlanificacionDeDieta {

    private Long idPlanificacionDeDieta;

    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    private DietaCompleta dietaCompleta;
    private Usuario usuario;

    public PlanificacionDeDieta() {

    }

    public PlanificacionDeDieta(LocalDate fechaInicio, LocalDate fechaFin, DietaCompleta dietaCompleta, Usuario usuario) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.dietaCompleta = dietaCompleta;
        this.usuario = usuario;
    }

}
