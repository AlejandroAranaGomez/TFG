package trabajo.aplicacionSaludable.Excepciones.ExcepcionesDietas;

public class DietaDuplicadaException extends RuntimeException {

    public DietaDuplicadaException() {
        super("Ya existe una dieta con este nombre.");
    }
}
