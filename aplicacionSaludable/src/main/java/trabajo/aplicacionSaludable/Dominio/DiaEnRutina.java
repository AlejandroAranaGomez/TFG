package trabajo.aplicacionSaludable.Dominio;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter

@Entity
@Table(name = "diaEnRutina")
public class DiaEnRutina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idDiaEnRutina")
    private Long idDiaEnRutina;

    @Column(nullable = false)
    private String nombre;

    private String resumen;

    private float caloriasQuemadas;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DiaDeLaSemana diaDeLaSemana;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "idRutinaCompleta",  nullable = false)
    private RutinaCompleta rutinaCompleta;


    @ManyToMany
            @JoinTable(
            name = "diaRutinaEjercicio",
            joinColumns = @JoinColumn(name = "idDiaEnRutina"),
                    inverseJoinColumns = @JoinColumn(name = "idEjercicio")

    )
    private List<Ejercicio> ejercicios = new ArrayList<>();

    public DiaEnRutina() {

    }

    public DiaEnRutina(String nombre, String resumen, float caloriasQuemadas, DiaDeLaSemana diaDeLaSemana, RutinaCompleta rutinaCompleta) {
        this.nombre = nombre;
        this.resumen = resumen;
        this.caloriasQuemadas = caloriasQuemadas;
        this.diaDeLaSemana = diaDeLaSemana;
        this.rutinaCompleta = rutinaCompleta;
    }

}
