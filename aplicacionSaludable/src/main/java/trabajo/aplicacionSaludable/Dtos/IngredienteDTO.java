package trabajo.aplicacionSaludable.Dtos;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class IngredienteDTO {

    private Long idIngrediente;

    private float cantidadEnGramos;
    private String nombre;
    private float caloriasTotales;
    private float carbohidratos;
    private float proteinas;
    private float grasas;

    public IngredienteDTO() {

    }

    public IngredienteDTO(Long idIngrediente, float cantidadEnGramos, String nombre,  float carbohidratos, float grasas,  float caloriasTotales, float proteinas) {
        this.idIngrediente = idIngrediente;
        this.cantidadEnGramos = cantidadEnGramos;
        this.nombre = nombre;
        this.carbohidratos = carbohidratos;
        this.grasas = grasas;
        this.caloriasTotales = caloriasTotales;
        this.proteinas = proteinas;
    }
}
