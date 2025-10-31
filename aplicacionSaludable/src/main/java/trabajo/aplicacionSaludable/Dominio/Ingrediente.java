package trabajo.aplicacionSaludable.Dominio;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Ingrediente {

    private Long idIngrediente;

    private float cantidadEnGramos;

    private Comida comida;
    private Alimento alimento;

    public Ingrediente() {

    }

    public Ingrediente(float cantidadEnGramos, Comida comida,  Alimento alimento) {
        this.cantidadEnGramos = cantidadEnGramos;
        this.comida = comida;
        this.alimento = alimento;
    }

}
