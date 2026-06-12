package trabajo.aplicacionSaludable.Repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import trabajo.aplicacionSaludable.Dominio.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

}
