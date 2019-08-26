package cl.sitack.ws.alphabet.soup.mongo.crud.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(
        collection = "sequences"
)
public class Sequence {
    @Id
    private String id;
    private Integer value;

    public Sequence() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getValue() {
        return this.value;
    }

}
