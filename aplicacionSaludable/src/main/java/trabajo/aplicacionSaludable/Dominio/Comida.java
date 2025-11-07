package trabajo.aplicacionSaludable.Dominio;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.service.spi.InjectService;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter

@Entity
@Table(name = "comida")
public class Comida {

    // Clave Primaria BBDD
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idComida")
    private Long idComida;

    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false)
    private float caloriasTotales;
    @Column(nullable = false)
    private float proteinas;
    @Column(nullable = false)
    private float carbohidratos;
    @Column(nullable = false)
    private float grasas;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idDiaEnDieta",  nullable = false)
    private DiaEnDieta diaEnDieta;

    @OneToMany(mappedBy = "comida", cascade = CascadeType.ALL,  orphanRemoval = true)
    private Set<Ingrediente> ingredientes = new HashSet<>();

    public Comida() {

    }

    public Comida(String nombre,  float caloriasTotales, float proteinas, float carbohidratos, float grasas,  DiaEnDieta diaEnDieta) {
        this.nombre = nombre;
        this.caloriasTotales = caloriasTotales;
        this.proteinas = proteinas;
        this.carbohidratos = carbohidratos;
        this.grasas = grasas;
        this.diaEnDieta = diaEnDieta;
    }

}
