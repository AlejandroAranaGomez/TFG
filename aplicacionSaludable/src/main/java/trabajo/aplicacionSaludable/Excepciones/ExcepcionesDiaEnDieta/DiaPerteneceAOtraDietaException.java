package trabajo.aplicacionSaludable.Excepciones.ExcepcionesDiaEnDieta;

public class DiaPerteneceAOtraDietaException extends RuntimeException {
    public DiaPerteneceAOtraDietaException() {
        super("Este dia pertence a otra dieta");
    }
}
