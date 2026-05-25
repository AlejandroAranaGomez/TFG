package trabajo.aplicacionSaludable.Excepciones.ExcepcionesUsuarios;

public class UsuarioYaRegistradoException extends RuntimeException {
    public UsuarioYaRegistradoException() {
        super("El email ya está registrado");
    }
}
