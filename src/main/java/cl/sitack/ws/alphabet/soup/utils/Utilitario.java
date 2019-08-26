package cl.sitack.ws.alphabet.soup.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;

public class Utilitario {


    public static String convertirAJson(Object object){
        if(object==null){
            return "null";
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            String m= mapper.writeValueAsString(object);
            return m;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object jsonToObject(String json, Class nameClass){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, nameClass);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <Source, Target> Target convertirObjeto(Source source, Class<Target> target) {
        if (source == null) {
            return null;
        } else {
            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            mapper.registerModule(new JavaTimeModule());
            return mapper.convertValue(source, target);
        }
    }

}
