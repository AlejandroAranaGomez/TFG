package trabajo.aplicacionSaludable.Excepciones.ExcepcionesIngredientes;

public class CantidadNegativaException extends RuntimeException {
    public CantidadNegativaException() {
        super("La cantidad en gramos debe ser mayor que 0");
    }
}
