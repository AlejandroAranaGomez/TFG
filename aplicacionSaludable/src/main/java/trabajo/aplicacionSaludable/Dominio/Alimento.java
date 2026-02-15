package trabajo.aplicacionSaludable.Dominio;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter

@NoArgsConstructor

@Entity
@Table(name = "alimento")
public class Alimento {

    // Clave Primaria BBDD
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAlimento;

    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false)
    private float calorias;
    @Column(nullable = false)
    private float proteinas;
    @Column(nullable = false)
    private float carbohidratos;
    @Column(nullable = false)
    private float grasas;

    // Existen alientos globales que todo el mundo tiene y alimentos personales creado por usuarios que son unicamente suyos
    // si un alimento tiene a null el usuario significa que es global no lo ha creado nadie.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUsuario", nullable = true)
    private Usuario usuario;

    public Alimento(String nombre, float calorias,float proteinas, float carbohidratos, float grasas,  Usuario usuario) {
        this.nombre = nombre;
        this.calorias = calorias;
        this.proteinas = proteinas;
        this.carbohidratos = carbohidratos;
        this.grasas = grasas;
        this.usuario = usuario;
    }
    
}
