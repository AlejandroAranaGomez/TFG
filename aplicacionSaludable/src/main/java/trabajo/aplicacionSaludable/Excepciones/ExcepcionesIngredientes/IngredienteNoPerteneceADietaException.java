package trabajo.aplicacionSaludable.Excepciones.ExcepcionesIngredientes;

public class IngredienteNoPerteneceADietaException extends RuntimeException {
    public IngredienteNoPerteneceADietaException() {
        super("Este ingrediente no pertenece a esta comida");
    }
}
