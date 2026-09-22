package ies.puerto;
/**
 * Clase que lee una direccion
 * @author AlejandroDonGar
 */
import java.io.File;
import java.net.URL;
import java.nio.file.Path;

public class LectorRecursos {
    public static void main(String[] args) {
        Path path = Path.of("resources", "archivo.txt");
        String ruta = path.toAbsolutePath().toString();
        System.out.println("La ruta del fichero es: " + ruta);
        File file = new File(ruta);
        if(file.exists()) {
            System.out.printf("El fichero existe");
        } else {
            System.out.printf("El fichero no existe");
        }
        URL url LectorRecursos.class.getClassLoader().getResouce("archivo.txt");
        System.out.println("Path dentro de resources");
        Files.readAllLines(file)
    }
}