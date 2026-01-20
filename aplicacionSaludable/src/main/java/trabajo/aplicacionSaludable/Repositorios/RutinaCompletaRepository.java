package trabajo.aplicacionSaludable.Repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import trabajo.aplicacionSaludable.Dominio.RutinaCompleta;
import trabajo.aplicacionSaludable.Dominio.Usuario;

import java.util.List;
import java.util.Optional;

@Repository
public interface RutinaCompletaRepository extends JpaRepository<RutinaCompleta, Long> {
    List<RutinaCompleta> findByUsuario(Usuario usuario);

    Optional<RutinaCompleta> findByNombreRutinaCompletaAndUsuario(String nombreRutinaCompleta, Usuario usuario);

    Optional<RutinaCompleta> findByIdRutinaCompleta(Long idRutinaCompleta);
}
