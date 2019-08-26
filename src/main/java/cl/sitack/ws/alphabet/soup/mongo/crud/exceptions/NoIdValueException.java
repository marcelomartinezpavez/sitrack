package cl.sitack.ws.alphabet.soup.mongo.crud.exceptions;

public class NoIdValueException extends Exception {
    public NoIdValueException(Throwable cause) {
        super(cause);
    }

    public NoIdValueException(String mensaje) {
        super(mensaje);
    }

    public NoIdValueException(Class documentClass) {
        super("se esperaba que la instancia de " + documentClass.getName() + " tuviese un valor para su id");
    }
}
