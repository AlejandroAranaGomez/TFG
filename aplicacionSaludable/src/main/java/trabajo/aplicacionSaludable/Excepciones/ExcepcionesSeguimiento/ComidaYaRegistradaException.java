package trabajo.aplicacionSaludable.Excepciones.ExcepcionesSeguimiento;

public class ComidaYaRegistradaException extends RuntimeException {
    public ComidaYaRegistradaException() {
        super("Esta comida ya esta registrada");
    }
}
