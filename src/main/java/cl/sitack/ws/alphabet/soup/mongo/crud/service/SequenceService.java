package cl.sitack.ws.alphabet.soup.mongo.crud.service;

public interface SequenceService<T> {
    T getNextValue(String idSecuencia);
}
