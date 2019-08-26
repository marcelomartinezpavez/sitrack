package cl.sitack.ws.alphabet.soup.mongo.crud.service;

import cl.sitack.ws.alphabet.soup.mongo.crud.domain.Sequence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service("integerSequenceService")
public class IntegerSequenceService implements SequenceService<Integer>{
    @Autowired
    private MongoOperations mongo;

    public IntegerSequenceService() {
    }

    public Integer getNextValue(String sequenceId) {
        Sequence sequence = (Sequence)this.mongo.findAndModify(Query.query(Criteria.where("_id").is(sequenceId)), (new Update()).inc("value", 1), FindAndModifyOptions.options().returnNew(true).upsert(true), Sequence.class);
        return sequence.getValue();
    }
}
