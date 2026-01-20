package trabajo.aplicacionSaludable.Excepciones.ExcepcionesRutinas;

public class RutinaOtroUsuarioException extends RuntimeException {
    public RutinaOtroUsuarioException() {
        super("Esta rutina pertence a otro usuario");
    }
}
