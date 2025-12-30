package trabajo.aplicacionSaludable.Excepciones.ExcepcionesComidas;

public class ComidaYaExisteException extends RuntimeException {
    public ComidaYaExisteException() {
        super("Ya existe una comida con ese nombre en este día");
    }
}
