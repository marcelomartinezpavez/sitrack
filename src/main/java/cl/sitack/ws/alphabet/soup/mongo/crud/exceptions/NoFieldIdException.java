package cl.sitack.ws.alphabet.soup.mongo.crud.exceptions;

public class NoFieldIdException extends Exception{
    public NoFieldIdException(Throwable cause) {
        super(cause);
    }

    public NoFieldIdException(String mensaje) {
        super(mensaje);
    }

    public NoFieldIdException(Class documentClass) {
        super("se esperaba que la clase " + documentClass.getName() + " tenga la anotación @Id");
    }
}
