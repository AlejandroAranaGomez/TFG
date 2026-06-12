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
@Table(name = "historialPeso")
public class HistorialPeso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idHistorialPeso;

    @Column(nullable = false)
    private float peso;
    @Column(nullable = false)
    private LocalDate fecha;

    @ManyToOne
    @JoinColumn(name = "idUsuario", nullable = false)
    private Usuario usuario;

    public HistorialPeso(float peso, LocalDate fecha) {
        this.peso = peso;
        this.fecha = fecha;
    }

}
