package trabajo.aplicacionSaludable.Dominio;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter

@NoArgsConstructor

@Entity
@Table(name = "credenciales")
public class Credenciales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCredenciales;

    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String contrasenha;

    @OneToOne
    @JoinColumn(name = "idUsuario", nullable = false)
    private Usuario usuario;

    public Credenciales(String email, String contrasenha) {
        this.email = email;
        this.contrasenha = contrasenha;
    }


}
