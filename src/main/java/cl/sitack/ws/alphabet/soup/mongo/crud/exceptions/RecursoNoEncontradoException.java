package cl.sitack.ws.alphabet.soup.mongo.crud.exceptions;

public class RecursoNoEncontradoException extends Exception {
    public RecursoNoEncontradoException(Throwable cause) {
        super(cause);
    }

    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
