package ies.puesto.peliculas.service.mapper;

import ies.puesto.peliculas.model.Pelicula;
import ies.puesto.peliculas.service.modelo.DatosCreacionPelicula;
import ies.puesto.peliculas.service.modelo.DatosEdicionPelicula;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Comprueba que {@link PeliculaDominioMapper} conserva los cuatro valores y que,
 * en la edición, el identificador es el que llega aparte (el de la URL).
 *
 * @author AlejandroDonGar
 */
class PeliculaDominioMapperTest {

    private final PeliculaDominioMapper mapper = new PeliculaDominioMapper();

    @Test
    void aPeliculaDesdeCreacionConservaLosCuatroValores() {
        Pelicula pelicula = mapper.aPelicula(new DatosCreacionPelicula("PEL003", "Ponyo", 2008, 101));
        assertEquals("PEL003", pelicula.getIdentificador());
        assertEquals("Ponyo", pelicula.getNombre());
        assertEquals(2008, pelicula.getAnioLanzamiento());
        assertEquals(101, pelicula.getMinutosProyeccion());
    }

    @Test
    void aPeliculaDesdeEdicionUsaElCodigoDeLaUrl() {
        Pelicula pelicula = mapper.aPelicula("PEL001", new DatosEdicionPelicula("Ponyo", 2008, 101));
        assertEquals("PEL001", pelicula.getIdentificador());
        assertEquals("Ponyo", pelicula.getNombre());
        assertEquals(2008, pelicula.getAnioLanzamiento());
        assertEquals(101, pelicula.getMinutosProyeccion());
    }
}
