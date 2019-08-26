package cl.sitack.ws.alphabet.soup.services;

import cl.sitack.ws.alphabet.soup.dto.ConfiguracionSoupDTO;
import cl.sitack.ws.alphabet.soup.mongo.crud.exceptions.*;
import cl.sitack.ws.alphabet.soup.mongo.crud.service.MongoCRUDServiceAbstract;
import cl.sitack.ws.alphabet.soup.repository.SoupRepository;
import cl.sitack.ws.alphabet.soup.resource.Sopa;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;


@Service(value = "alphabetSoupCRUDServiceImpl" )
public class AlphabetSoupCRUDServiceImpl extends MongoCRUDServiceAbstract<ConfiguracionSoupDTO, Sopa,String> {
    private static final Logger logger = LoggerFactory.getLogger(AlphabetSoupCRUDServiceImpl.class);

    @Autowired
    private SoupRepository mongoRepository;

    @Autowired
    protected MongoTemplate mongoTemplate;

    @Override
    protected MongoRepository<Sopa, String> getMongoRepository() {

        return mongoRepository;
    }

    public AlphabetSoupCRUDServiceImpl() throws NoDocumentException, NoFieldIdException, IdTypeAndIdDocumentIncompatiblesException {
    }

    @Override
    public ConfiguracionSoupDTO actualizar(String s, ConfiguracionSoupDTO dtoObject) throws ValidacionDeNegocioException, RecursoNoEncontradoException {
        return null;
    }
}

