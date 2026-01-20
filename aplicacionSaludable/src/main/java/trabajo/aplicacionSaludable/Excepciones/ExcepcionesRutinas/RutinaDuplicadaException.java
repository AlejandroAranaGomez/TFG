package trabajo.aplicacionSaludable.Excepciones.ExcepcionesRutinas;

public class RutinaDuplicadaException extends RuntimeException {
    public RutinaDuplicadaException() {
        super("Ya tienes una rutina con este nombre");
    }
}
