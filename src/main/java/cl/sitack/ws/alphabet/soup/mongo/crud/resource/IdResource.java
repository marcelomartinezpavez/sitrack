package cl.sitack.ws.alphabet.soup.mongo.crud.resource;

public class IdResource<Id> {
    Id id;

    public IdResource() {
    }

    public IdResource(Id id) {
        this.id = id;
    }

    public Id getId() {
        return this.id;
    }

    public void setId(Id id) {
        this.id = id;
    }
}
