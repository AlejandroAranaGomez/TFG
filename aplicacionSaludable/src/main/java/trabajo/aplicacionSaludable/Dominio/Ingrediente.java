package trabajo.aplicacionSaludable.Dominio;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter

@NoArgsConstructor

@Entity
@Table(name = "ingrediente")
public class Ingrediente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idIngrediente;

    @Column(nullable = false)
    private float cantidadEnGramos;
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
    @JoinColumn(name = "idComida",  nullable = false)
    private Comida comida;

    public Ingrediente(float cantidadEnGramos, String nombre, float caloriasTotales, float proteinas, float carbohidratos, float grasas, Comida comida) {
        this.cantidadEnGramos = cantidadEnGramos;
        this.nombre = nombre;
        this.caloriasTotales = caloriasTotales;
        this.proteinas = proteinas;
        this.carbohidratos = carbohidratos;
        this.grasas = grasas;
        this.comida = comida;
    }

}
