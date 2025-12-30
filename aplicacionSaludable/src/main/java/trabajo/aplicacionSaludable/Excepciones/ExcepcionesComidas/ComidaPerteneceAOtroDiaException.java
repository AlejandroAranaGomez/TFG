package trabajo.aplicacionSaludable.Excepciones.ExcepcionesComidas;

public class ComidaPerteneceAOtroDiaException extends RuntimeException {
    public ComidaPerteneceAOtroDiaException() {
        super("Esta comida no pertenece a este día");
    }
}
