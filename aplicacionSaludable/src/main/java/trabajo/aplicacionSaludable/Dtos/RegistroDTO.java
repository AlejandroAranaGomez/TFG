package trabajo.aplicacionSaludable.Dtos;

import lombok.Getter;
import lombok.Setter;
import trabajo.aplicacionSaludable.Dominio.Generos;

@Getter
@Setter

public class RegistroDTO {

    private String nombre;
    private String apellido1;
    private String apellido2;
    private String fechaNacimiento;
    private Generos genero;
    private float peso;
    private float altura;
    private String email;
    private String telefono;
    private String contrasenha;

    public RegistroDTO() {

    }

}
