package trabajo.aplicacionSaludable.Excepciones.ExcepcionesEjercicios;

public class EjercicioDuplicadoException extends RuntimeException {
    public EjercicioDuplicadoException() {
        super("Ya existe este ejercicio en este dia.");
    }
}
