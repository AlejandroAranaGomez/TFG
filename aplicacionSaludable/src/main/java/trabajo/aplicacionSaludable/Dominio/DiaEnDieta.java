package trabajo.aplicacionSaludable.Dominio;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter

@Entity
@Table(name = "diaEnDieta")
public class DiaEnDieta {

    // Clave Primaria BBDD

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idDiaEnDieta")
    private Long idDiaEnDieta;

    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false)
    private float caloriasTotales;
    @Column(nullable = false)
    private float proteinas;
    @Column(nullable = false)
    private float carbohidratos;
    @Column(nullable = false)
    private float grasas;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DiaDeLaSemana diaDeLaSemana;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idDietaCompleta",  nullable = false)
    private DietaCompleta dietaCompleta;

    @OneToMany(mappedBy = "diaEnDieta", cascade = CascadeType.ALL,  orphanRemoval = true)
    private List<Comida> comidas = new ArrayList<>();

    public DiaEnDieta() {

    }

    public DiaEnDieta(String nombre, float caloriasTotales, float proteinas, float carbohidratos, float grasas, DiaDeLaSemana diaDeLaSemana,  DietaCompleta dietaCompleta) {
        this.nombre = nombre;
        this.caloriasTotales = caloriasTotales;
        this.proteinas = proteinas;
        this.carbohidratos = carbohidratos;
        this.grasas = grasas;
        this.diaDeLaSemana = diaDeLaSemana;
        this.dietaCompleta = dietaCompleta;
    }

}
