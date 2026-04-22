package trabajo.aplicacionSaludable.Dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ComidaSeguimientoDTO {

    private Long idComida;

    private String nombre;
    private float caloriasTotales;
    private float carbohidratos;
    private float proteinas;
    private float grasas;

    private boolean registrada;

    private List<IngredienteDTO> ingredientes;

    public ComidaSeguimientoDTO() {

    }

    public ComidaSeguimientoDTO(Long idComida, String nombre,  float carbohidratos, float grasas,  float caloriasTotales, float proteinas) {
        this.idComida = idComida;
        this.nombre = nombre;
        this.carbohidratos = carbohidratos;
        this.grasas = grasas;
        this.caloriasTotales = caloriasTotales;
        this.proteinas = proteinas;
    }
}
