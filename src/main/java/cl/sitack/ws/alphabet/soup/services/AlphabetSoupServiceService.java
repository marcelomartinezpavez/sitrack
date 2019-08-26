package cl.sitack.ws.alphabet.soup.services;

import cl.sitack.ws.alphabet.soup.dto.ConfiguracionSoupDTO;
import cl.sitack.ws.alphabet.soup.resource.Solucion;
import cl.sitack.ws.alphabet.soup.resource.Sopa;

public interface AlphabetSoupServiceService {

    Sopa createSoup(ConfiguracionSoupDTO configuracionSoupDTO) throws Exception;

    void mostrarSopa();
    String resolver(String palabra);
    void resolver(Solucion solucion);
}
