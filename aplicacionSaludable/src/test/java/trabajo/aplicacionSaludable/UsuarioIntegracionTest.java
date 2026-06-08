package trabajo.aplicacionSaludable;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import trabajo.aplicacionSaludable.Dominio.Genero;
import trabajo.aplicacionSaludable.Dominio.NivelDeActividad;
import trabajo.aplicacionSaludable.Dominio.Objetivo;
import trabajo.aplicacionSaludable.Dominio.Usuario;
import trabajo.aplicacionSaludable.Dtos.InicioSesionDTO;
import trabajo.aplicacionSaludable.Dtos.RegistroDTO;
import trabajo.aplicacionSaludable.Dtos.UsuarioDTO;
import trabajo.aplicacionSaludable.Excepciones.ExcepcionesUsuarios.UsuarioYaRegistradoException;
import trabajo.aplicacionSaludable.Repositorios.UsuarioRepository;
import trabajo.aplicacionSaludable.Servicios.UsuarioService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class UsuarioIntegracionTest {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private RegistroDTO crearRegistroDTO() {

        RegistroDTO dto = new RegistroDTO();

        dto.setNombre("Alex");
        dto.setApellido1("Arana");
        dto.setApellido2("Test");

        dto.setEmail("test@gmail.com");
        dto.setContrasenha("1234");

        dto.setPeso(80);
        dto.setAltura(180);

        dto.setTelefono("123456789");

        dto.setGenero(Genero.HOMBRE);
        dto.setObjetivo(Objetivo.MANTENER_PESO);
        dto.setNivelDeActividad(NivelDeActividad.SEDENTARIO);

        dto.setFechaNacimiento("2000-01-01");

        return dto;
    }   


    @Test
    void registraUsuarioValido() {

        RegistroDTO dto = crearRegistroDTO();

        UsuarioDTO resultado = usuarioService.registrarUsuario(dto);

        assertNotNull(resultado);

        Usuario usuario =
                usuarioRepository.findById(
                        resultado.getIdUsuario()
                ).orElse(null);

        assertNotNull(usuario);

        assertEquals("Alex", usuario.getNombre());
        assertEquals("Arana", usuario.getApellido1());
        assertEquals("Test", usuario.getApellido2());
        assertEquals("test@gmail.com", usuario.getCredenciales().getEmail());

    }

    @Test
    void registrarUsuarioYaExistente() {

        RegistroDTO dto = crearRegistroDTO();

        // primer registro correcto
        usuarioService.registrarUsuario(dto);

        // segundo registro con mismo email
        assertThrows(
                UsuarioYaRegistradoException.class,
                () -> usuarioService.registrarUsuario(dto)
        );

    }

    @Test
    void contrasenhaEncriptada() {
        RegistroDTO dto = crearRegistroDTO();

        UsuarioDTO resultado =
                usuarioService.registrarUsuario(dto);

        Usuario usuario =
                usuarioRepository.findById(
                        resultado.getIdUsuario()
                ).orElse(null);

        assertNotNull(usuario);

        // Conmpruebo que la contrasenha es la misma encriptada
        assertTrue(passwordEncoder.matches("1234", usuario.getCredenciales().getContrasenha()));
    }

    @Test
    void inicioSesionValido() {
        RegistroDTO registro = crearRegistroDTO();

        registro.setEmail("login@gmail.com");

        usuarioService.registrarUsuario(registro);

        InicioSesionDTO login = new InicioSesionDTO();

        login.setEmail("login@gmail.com");
        login.setContrasenha("1234");

        UsuarioDTO resultado =
                usuarioService.iniciarSesion(login);

        assertNotNull(resultado);

        assertEquals("Alex", resultado.getNombre());
    }

    @Test
    void inicioSesionIncorrecto() {
        RegistroDTO registro = crearRegistroDTO();

        registro.setEmail("login@gmail.com");

        usuarioService.registrarUsuario(registro);

        InicioSesionDTO login = new InicioSesionDTO();

        login.setEmail("loginFalso@gmail.com");
        login.setContrasenha("1234");

        UsuarioDTO resultado =
                usuarioService.iniciarSesion(login);

        assertNull(resultado);
    }

}
