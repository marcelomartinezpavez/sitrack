package cl.sitack.ws.alphabet.soup.services;

import cl.sitack.ws.alphabet.soup.dto.ConfiguracionSoupDTO;

import cl.sitack.ws.alphabet.soup.mongo.crud.exceptions.*;
import cl.sitack.ws.alphabet.soup.mongo.crud.resource.IdResource;
import cl.sitack.ws.alphabet.soup.mongo.crud.service.MongoCRUDServiceAbstract;
import cl.sitack.ws.alphabet.soup.mongo.crud.service.SequenceService;
import cl.sitack.ws.alphabet.soup.repository.SoupRepository;
import cl.sitack.ws.alphabet.soup.resource.Solucion;
import cl.sitack.ws.alphabet.soup.resource.Sopa;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service("alphabet-soup")
public class AlphabetSoupServiceServiceImpl extends MongoCRUDServiceAbstract<ConfiguracionSoupDTO,Sopa,String> implements AlphabetSoupServiceService {

    private final Logger logger = LoggerFactory.getLogger(AlphabetSoupServiceServiceImpl.class);
    private char[][] sopa;
    private Sopa sopaFinal = new Sopa();
    private ConfiguracionSoupDTO configuracionSoupDTO;
    private List<String> resultadoPalabras = new ArrayList<>();
    private List<String> palabrasEncontradas = new ArrayList<>();

    @Autowired
    private SoupRepository mongoRepository;
    @Autowired
    protected MongoTemplate mongoTemplate;
    @Autowired
    @Resource(name = "alphabetSoupCRUDServiceImpl")
    AlphabetSoupCRUDServiceImpl mongoCRUDService;

    public AlphabetSoupServiceServiceImpl() throws NoDocumentException, NoFieldIdException, IdTypeAndIdDocumentIncompatiblesException {
        super();
    }

    @Override
    protected MongoRepository<Sopa, String> getMongoRepository() {
        return mongoRepository;
    }

    public Sopa createSoup(ConfiguracionSoupDTO configuracionSoupDTO) throws Exception{
        sopa = new char[configuracionSoupDTO.getW()][configuracionSoupDTO.getH()];
        sopaFinal.setConfiguracionSoupDTO(configuracionSoupDTO);
        return createSopa(configuracionSoupDTO);
    }

    public Sopa createSopa(ConfiguracionSoupDTO configuracionSoupDTO){
        char [] abecedario = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','ñ','o','p','q','r','s','t','u',
                              'v','w','x','y','z'};
        String [] palabrasSopa = {"rastreo","satelital","inteligente","asistente","conduccion","transporte","controlar","optimizador","rutas","sitrack","seguridad","soluciones","inteligentes", "capitales","bienestar","programacion","argentina","brasil","chile","uruguay", "mexico"};

        while (7>resultadoPalabras.size()){
            int numRandomSel = (int) Math.round(Math.random() * 20);
            String seleccionada = palabrasSopa[numRandomSel];

            if (!resultadoPalabras.contains(seleccionada)) {
                resultadoPalabras.add(seleccionada);
            }
        }

        //TODO recorrer todos los seleccionados y agregar a la matriz de la sopa
        int inicioIzqADer = 0;
        for (int palabras=0;palabras<resultadoPalabras.size();palabras++){
            if (configuracionSoupDTO.isLtr()) {//palabras de izquerda a derrecha
                char sel[] = resultadoPalabras.get(0).toCharArray();
                int columna = (int) Math.round(Math.random() * (configuracionSoupDTO.getH()-1));
                int inicioMaximo = configuracionSoupDTO.getW() - (sel.length-1);
                int inicioReal = (int) Math.round(Math.random() * inicioMaximo);
//                for (int a = 0; a < sel.length; a++) {

                for (int a = inicioReal; a < (inicioReal+(sel.length-1)); a++) {
                    sopa[columna][a] = sel[inicioIzqADer];
                    inicioIzqADer= inicioIzqADer+1;
                }
            }

            if (configuracionSoupDTO.isRtl()){//palabras de derrecha a izquerda
//                char sel[] = resultadoPalabras.get(palabras).toCharArray();
//                int inicioMaximo = configuracionSoupDTO.getW() - sel.length;
//                int inicioReal = (int) Math.round(Math.random() * inicioMaximo);
//                for(int a=sel.length; a < inicioReal; a--) {
//                    sopa[0][a]=sel[a];
//                }
            }

            if (configuracionSoupDTO.isBtt()){//palabras de abajo hacia arriba

            }
            if (configuracionSoupDTO.isTtb()){//palabras de arriba hacia abajo

            }
            if (configuracionSoupDTO.isD()){//palabras diagonales

            }


        }


        for(int i=0; i < sopa.length; i++){
            for(int j=0; j < sopa[i].length; j++){
                int numRandom = (int) Math.round(Math.random() * 26);
                if (sopa[i][j] == 0) {
                    sopa[i][j] = abecedario[numRandom];
                }
                //System.out.print(" " + sopa[i][j] + " ");
            }

            //System.out.print("\r\n");
        }

        sopaFinal.setConfiguracionSoupDTO(configuracionSoupDTO);
        sopaFinal.setResultadoPalabras(resultadoPalabras);
        sopaFinal.setSopa(sopa);

        return sopaFinal;
    }

    public char[][] getSopa() {
        return sopa;
    }

    public void setSopa(char[][] nuevaSopa) {
        sopa = nuevaSopa;
    }

    public void mostrarSopa(){

        System.out.println("==== SOPA DE LETRAS ====");

        for(int i=0; i < sopa.length; i++){
            System.out.print("| ");
            for(int j=0; j < sopa[i].length; j++){
                System.out.print(" " + sopa[i][j] + " | ");
            }
            System.out.print("\r\n");
        }
    }

    public void resolver(Solucion solucion){

    }

    public String resolver(String palabra){

        for( int[] pos : posiblesSolucionesDe(palabra) ){

            // Buscar horizontalmente hacia derecha.
            String palabraEncontrada = palabraEnMatriz(pos, palabra.length(), 0, 1);
            if(palabraEncontrada.equals(palabra))
                return "palabra '"+ palabra +"' encontrada a partir de la posicion [" + pos[0] + "," + pos[1] + "] de la matriz con orientacion horizontal derecha";

            // Buscar horizontalmente hacia izquierda.
            palabraEncontrada = palabraEnMatriz(pos, palabra.length(), 0, -1);
            if(palabraEncontrada.equals(palabra))
                return "palabra '"+ palabra +"' encontrada a partir de la posicion [" + pos[0] + "," + pos[1] + "] de la matriz con orientacion horizontal izquierda";

            // Buscar verticalmente hacia abajo.
            palabraEncontrada = palabraEnMatriz(pos, palabra.length(), 1, 0);
            if(palabraEncontrada.equals(palabra))
                return "palabra '"+ palabra +"' encontrada a partir de la posici—n [" + pos[0] + "," + pos[1] + "] de la matriz con orientaci—n vertical hacia abajo";

            // Buscar verticalmente hacia arriba.
            palabraEncontrada = palabraEnMatriz(pos, palabra.length(), -1, 0);
            if(palabraEncontrada.equals(palabra))
                return "palabra '"+ palabra +"' encontrada a partir de la posici—n [" + pos[0] + "," + pos[1] + "] de la matriz con orientaci—n vertical hacia arriba";

            // Buscar diagonal superior derecha.
            palabraEncontrada = palabraEnMatriz(pos, palabra.length(), -1, 1);
            if(palabraEncontrada.equals(palabra))
                return "palabra '"+ palabra +"' encontrada a partir de la posici—n [" + pos[0] + "," + pos[1] + "] de la matriz con orientaci—n diagonal superior derecha";

            // Buscar diagonal superior izquierda.
            palabraEncontrada = palabraEnMatriz(pos, palabra.length(), -1, -1);
            if(palabraEncontrada.equals(palabra))
                return "palabra '"+ palabra +"' encontrada a partir de la posici—n [" + pos[0] + "," + pos[1] + "] de la matriz con orientaci—n diagonal superior izquierda";

            // Buscar diagonal inferior derecha.
            palabraEncontrada = palabraEnMatriz(pos, palabra.length(), 1, 1);
            if(palabraEncontrada.equals(palabra))
                return "palabra '"+ palabra +"' encontrada a partir de la posici—n [" + pos[0] + "," + pos[1] + "] de la matriz con orientaci—n diagonal inferior derecha";

            // Buscar diagonal inferior izquierda.
            palabraEncontrada = palabraEnMatriz(pos, palabra.length(), 1, -1);
            if(palabraEncontrada.equals(palabra))
                return "palabra '"+ palabra +"' encontrada a partir de la posici—n [" + pos[0] + "," + pos[1] + "] de la matriz con orientaci—n diagonal inferior izquierda";
        }

        return "La palabra '" +palabra+ "' no fue encontrada en la sopa de letras";
    }

    /*
     * Retorna indice invertido de las posiciones donde puede
     * resolverse una palabra buscada.
     */
    public int[][] posiblesSolucionesDe(String palabra) {
        char primeraLetra = palabra.charAt(0);
        List<int[]> indiceInvertido = new ArrayList<int[]>();

        for(int i=0; i < sopa.length; i++){
            for(int j=0; j < sopa[i].length; j++){
                if(sopa[i][j] == primeraLetra){
                    indiceInvertido.add(new int[]{i, j}); // Guardar la posicion de la letra en la matriz.
                }
            }
        }
        return toArrayInt(indiceInvertido);
    }

    /*
     * Transforma un objeto List a un multi arreglo
     * de nœmeros enteros.
     * @param list la lista a transformar.
     */
    private int[][] toArrayInt( List<int[]> list ){
        return (int[][]) list.toArray( new int[list.size()][list.get(0).length]);
    }

    /*
     * Algoritmo que busca palabras en la matriz de palabras de forma
     * recursiva usando la tŽcnica de backtracking.
     */
    public String palabraEnMatriz(int[] posInicial, int numeroCaracteres, int moverEnFila, int moverEnColumna) {
        String palabra = "";
        int recorrido = 0, fila = posInicial[0], columna = posInicial[1];

        while((recorrido < numeroCaracteres) &&
                (fila < sopa.length && columna < sopa.length) &&
                (fila > -1 && columna > -1)) {

            palabra += sopa[fila][columna];
            fila = fila + moverEnFila;
            columna = columna + moverEnColumna;
            recorrido++;
        }

        return palabra;
    }

//    public void getConnection(){
//        MongoClientURI connectionString = new MongoClientURI("mongodb://alphabetsoup:alphabetsoup@localhost:27017/alphabetsoup");
//        MongoClientURI connectionString = new MongoClientURI("mongodb://localhost:27017/alphabetsoup");
//        mongoClient = new MongoClient(connectionString);
//        MongoDatabase database = mongoClient.getDatabase("alphabetsoup");
//        bookmarksCollection = database.getCollection("alphabetsoup");
//
//    }

    public String insertaAlphabetSoup(){
//        Document sopaMongo = new Document();

//        logger.info(Utilitario.convertirAJson(sopa));

//        BasicDBObject document = new BasicDBObject();

//        sopaMongo.put("sopa", sopa);

//        sopaMongo = Document.parse(Utilitario.convertirAJson(sopa));
//        try {
//            bookmarksCollection.insertOne(sopaMongo);
//        }catch (Exception e){
//            e.printStackTrace();
//        }

//        MongoClient mongoClient;
        try {
//            Pojo pojo = new Pojo();
            //Sopa sopa = new Sopa();
//            mongoClient = new MongoClient("localhost", 27017);
//            MongoDatabase database = mongoClient.getDatabase("alphabetsoup");

            //sopa.setSopa(Utilitario.convertirAJson(sopa));
            IdResource id = null;
            Sopa c = new Sopa();
            c.setSopa(sopa);
            id = mongoCRUDService.crear(c,(SequenceService)null);
            
//            pojo.setId("1");
//            pojo.setName("ilkay");
//            pojo.setSurname("günel");

//            Document document = new Document();
//            document.put("sopa", sopa);

//            database.getCollection("alphabetsoup").insertOne(document);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Bir Hata Meydana Geldi!");
            System.out.println("Hata" + e);
        }

        return "";
    }

    @Override
    public ConfiguracionSoupDTO actualizar(String s, ConfiguracionSoupDTO dtoObject) throws ValidacionDeNegocioException, RecursoNoEncontradoException {
        return null;
    }

    /*
    public static void main(String[] args){
        SopaDeLetras sopaDeLetras = new SopaDeLetras();
        sopaDeLetras.mostrarMatriz();
        System.out.println(sopaDeLetras.resolver("agua"));
        System.out.println(sopaDeLetras.resolver("tierra"));
        System.out.println(sopaDeLetras.resolver("fuego"));
        System.out.println(sopaDeLetras.resolver("viento"));
    }
    */

}
