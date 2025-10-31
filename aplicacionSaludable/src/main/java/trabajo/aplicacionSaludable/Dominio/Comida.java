package trabajo.aplicacionSaludable.Dominio;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter

public class Comida {

    // Clave Primaria BBDD
    private Long idComida;

    private String nombre;
    private float caloriasTotales;
    private float proteinas;
    private float carbohidratos;
    private float grasas;

    private DiaEnDieta diaEnDieta;

    private Set<Ingrediente> ingredientes = new HashSet<>();

    public Comida() {

    }

    public Comida(String nombre,  float caloriasTotales, float proteinas, float carbohidratos, float grasas,  DiaEnDieta diaEnDieta) {
        this.nombre = nombre;
        this.caloriasTotales = caloriasTotales;
        this.proteinas = proteinas;
        this.carbohidratos = carbohidratos;
        this.grasas = grasas;
        this.diaEnDieta = diaEnDieta;
    }

}
