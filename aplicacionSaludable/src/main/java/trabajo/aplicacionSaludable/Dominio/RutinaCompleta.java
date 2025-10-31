package trabajo.aplicacionSaludable.Dominio;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter

public class RutinaCompleta {

    private Long idRutinaCompleta;

    private String nombreCompleta;
    private String resumen;

    private Usuario usuario;

    private List<DiaEnRutina> diaEnRutinas = new ArrayList<>();

    public RutinaCompleta() {

    }

    public RutinaCompleta(String nombreCompleta, String resumen, Usuario usuario) {
        this.nombreCompleta = nombreCompleta;
        this.resumen = resumen;
        this.usuario = usuario;
    }

}
