package aula.tareas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada de la API de tareas: arranca Spring Boot y su Tomcat embebido.
 *
 * @author AlejandroDonGar
 */
@SpringBootApplication
public class TareasApplication {
    public static void main(String[] args) {
        SpringApplication.run(TareasApplication.class, args);
    }
}
