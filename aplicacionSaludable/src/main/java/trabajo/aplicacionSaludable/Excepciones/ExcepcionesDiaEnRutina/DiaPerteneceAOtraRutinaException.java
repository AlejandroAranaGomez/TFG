package trabajo.aplicacionSaludable.Excepciones.ExcepcionesDiaEnRutina;

public class DiaPerteneceAOtraRutinaException extends RuntimeException {
    public DiaPerteneceAOtraRutinaException() {
        super("Este dia pertence a otra rutina");
    }
}
