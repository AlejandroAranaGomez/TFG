package trabajo.aplicacionSaludable.Excepciones.ExcepcionesSeguimiento;

public class ComidaNoRegistradaException extends RuntimeException {
    public ComidaNoRegistradaException() {
        super("Esta comida no esta registrada");
    }
}
