package trabajo.aplicacionSaludable.Assemblers;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import trabajo.aplicacionSaludable.Dominio.Credenciales;
import trabajo.aplicacionSaludable.Dominio.Usuario;
import trabajo.aplicacionSaludable.Dtos.RegistroDTO;
import trabajo.aplicacionSaludable.Dtos.UsuarioActualizarDTO;
import trabajo.aplicacionSaludable.Dtos.UsuarioDTO;
import trabajo.aplicacionSaludable.Dtos.UsuarioPerfilDTO;
import trabajo.aplicacionSaludable.Servicios.CalculoCalorias;

import java.time.LocalDate;

@Component
public class UsuarioAssembler {

    private final PasswordEncoder passwordEncoder;

    public UsuarioAssembler(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario dtoAEntidadRegistro(RegistroDTO registroDTO) {

        LocalDate fechaNacimiento = LocalDate.parse(registroDTO.getFechaNacimiento());

        int calorias = CalculoCalorias.calcularCalorias(
                registroDTO.getPeso(),
                registroDTO.getAltura(),
                fechaNacimiento,
                registroDTO.getGenero(),
                registroDTO.getNivelDeActividad(),
                registroDTO.getObjetivo()
        );

        Usuario usuario = new Usuario(
                registroDTO.getNombre(),
                registroDTO.getApellido1(),
                registroDTO.getApellido2(),
                fechaNacimiento,
                registroDTO.getGenero(),
                registroDTO.getPeso(),
                registroDTO.getAltura(),
                registroDTO.getTelefono(),
                registroDTO.getObjetivo(),
                registroDTO.getNivelDeActividad(),
                calorias
        );

        Credenciales credenciales = new Credenciales(
                registroDTO.getEmail(),
                passwordEncoder.encode(registroDTO.getContrasenha())
        );

        credenciales.setUsuario(usuario);
        usuario.setCredenciales(credenciales);

        return usuario;
    }

    public UsuarioDTO entidadADTOBasico(Usuario usuario) {
        return new UsuarioDTO(
                usuario.getIdUsuario(),
                usuario.getNombre()
        );
    }

    public UsuarioPerfilDTO entidadADTOPerfil(Usuario usuario) {
        UsuarioPerfilDTO dto = new UsuarioPerfilDTO();

        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setNombre(usuario.getNombre());
        dto.setApellido1(usuario.getApellido1());
        dto.setApellido2(usuario.getApellido2());
        dto.setFechaNacimiento(usuario.getFechaNacimiento().toString());
        dto.setGenero(usuario.getGenero());
        dto.setPeso(usuario.getPeso());
        dto.setAltura(usuario.getAltura());
        dto.setTelefono(usuario.getTelefono());
        dto.setObjetivo(usuario.getObjetivo());
        dto.setNivelDeActividad(usuario.getNivelDeActividad());
        dto.setEmail(usuario.getCredenciales().getEmail());
        dto.setCaloriasObjetivo(usuario.getCaloriasObjetivo());

        return dto;
    }

    public void actualizarEntidadDesdeDTO(Usuario usuario, UsuarioActualizarDTO dto) {
        usuario.setNombre(dto.getNombre());
        usuario.setApellido1(dto.getApellido1());
        usuario.setApellido2(dto.getApellido2());
        usuario.setFechaNacimiento(LocalDate.parse(dto.getFechaNacimiento()));
        usuario.setAltura(dto.getAltura());
        usuario.setTelefono(dto.getTelefono());
        usuario.setObjetivo(dto.getObjetivo());
        usuario.setNivelDeActividad(dto.getNivelDeActividad());
    }

}
