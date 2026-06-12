package trabajo.aplicacionSaludable.Dtos;

import lombok.Getter;
import lombok.Setter;
import trabajo.aplicacionSaludable.Dominio.Genero;
import trabajo.aplicacionSaludable.Dominio.NivelDeActividad;
import trabajo.aplicacionSaludable.Dominio.Objetivo;

@Getter
@Setter
public class UsuarioPerfilDTO {

    private Long idUsuario;

    private String nombre;
    private String apellido1;
    private String apellido2;

    private String fechaNacimiento;

    private float peso;
    private int altura;

    private Objetivo objetivo;
    private NivelDeActividad nivelDeActividad;

    private String email;
    private String telefono;
    private Genero genero;

    private int caloriasObjetivo;
}
