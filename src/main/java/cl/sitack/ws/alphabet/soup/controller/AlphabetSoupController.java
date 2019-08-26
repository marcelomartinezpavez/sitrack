package cl.sitack.ws.alphabet.soup.controller;

import cl.sitack.ws.alphabet.soup.dto.ConfiguracionSoupDTO;
import cl.sitack.ws.alphabet.soup.dto.IdResponseDTO;
import cl.sitack.ws.alphabet.soup.mongo.crud.resource.IdResource;
import cl.sitack.ws.alphabet.soup.resource.Solucion;
import cl.sitack.ws.alphabet.soup.resource.Sopa;
import cl.sitack.ws.alphabet.soup.services.AlphabetSoupCRUDServiceImpl;
import cl.sitack.ws.alphabet.soup.services.AlphabetSoupServiceService;
import cl.sitack.ws.alphabet.soup.utils.Utilitario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.ws.rs.PathParam;
import java.util.List;

@RestController
public class AlphabetSoupController {

    private static final Logger logger = LoggerFactory.getLogger(AlphabetSoupController.class);

    @Autowired
    @Resource(name = "alphabet-soup")
    AlphabetSoupServiceService alphabetSoupService;

    @Autowired
    @Resource(name = "alphabetSoupCRUDServiceImpl")
    AlphabetSoupCRUDServiceImpl mongoCRUDService;


    @PostMapping(value = "/alphabetSoup", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<IdResponseDTO> crearSoup(@RequestBody ConfiguracionSoupDTO configuracionSoupDTO) throws Exception {
        logger.info("Creando sopa");
        Sopa sopa = alphabetSoupService.createSoup(configuracionSoupDTO);
        alphabetSoupService.mostrarSopa();
        String respuesta = alphabetSoupService.resolver(sopa.getResultadoPalabras().get(0));
        System.out.println("respuesta: "+respuesta);
//        Sopa s = new Sopa();
//        s.setSopa(Utilitario.convertirAJson(sopa));
        IdResource id = null;

        try {
            id =mongoCRUDService.crear(sopa);
        }catch (Exception e){
            System.out.println("NO SE CREO!!!!");
            e.printStackTrace();
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(id, HttpStatus.CREATED);

    }

    @GetMapping(value = "/alphabetSoup/list/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<IdResponseDTO> getSolutionSoup(@PathVariable("id") String id) throws Exception {
        logger.info("Obteniendo palabras solucion de la sopa");
        try {
            Sopa sopa = mongoCRUDService.obtenerPorId(id);
            return new ResponseEntity(Utilitario.convertirAJson(sopa.getResultadoPalabras()), HttpStatus.OK);
        }catch (Exception e){
            System.out.println("NO SE CREO!!!!");
            e.printStackTrace();
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping(value = "/alphabetSoup/view/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<IdResponseDTO> getSoup(@PathVariable("id") String id) throws Exception {
        logger.info("Obteniendo palabras solucion de la sopa");
        try {
            Sopa sopa = mongoCRUDService.obtenerPorId(id);
            //TODO dar formato a la sopa para que se muestre
            return new ResponseEntity(Utilitario.convertirAJson(sopa.getSopa()), HttpStatus.OK);
        }catch (Exception e){
            System.out.println("NO SE CREO!!!!");
            e.printStackTrace();
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }

    }


    @PutMapping(value = "/alphabetSoup/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<IdResponseDTO> uodateSoup(@PathVariable("id") String id, @RequestBody Solucion solucion) throws Exception {
        logger.info("Obteniendo palabras solucion de la sopa");
        try {
            Sopa sopa = mongoCRUDService.obtenerPorId(id);
            alphabetSoupService.resolver(solucion);
            //TODO buscar solucion y modificar sopa con mayusculas si se encuentra la palabra
            mongoCRUDService.actualizar(id, sopa);
            //TODO dar formato a la sopa para que se muestre
            return new ResponseEntity(Utilitario.convertirAJson(sopa.getSopa()), HttpStatus.OK);
        }catch (Exception e){
            System.out.println("NO SE CREO!!!!");
            e.printStackTrace();
            return new ResponseEntity(e.getMessage(),HttpStatus.NOT_FOUND);
        }

    }

}
