package trabajo.aplicacionSaludable.Dominio;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter

@NoArgsConstructor

@Entity
@Table(name = "ejercicio")
public class Ejercicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEjercicio;

    @Column(nullable = false)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Musculo musculoEnfocado;

    @Column(unique = true)
    private String idApi;

    @ManyToMany(mappedBy = "ejercicios")
    private Set<DiaEnRutina> rutinasDelEjercicio = new HashSet<>();

    @OneToMany(mappedBy = "ejercicio", cascade = CascadeType.ALL,  orphanRemoval = true)
    private List<Serie> series = new ArrayList<>();

    public Ejercicio(String nombre, Musculo musculoEnfocado) {
        this.nombre = nombre;
        this.musculoEnfocado = musculoEnfocado;
    }

}
