package trabajo.aplicacionSaludable.Dtos;

import lombok.Getter;
import lombok.Setter;
import trabajo.aplicacionSaludable.Dominio.Genero;
import trabajo.aplicacionSaludable.Dominio.NivelDeActividad;
import trabajo.aplicacionSaludable.Dominio.Objetivo;

@Getter
@Setter

public class RegistroDTO {

    private String nombre;
    private String apellido1;
    private String apellido2;
    private String fechaNacimiento;
    private Genero genero;
    private float peso;
    private int altura;
    private String email;
    private String telefono;
    private String contrasenha;
    private Objetivo objetivo;
    private NivelDeActividad nivelDeActividad;

    public RegistroDTO() {

    }

}
