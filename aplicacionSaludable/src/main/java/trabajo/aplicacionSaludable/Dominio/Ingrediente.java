package trabajo.aplicacionSaludable.Dominio;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Entity
@Table(name = "ingrediente")
public class Ingrediente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idIngrediente")
    private Long idIngrediente;

    @Column(nullable = false)
    private float cantidadEnGramos;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idComida",  nullable = false)
    private Comida comida;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idAlimento", nullable = false)
    private Alimento alimento;

    public Ingrediente() {

    }

    public Ingrediente(float cantidadEnGramos, Comida comida,  Alimento alimento) {
        this.cantidadEnGramos = cantidadEnGramos;
        this.comida = comida;
        this.alimento = alimento;
    }

}
