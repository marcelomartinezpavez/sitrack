package cl.sitack.ws.alphabet.soup.mongo.crud.utils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class ArchivosUtils {
    public ArchivosUtils() {
    }

    public static boolean escribirFullArchivo(String contenido, String ruta, String fileName, boolean sobreescribir) throws IOException {
        File directorio = new File(ruta);
        if (!directorio.exists()) {
            directorio.mkdir();
        }

        Path file = Paths.get(ruta + "/" + fileName);
        if (!sobreescribir && Files.exists(file, new LinkOption[0])) {
            return false;
        } else {
            Files.write(file, contenido.getBytes(), new OpenOption[0]);
            return true;
        }
    }

    public static boolean escribirArchivo(List<String> lineas, String fileName, boolean sobreescribir) throws IOException {
        Path file = Paths.get(fileName);
        if (!sobreescribir && Files.exists(file, new LinkOption[0])) {
            return false;
        } else {
            Files.write(file, lineas, Charset.forName("UTF-8"));
            return true;
        }
    }

    public static String readFullFromJARFile(String filename) throws IOException {
        InputStream is = ArchivosUtils.class.getResourceAsStream(filename);
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        StringBuffer sb = new StringBuffer();

        String line;
        while((line = br.readLine()) != null) {
            sb.append(line);
            sb.append("\n");
        }

        br.close();
        isr.close();
        is.close();
        return sb.toString();
    }

    public static List<String> readFromJARFile(String filename) throws IOException {
        List<String> lineas = new ArrayList();
        InputStream is = ArchivosUtils.class.getResourceAsStream(filename);
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);

        String line;
        while((line = br.readLine()) != null) {
            lineas.add(line);
        }

        br.close();
        isr.close();
        is.close();
        return lineas;
    }
}
