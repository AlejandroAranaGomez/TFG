package trabajo.aplicacionSaludable.Dominio;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter

@Entity
@Table(name = "dietaCompleta")
public class DietaCompleta {

    // Clave Primaria BBDD
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idDietaCompleta")
    private Long idDietaCompleta;

    @Column(nullable = false)
    private String nombre;

    private String descripcion;
    @Column(nullable = false)
    private float caloriasTotales;
    @Column(nullable = false)
    private float proteinas;
    @Column(nullable = false)
    private float carbohidratos;
    @Column(nullable = false)
    private float grasas;
    @Column(nullable = false)
    private boolean activa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUsuario", nullable = false)
    private Usuario usuario;

    @OneToMany(mappedBy = "dietaCompleta", cascade = CascadeType.ALL,  orphanRemoval = true)
    private List<DiaEnDieta> diasDeDieta = new ArrayList<>();

    // Necesario para JPA
    public DietaCompleta() {

    }

    public DietaCompleta(String nombre, String descripcion, float caloriasTotales, float proteinas, float carbohidratos, float grasas, Usuario usuario, boolean activa) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.caloriasTotales = caloriasTotales;
        this.proteinas = proteinas;
        this.carbohidratos = carbohidratos;
        this.grasas = grasas;
        this.usuario = usuario;
        this.activa = activa;
    }

}
