package trabajo.aplicacionSaludable.Repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import trabajo.aplicacionSaludable.Dominio.Alimento;
import trabajo.aplicacionSaludable.Dominio.Usuario;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlimentoRepository extends JpaRepository<Alimento, Long> {

    // Alimento global
    Optional<Alimento> findByNombreAndUsuarioIsNull(String nombre);

    // alimento de un usuario
    Optional<Alimento> findByNombreAndUsuario(String nombre, Usuario usuario);

    // alimento por id
    Optional<Alimento> findByIdAlimento(Long idAlimento);

    // devuelve los alimento globales y los del usuario.
    List<Alimento> findByUsuarioOrUsuarioIsNull(Usuario usuario);

    // Todos los alimentos globales
    List<Alimento> findByUsuarioIsNull();
}
