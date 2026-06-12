package trabajo.aplicacionSaludable.Dominio;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter

@NoArgsConstructor

@Entity
@Table(name = "serie")
public class Serie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSerie;

    private String serieAnterior;

    @Column(nullable = false)
    private int repeticiones;

    @Column(nullable = false)
    private float peso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idEjercicioEnDiaRutina", nullable = false)
    private EjercicioEnDiaRutina ejercicioEnDiaRutina;

    public Serie(String serieAnterior, int repeticiones, float peso, EjercicioEnDiaRutina ejercicioEnDiaRutina) {
        this.serieAnterior = serieAnterior;
        this.repeticiones = repeticiones;
        this.peso = peso;
        this.ejercicioEnDiaRutina = ejercicioEnDiaRutina;
    }

}
