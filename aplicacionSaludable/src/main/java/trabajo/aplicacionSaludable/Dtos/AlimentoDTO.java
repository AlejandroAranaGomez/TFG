package trabajo.aplicacionSaludable.Dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class AlimentoDTO {

    private String nombre;

    private float calorias;
    private float proteinas;
    private float carbohidratos;
    private float grasas;

    public AlimentoDTO() {

    }

    public AlimentoDTO(String nombre, float calorias,  float proteinas, float carbohidratos, float grasas) {
        this.nombre = nombre;
        this.calorias = calorias;
        this.proteinas = proteinas;
        this.carbohidratos = carbohidratos;
        this.grasas = grasas;
    }

}
