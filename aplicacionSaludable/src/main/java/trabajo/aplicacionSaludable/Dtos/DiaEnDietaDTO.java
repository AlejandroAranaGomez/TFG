package trabajo.aplicacionSaludable.Dtos;

import lombok.Getter;
import lombok.Setter;
import trabajo.aplicacionSaludable.Dominio.DiaDeLaSemana;

@Getter
@Setter

public class DiaEnDietaDTO {

    private Long idDiaEnDieta;

    private String nombre;

    private float caloriasTotales;
    private float carbohidratos;
    private float proteinas;
    private float grasas;
    private DiaDeLaSemana diaDeLaSemana;

    public DiaEnDietaDTO() {

    }

    public DiaEnDietaDTO(Long idDiaEnDieta, String nombre, float caloriasTotales, float carbohidratos, float proteinas, float grasas, DiaDeLaSemana diaDeLaSemana) {
        this.idDiaEnDieta = idDiaEnDieta;
        this.nombre = nombre;
        this.caloriasTotales = caloriasTotales;
        this.carbohidratos = carbohidratos;
        this.proteinas = proteinas;
        this.grasas = grasas;
        this.diaDeLaSemana = diaDeLaSemana;
    }

}
