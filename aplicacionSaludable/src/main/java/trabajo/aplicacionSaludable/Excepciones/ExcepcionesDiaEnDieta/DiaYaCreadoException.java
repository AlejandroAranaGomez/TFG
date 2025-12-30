package trabajo.aplicacionSaludable.Excepciones.ExcepcionesDiaEnDieta;

public class DiaYaCreadoException extends RuntimeException {
    public DiaYaCreadoException() {
        super("Ya tienes este dia creado en la dieta.");
    }
}
