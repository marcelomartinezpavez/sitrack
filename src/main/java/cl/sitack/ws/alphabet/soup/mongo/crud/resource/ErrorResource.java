package cl.sitack.ws.alphabet.soup.mongo.crud.resource;

public class ErrorResource {
    private Long codigo;
    private String mensaje;

    public ErrorResource() {
    }

    public ErrorResource(String mensaje) {
        this.mensaje = mensaje;
    }

    public ErrorResource(Long codigo, String mensaje) {
        this.codigo = codigo;
        this.mensaje = mensaje;
    }

    public Long getCodigo() {
        return this.codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getMensaje() {
        return this.mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
