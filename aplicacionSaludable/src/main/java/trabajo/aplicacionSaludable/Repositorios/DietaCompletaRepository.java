package trabajo.aplicacionSaludable.Repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import trabajo.aplicacionSaludable.Dominio.DietaCompleta;
import trabajo.aplicacionSaludable.Dominio.Usuario;

import java.util.List;
import java.util.Optional;

public interface DietaCompletaRepository extends JpaRepository<DietaCompleta, Long> {

    List<DietaCompleta> findByUsuario(Usuario usuario);

    Optional<DietaCompleta> findByUsuarioAndActivaTrue(Usuario usuario);

    Optional<DietaCompleta> findByNombreAndUsuario(String nombre, Usuario usuario);

    Optional<DietaCompleta> findByIdDietaCompleta(Long idDietaCompleta);
}
