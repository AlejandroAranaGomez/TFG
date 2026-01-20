package trabajo.aplicacionSaludable.Dtos;

import lombok.Getter;
import lombok.Setter;
import trabajo.aplicacionSaludable.Dominio.Musculo;

@Getter
@Setter

public class EjercicioDTO {

    private Long idEjercicio;
    private String nombre;
    private Musculo musculoEnfocado;
    private String idApi;

    public EjercicioDTO() {

    }

    public EjercicioDTO(Long idEjercicio, String nombre, Musculo musculoEnfocado, String idApi) {
        this.idEjercicio = idEjercicio;
        this.nombre = nombre;
        this.musculoEnfocado = musculoEnfocado;
        this.idApi = idApi;
    }

}
