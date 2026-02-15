package trabajo.aplicacionSaludable.Dominio;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter

@NoArgsConstructor

@Entity
@Table(name = "diaEnRutina")
public class DiaEnRutina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDiaEnRutina;

    @Column(nullable = false)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DiaDeLaSemana diaDeLaSemana;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idRutinaCompleta",  nullable = false)
    private RutinaCompleta rutinaCompleta;


    @ManyToMany
            @JoinTable(
            name = "diaRutinaEjercicio",
            joinColumns = @JoinColumn(name = "idDiaEnRutina"),
                    inverseJoinColumns = @JoinColumn(name = "idEjercicio")

    )
    private List<Ejercicio> ejercicios = new ArrayList<>();

    public DiaEnRutina(String nombre, DiaDeLaSemana diaDeLaSemana, RutinaCompleta rutinaCompleta) {
        this.nombre = nombre;
        this.diaDeLaSemana = diaDeLaSemana;
        this.rutinaCompleta = rutinaCompleta;
    }

}
