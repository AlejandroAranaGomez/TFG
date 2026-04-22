package trabajo.aplicacionSaludable.Repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import trabajo.aplicacionSaludable.Dominio.Alimento;
import trabajo.aplicacionSaludable.Dominio.Usuario;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlimentoRepository extends JpaRepository<Alimento, Long> {

    // alimento de un usuario
    Optional<Alimento> findByNombreAndUsuario(String nombre, Usuario usuario);

    // devuelve los alimento globales y los del usuario.
    List<Alimento> findByUsuarioOrUsuarioIsNull(Usuario usuario);

}
