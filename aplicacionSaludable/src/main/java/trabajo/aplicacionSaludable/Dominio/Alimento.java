package trabajo.aplicacionSaludable.Dominio;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter

@Entity
@Table(name = "alimento")
public class Alimento {

    // Clave Primaria BBDD
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idAlimento")
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

    @OneToMany(mappedBy = "alimento", cascade = CascadeType.ALL,  orphanRemoval = true)
    private Set<Ingrediente> ingredientesEnComidas = new HashSet<>();

    public Alimento() {

    }

    public Alimento(String nombre, float calorias,float proteinas, float carbohidratos, float grasas) {
        this.nombre = nombre;
        this.calorias = calorias;
        this.proteinas = proteinas;
        this.carbohidratos = carbohidratos;
        this.grasas = grasas;
    }
    
}
