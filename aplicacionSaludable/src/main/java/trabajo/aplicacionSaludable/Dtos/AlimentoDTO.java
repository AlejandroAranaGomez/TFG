package trabajo.aplicacionSaludable.Dtos;

import lombok.Getter;
import lombok.Setter;

// Estas clases se utilizan para transportar datos que nosotros querramos entre el backend y el frontend.

@Getter
@Setter

public class AlimentoDTO {

    private Long idAlimento;

    private String nombre;

    private float calorias;
    private float proteinas;
    private float carbohidratos;
    private float grasas;
    private String idApi;

    public AlimentoDTO() {

    }

    public AlimentoDTO(Long idAlimento, String nombre, float calorias,  float proteinas, float carbohidratos, float grasas, String idApi) {
        this.idAlimento = idAlimento;
        this.nombre = nombre;
        this.calorias = calorias;
        this.proteinas = proteinas;
        this.carbohidratos = carbohidratos;
        this.grasas = grasas;
        this.idApi = idApi;
    }

}
