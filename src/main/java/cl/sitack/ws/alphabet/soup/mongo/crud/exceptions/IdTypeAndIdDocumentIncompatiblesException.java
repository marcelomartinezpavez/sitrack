package cl.sitack.ws.alphabet.soup.mongo.crud.exceptions;

public class IdTypeAndIdDocumentIncompatiblesException extends Exception{
    public IdTypeAndIdDocumentIncompatiblesException(Throwable cause) {
        super(cause);
    }

    public IdTypeAndIdDocumentIncompatiblesException(String mensaje) {
        super(mensaje);
    }

    public IdTypeAndIdDocumentIncompatiblesException(Class documentClass, Class idClass) {
        super("no es posible asignar un valor de tipo " + idClass.getName() + " como id en un objeto de tipo " + documentClass.getName());
    }
}
