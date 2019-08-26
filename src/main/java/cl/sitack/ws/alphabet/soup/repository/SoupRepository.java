package cl.sitack.ws.alphabet.soup.repository;

import cl.sitack.ws.alphabet.soup.resource.Sopa;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SoupRepository extends MongoRepository<Sopa, String> {
}
