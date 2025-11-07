package trabajo.aplicacionSaludable.Dominio;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Entity
@Table(name = "serie")
public class Serie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idSerie")
    private Long idSerie;

    private String serieAnterior;

    private int repeticiones;

    private float peso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idEjercicio",  nullable = false)
    private Ejercicio ejercicio;

    public Serie() {

    }

    public Serie(String serieAnterior, int repeticiones, float peso, Ejercicio ejercicio) {
        this.serieAnterior = serieAnterior;
        this.repeticiones = repeticiones;
        this.peso = peso;
        this.ejercicio = ejercicio;
    }

}
