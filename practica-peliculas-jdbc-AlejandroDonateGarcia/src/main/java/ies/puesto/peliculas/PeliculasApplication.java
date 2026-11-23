package ies.puesto.peliculas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada de la API de películas. Está en el paquete raíz
 * {@code ies.puesto.peliculas} para que Spring encuentre todos los componentes de debajo.
 *
 * @author AlejandroDonGar
 */
@SpringBootApplication
public class PeliculasApplication {
    public static void main(String[] args) {
        SpringApplication.run(PeliculasApplication.class, args);
    }
}
