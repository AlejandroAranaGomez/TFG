package trabajo.aplicacionSaludable.Dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class UsuarioDTO {

    private Long idUsuario;
    private String nombre;

    public UsuarioDTO() {

    }

    public UsuarioDTO(Long idUsuario, String nombre) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
    }
}
