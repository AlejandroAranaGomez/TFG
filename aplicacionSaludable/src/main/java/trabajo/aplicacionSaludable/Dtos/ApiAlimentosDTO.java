package trabajo.aplicacionSaludable.Dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiAlimentosDTO {

    private String nombre;
    private float calorias;
    private float proteinas;
    private float carbohidratos;
    private float grasas;

}
