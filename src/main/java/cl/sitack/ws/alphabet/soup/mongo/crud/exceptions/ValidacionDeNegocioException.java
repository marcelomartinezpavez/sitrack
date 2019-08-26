package cl.sitack.ws.alphabet.soup.mongo.crud.exceptions;

import cl.sitack.ws.alphabet.soup.mongo.crud.resource.ErrorResource;

public class ValidacionDeNegocioException extends Exception {

    private ErrorResource errorResource;

    public ValidacionDeNegocioException(String mensaje) {
        super(mensaje);
        this.errorResource = new ErrorResource(mensaje);
    }

    public ValidacionDeNegocioException(String mensaje, Long codigo) {
        super(mensaje);
        this.errorResource = new ErrorResource(codigo, mensaje);
    }

    public ErrorResource getErrorResource() {
        return this.errorResource;
    }
}
