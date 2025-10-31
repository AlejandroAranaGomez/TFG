package trabajo.aplicacionSaludable.Dominio;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter

public class Alimento {

    // Clave Primaria BBDD
    private Long idAlimento;

    private String nombre;
    private float calorias;
    private float proteinas;
    private float carbohidratos;
    private float grasas;

    private Set<Ingrediente> ingredientesEnComidas = new HashSet<>();

    public Alimento() {

    }

    public Alimento(String nombre, float calorias,float proteinas, float carbohidratos, float grasas) {
        this.nombre = nombre;
        this.calorias = calorias;
        this.proteinas = proteinas;
        this.carbohidratos = carbohidratos;
        this.grasas = grasas;
    }
    
}
