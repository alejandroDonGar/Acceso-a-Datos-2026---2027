package ies.puerto;
/**
 * Clase que lee una direccion
 * @author AlejandroDonGar
 */
import org.apache.commons.csv.CSVFormat;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;

public class LectorRecursos  extends FicheroImpl{
    public static void main(String[] args) {
        Path path = Path.of("resources", "csv.txt");
        String ruta = path.toAbsolutePath().toString();
        System.out.println("La ruta del fichero es: " + ruta);
        File file = new File(ruta);

        if(file.exists()) {
            System.out.printf("El fichero existe");
        } else {
            System.out.printf("El fichero no existe");
        }
        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)){
            String linea = reader.readLine();
            String[] valores = linea.split(",");
            System.out.println(valores[1]);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Reader in = null;
        try {
            in = new FileReader(path.toAbsolutePath().toString());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setHeader(HEADERS)
                .setSkipHeaderRecord(true)
                .build();

        Iterable<CSVFormat> records = csvFormat.parse(in);
    }
}