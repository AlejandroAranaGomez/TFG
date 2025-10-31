package trabajo.aplicacionSaludable.Dominio;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter

public class Ejercicio {

    private Long idEjercicio;

    private String nombre;
    private String explicacion;
    private Musculo musculoEnfocado;


    private Set<DiaEnRutina> rutinasDelEjercicio = new HashSet<>();
    private List<Serie> series = new ArrayList<>();

    public Ejercicio() {

    }

    public Ejercicio(String nombre, String explicacion, Musculo musculoEnfocado) {
        this.nombre = nombre;
        this.explicacion = explicacion;
        this.musculoEnfocado = musculoEnfocado;
    }

}
