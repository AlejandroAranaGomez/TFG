package trabajo.aplicacionSaludable.Excepciones.ExcepcionesDietas;

public class DietaDeOtroUsuarioException extends RuntimeException {

    public DietaDeOtroUsuarioException() {
        super("Esta dieta pertenece a otro usuario.");
    }
}
