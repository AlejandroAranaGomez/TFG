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

    @OneToMany(mappedBy = "ejercicio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EjercicioEnDiaRutina> ejerciciosUsuarios = new ArrayList<>();

    public Ejercicio(String nombre, Musculo musculoEnfocado) {
        this.nombre = nombre;
        this.musculoEnfocado = musculoEnfocado;
    }

}
