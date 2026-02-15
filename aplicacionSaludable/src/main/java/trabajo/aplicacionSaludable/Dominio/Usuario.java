package trabajo.aplicacionSaludable.Dominio;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter

@NoArgsConstructor

@Entity
@Table(name = "usuario")
public class Usuario {

    // Clave Primaria BBDD
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private Genero genero;
    @Column(nullable = false)
    private float peso;
    @Column(nullable = false)
    private int altura;
    @Column(nullable = false)
    private String telefono;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Objetivo objetivo;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private NivelDeActividad nivelDeActividad;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL,  orphanRemoval = true)
    private Set<DietaCompleta> dietas = new HashSet<>();
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL,  orphanRemoval = true)
    private Set<RutinaCompleta> rutinas = new HashSet<>();
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL,  orphanRemoval = true)
    private Set<Alimento> alimentosPersonales = new HashSet<>();
    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private Credenciales credenciales;

    public Usuario(String nombre, String apellido1, String apellido2, LocalDate fechaNacimiento, Genero genero, float peso, int altura, String telefono,
                   Objetivo objetivo, NivelDeActividad nivelDeActividad) {
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.fechaNacimiento = fechaNacimiento;
        this.genero = genero;
        this.peso = peso;
        this.altura = altura;
        this.telefono = telefono;
        this.objetivo = objetivo;
        this.nivelDeActividad = nivelDeActividad;
    }

}
