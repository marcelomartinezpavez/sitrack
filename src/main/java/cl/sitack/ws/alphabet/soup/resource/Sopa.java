package cl.sitack.ws.alphabet.soup.resource;

import cl.sitack.ws.alphabet.soup.dto.ConfiguracionSoupDTO;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "alphabetsoup")
@TypeAlias(value = "alphabetsoup")
public class Sopa {

    @Id
    private String id;

//    ArrayList<ArrayList<String>> sopa = new ArrayList<ArrayList<String>>();

    private char[][] sopa;
    private ConfiguracionSoupDTO configuracionSoupDTO;
    private List<String> resultadoPalabras;
    private List<String> palabrasEncontradas;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public char[][] getSopa() {
        return sopa;
    }

    public void setSopa(char[][] sopa) {
        this.sopa = sopa;
    }

    /*public ArrayList<ArrayList<String>> getSopa() {
        return sopa;
    }

    public void setSopa(ArrayList<ArrayList<String>> sopa) {
        this.sopa = sopa;
    }*/

    public ConfiguracionSoupDTO getConfiguracionSoupDTO() {
        return configuracionSoupDTO;
    }

    public void setConfiguracionSoupDTO(ConfiguracionSoupDTO configuracionSoupDTO) {
        this.configuracionSoupDTO = configuracionSoupDTO;
    }

    public List<String> getResultadoPalabras() {
        return resultadoPalabras;
    }

    public void setResultadoPalabras(List<String> resultadoPalabras) {
        this.resultadoPalabras = resultadoPalabras;
    }

    public List<String> getPalabrasEncontradas() {
        return palabrasEncontradas;
    }

    public void setPalabrasEncontradas(List<String> palabrasEncontradas) {
        this.palabrasEncontradas = palabrasEncontradas;
    }
}
