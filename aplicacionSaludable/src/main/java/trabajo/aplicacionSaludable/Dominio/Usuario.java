package trabajo.aplicacionSaludable.Dominio;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter

public class Usuario {

    // Clave Primaria BBDD
    private Long idUsuario;

    private String nombre;
    private String apellido1;
    private String apellido2;
    private LocalDate fechaNacimiento;
    private String genero;
    private float peso;
    private float altura;
    private String email;
    private String contrasenha;

    private Set<DietaCompleta> dietas = new HashSet<>();
    private Set<PlanificacionDeDieta> planificaciones = new HashSet<>();
    private Set<RutinaCompleta> rutinas = new HashSet<>();


    // Necesario para JPA
    public Usuario() {

    }

    public Usuario(String nombre, String apellido1, String apellido2, LocalDate fechaNacimiento,
                   String genero, float peso, float altura, String email, String contrasenha) {
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.fechaNacimiento = fechaNacimiento;
        this.genero = genero;
        this.peso = peso;
        this.altura = altura;
        this.email = email;
        this.contrasenha = contrasenha;
    }



}
