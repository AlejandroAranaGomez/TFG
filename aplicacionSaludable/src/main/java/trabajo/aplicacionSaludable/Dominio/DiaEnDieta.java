package trabajo.aplicacionSaludable.Dominio;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter

public class DiaEnDieta {

    // Clave Primaria BBDD

    private Long idDiaEnDieta;

    private String nombre;
    private float caloriasTotales;
    private float proteinas;
    private float carbohidratos;
    private float grasas;

    private DiaDeLaSemana diaDeLaSemana;

    private DietaCompleta dietaCompleta;

    private List<Comida> comidas = new ArrayList<>();

    public DiaEnDieta() {

    }

    public DiaEnDieta(String nombre, float caloriasTotales, float proteinas, float carbohidratos, float grasas, DiaDeLaSemana diaDeLaSemana,  DietaCompleta dietaCompleta) {
        this.nombre = nombre;
        this.caloriasTotales = caloriasTotales;
        this.proteinas = proteinas;
        this.carbohidratos = carbohidratos;
        this.grasas = grasas;
        this.diaDeLaSemana = diaDeLaSemana;
        this.dietaCompleta = dietaCompleta;
    }

}
