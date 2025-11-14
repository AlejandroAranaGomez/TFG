package trabajo.aplicacionSaludable.Dominio;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter

@Entity
@Table(name = "usuario")
public class Usuario {

    // Clave Primaria BBDD
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idUsuario")
    private Long idUsuario;

    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false)
    private String apellido1;

    private String apellido2;
    @Column(nullable = false)
    private LocalDate fechaNacimiento;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Generos genero;
    @Column(nullable = false)
    private float peso;
    @Column(nullable = false)
    private float altura;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String telefono;
    @Column(nullable = false)
    private String contrasenha;
    @Enumerated(EnumType.STRING)
    private Objetivos objetivo;
    @Enumerated(EnumType.STRING)
    private NivelDeActividad nivelDeActividad;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL,  orphanRemoval = true)
    private Set<DietaCompleta> dietas = new HashSet<>();
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL,  orphanRemoval = true)
    private Set<PlanificacionDeDieta> planificacionesDieta = new HashSet<>();
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL,  orphanRemoval = true)
    private Set<RutinaCompleta> rutinas = new HashSet<>();


    // Necesario para JPA
    public Usuario() {

    }

    public Usuario(String nombre, String apellido1, String apellido2, LocalDate fechaNacimiento,
                   Generos genero, float peso, float altura, String email, String telefono, String contrasenha,
                   Objetivos objetivo,  NivelDeActividad nivelDeActividad) {
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.fechaNacimiento = fechaNacimiento;
        this.genero = genero;
        this.peso = peso;
        this.altura = altura;
        this.email = email;
        this.telefono = telefono;
        this.contrasenha = contrasenha;
        this.objetivo = objetivo;
        this.nivelDeActividad = nivelDeActividad;
    }

}
