package ies.puesto.peliculas.controller.mapper;

import ies.puesto.peliculas.controller.api.PeliculaEdicionRequest;
import ies.puesto.peliculas.controller.api.PeliculaRequest;
import ies.puesto.peliculas.controller.api.PeliculaResponse;
import ies.puesto.peliculas.model.Pelicula;
import ies.puesto.peliculas.service.modelo.DatosCreacionPelicula;
import ies.puesto.peliculas.service.modelo.DatosEdicionPelicula;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Comprueba que {@link PeliculaApiMapper} conserva los cuatro valores en cada conversión.
 *
 * @author AlejandroDonGar
 */
class PeliculaApiMapperTest {

    private final PeliculaApiMapper mapper = new PeliculaApiMapper();

    @Test
    void aDatosCreacionConservaLosCuatroValores() {
        DatosCreacionPelicula datos = mapper.aDatosCreacion(new PeliculaRequest("PEL003", "Ponyo", 2008, 101));
        assertEquals("PEL003", datos.referenciaSolicitada());
        assertEquals("Ponyo", datos.nombreSolicitado());
        assertEquals(2008, datos.anioSolicitado());
        assertEquals(101, datos.minutosSolicitados());
    }

    @Test
    void aDatosEdicionConservaLosTresValores() {
        DatosEdicionPelicula datos = mapper.aDatosEdicion(new PeliculaEdicionRequest("Ponyo", 2008, 101));
        assertEquals("Ponyo", datos.nombreSolicitado());
        assertEquals(2008, datos.anioSolicitado());
        assertEquals(101, datos.minutosSolicitados());
    }

    @Test
    void aRespuestaConservaLosCuatroValores() {
        PeliculaResponse respuesta = mapper.aRespuesta(new Pelicula("PEL003", "Ponyo", 2008, 101));
        assertEquals("PEL003", respuesta.codigo());
        assertEquals("Ponyo", respuesta.titulo());
        assertEquals(2008, respuesta.anioEstreno());
        assertEquals(101, respuesta.duracionMinutos());
    }

    @Test
    void aRespuestasConvierteTodaLaLista() {
        List<PeliculaResponse> respuestas = mapper.aRespuestas(List.of(
                new Pelicula("PEL001", "A", 2001, 1), new Pelicula("PEL002", "B", 2002, 2)));
        assertEquals(2, respuestas.size());
        assertEquals("PEL002", respuestas.get(1).codigo());
    }
}
