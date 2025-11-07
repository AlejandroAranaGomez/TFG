package trabajo.aplicacionSaludable.Dominio;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter

@Entity
@Table(name = "planificacionDeDieta")
public class PlanificacionDeDieta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPlanificacionDeDieta")
    private Long idPlanificacionDeDieta;

    @Column(nullable = false)
    private LocalDate fechaInicio;
    @Column(nullable = false)
    private LocalDate fechaFin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idDietaCompleta", nullable = false)
    private DietaCompleta dietaCompleta;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUsuario", nullable = false)
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
