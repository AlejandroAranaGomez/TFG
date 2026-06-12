package trabajo.aplicacionSaludable.Dominio;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter

@NoArgsConstructor

@Entity
@Table(name = "registroComidaDiaria")
public class RegistroComidaDiaria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRegistroComidaDiaria;


    @Column(nullable = false)
    private LocalDate fecha;
    @Column(nullable = false)
    private float caloriasTotales;
    @Column(nullable = false)
    private float proteinas;
    @Column(nullable = false)
    private float carbohidratos;
    @Column(nullable = false)
    private float grasas;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUsuario", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idComida", nullable = false)
    private Comida comida;

    public RegistroComidaDiaria(LocalDate fecha, float caloriasTotales, float proteinas, float carbohidratos, float grasas) {
        this.fecha = fecha;
        this.caloriasTotales = caloriasTotales;
        this.proteinas = proteinas;
        this.carbohidratos = carbohidratos;
        this.grasas = grasas;
    }


}
