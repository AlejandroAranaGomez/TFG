package trabajo.aplicacionSaludable.Dominio;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Serie {

    private Long idSerie;

    private String serieAnterior;
    private int repeticiones;
    private float peso;

    private Ejercicio ejercicio;

    public Serie() {

    }

    public Serie(String serieAnterior, int repeticiones, float peso, Ejercicio ejercicio) {
        this.serieAnterior = serieAnterior;
        this.repeticiones = repeticiones;
        this.peso = peso;
        this.ejercicio = ejercicio;
    }

}
