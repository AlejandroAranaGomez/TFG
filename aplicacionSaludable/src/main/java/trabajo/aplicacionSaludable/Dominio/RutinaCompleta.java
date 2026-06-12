package trabajo.aplicacionSaludable.Dominio;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter

@NoArgsConstructor

@Entity
@Table(name = "rutinaCompleta")
public class RutinaCompleta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRutinaCompleta;

    @Column(nullable = false)
    private String nombreRutinaCompleta;

    private String resumen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUsuario", nullable = false)
    private Usuario usuario;

    @OneToMany(mappedBy = "rutinaCompleta", cascade = CascadeType.ALL,  orphanRemoval = true)
    private List<DiaEnRutina> diaEnRutinas = new ArrayList<>();

    public RutinaCompleta(String nombreCompleta, String resumen, Usuario usuario) {
        this.nombreRutinaCompleta = nombreCompleta;
        this.resumen = resumen;
        this.usuario = usuario;
    }

}
