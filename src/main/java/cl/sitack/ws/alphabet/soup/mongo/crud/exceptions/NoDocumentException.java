package cl.sitack.ws.alphabet.soup.mongo.crud.exceptions;

public class NoDocumentException extends Exception{
    public NoDocumentException(Throwable cause) {
        super(cause);
    }

    public NoDocumentException(Class documentClass) {
        super("se esperaba que la clase " + documentClass.getName() + " tenga la anotación @Document");
    }

    public NoDocumentException(String mensaje) {
        super(mensaje);
    }
}
