package trabajo.aplicacionSaludable.Dominio;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter

public class DietaCompleta {

    // Clave Primaria BBDD
    private Long idDieta;

    private String nombre;
    private String descripcion;
    private float caloriasTotales;
    private float proteinas;
    private float carbohidratos;
    private float grasas;

    private Usuario usuario;

    private List<DietaCompleta> diasDeDieta = new ArrayList<>();
    private List<PlanificacionDeDieta> planificacionesDeDieta = new ArrayList<>();

    // Necesario para JPA
    public DietaCompleta() {

    }

    public DietaCompleta(String nombre, String descripcion, float caloriasTotales, float proteinas, float carbohidratos, float grasas, Usuario usuario) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.caloriasTotales = caloriasTotales;
        this.proteinas = proteinas;
        this.carbohidratos = carbohidratos;
        this.grasas = grasas;
        this.usuario = usuario;
    }

}
