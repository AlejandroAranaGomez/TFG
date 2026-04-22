package trabajo.aplicacionSaludable.Dtos;

import lombok.Getter;
import lombok.Setter;
import trabajo.aplicacionSaludable.Dominio.HistorialPeso;

@Getter
@Setter
public class HistorialPesoDTO {

    private float peso;
    private String fecha;

    public HistorialPesoDTO() {

    }

    public HistorialPesoDTO(float peso, String fecha) {
        this.peso = peso;
        this.fecha = fecha;
    }
}
