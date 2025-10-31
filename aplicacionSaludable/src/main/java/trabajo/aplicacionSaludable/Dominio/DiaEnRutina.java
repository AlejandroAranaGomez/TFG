package trabajo.aplicacionSaludable.Dominio;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter

public class DiaEnRutina {

    private Long idDiaEnRutina;

    private String nombre;
    private String resumen;
    private float caloriasQuemadas;
    private DiaDeLaSemana diaDeLaSemana;

    private RutinaCompleta rutinaCompleta;

    private List<Ejercicio> ejercicios = new ArrayList<>();

    public DiaEnRutina() {

    }

    public DiaEnRutina(String nombre, String resumen, float caloriasQuemadas, DiaDeLaSemana diaDeLaSemana, RutinaCompleta rutinaCompleta) {
        this.nombre = nombre;
        this.resumen = resumen;
        this.caloriasQuemadas = caloriasQuemadas;
        this.diaDeLaSemana = diaDeLaSemana;
        this.rutinaCompleta = rutinaCompleta;
    }

}
