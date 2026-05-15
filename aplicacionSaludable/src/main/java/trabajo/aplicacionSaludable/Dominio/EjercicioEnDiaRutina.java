package trabajo.aplicacionSaludable.Dominio;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter

@NoArgsConstructor

@Entity
@Table(name = "ejercicioEnDiaRutina")
public class EjercicioEnDiaRutina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEjercicioEnDiaRutina;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idDiaEnRutina", nullable = false)
    private DiaEnRutina diaEnRutina;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idEjercicio", nullable = false)
    private Ejercicio ejercicio;

    @OneToMany(mappedBy = "ejercicioEnDiaRutina", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Serie> series = new ArrayList<>();


}
