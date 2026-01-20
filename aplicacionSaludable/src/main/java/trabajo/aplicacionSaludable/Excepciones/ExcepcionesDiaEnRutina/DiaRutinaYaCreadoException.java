package trabajo.aplicacionSaludable.Excepciones.ExcepcionesDiaEnRutina;

public class DiaRutinaYaCreadoException extends RuntimeException {
    public DiaRutinaYaCreadoException() {
        super("Ya tienes este dia creado en la rutina.");
    }
}
