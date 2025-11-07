package trabajo.aplicacionSaludable.Dominio;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter

@Entity
@Table(name = "ejecicio")
public class Ejercicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idEjercicio")
    private Long idEjercicio;

    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false)
    private String explicacion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Musculo musculoEnfocado;


    @ManyToMany(mappedBy = "ejercicios")
    private Set<DiaEnRutina> rutinasDelEjercicio = new HashSet<>();

    @OneToMany(mappedBy = "ejercicio", cascade = CascadeType.ALL,  orphanRemoval = true)
    private List<Serie> series = new ArrayList<>();

    public Ejercicio() {

    }

    public Ejercicio(String nombre, String explicacion, Musculo musculoEnfocado) {
        this.nombre = nombre;
        this.explicacion = explicacion;
        this.musculoEnfocado = musculoEnfocado;
    }

}
