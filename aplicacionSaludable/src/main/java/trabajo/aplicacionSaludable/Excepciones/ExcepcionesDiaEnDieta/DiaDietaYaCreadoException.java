package trabajo.aplicacionSaludable.Excepciones.ExcepcionesDiaEnDieta;

public class DiaDietaYaCreadoException extends RuntimeException {
    public DiaDietaYaCreadoException() {
        super("Ya tienes este dia creado en la dieta.");
    }
}
