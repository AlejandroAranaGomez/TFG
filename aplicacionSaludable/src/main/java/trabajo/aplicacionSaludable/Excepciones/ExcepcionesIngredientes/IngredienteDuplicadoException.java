package trabajo.aplicacionSaludable.Excepciones.ExcepcionesIngredientes;

public class IngredienteDuplicadoException extends RuntimeException {
    public IngredienteDuplicadoException() {
        super("Ya existe este ingrediente en esta comida.");
    }
}
