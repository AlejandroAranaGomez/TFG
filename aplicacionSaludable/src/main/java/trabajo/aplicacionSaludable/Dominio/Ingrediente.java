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
    private String nombre;
    @Column(nullable = false)
    private float cantidadEnGramos;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idAlimento", nullable = false)
    private Alimento alimento;

    public Ingrediente(String nombre, float cantidadEnGramos, float caloriasTotales, float proteinas, float carbohidratos, float grasas, Comida comida, Alimento alimento) {
        this.nombre = nombre;
        this.cantidadEnGramos = cantidadEnGramos;
        this.caloriasTotales = caloriasTotales;
        this.proteinas = proteinas;
        this.carbohidratos = carbohidratos;
        this.grasas = grasas;
        this.comida = comida;
        this.alimento = alimento;
    }

}
