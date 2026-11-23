package ies.puesto.peliculas.repository.jdbc;

import ies.puesto.peliculas.model.Pelicula;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Pruebas de {@link PeliculaEntityMapper} y {@link PeliculaRowMapper}. Están en el mismo
 * paquete porque las dos clases (y la entidad) no son públicas.
 *
 * @author AlejandroDonGar
 */
class PeliculaMappersJdbcTest {

    private final PeliculaEntityMapper entityMapper = new PeliculaEntityMapper();

    @Test
    void aEntityConservaLosCuatroValores() {
        PeliculaEntity entity = entityMapper.aEntity(new Pelicula("PEL003", "Ponyo", 2008, 101));
        assertEquals("PEL003", entity.getCodigoFila());
        assertEquals("Ponyo", entity.getTituloFila());
        assertEquals(2008, entity.getAnioFila());
        assertEquals(101, entity.getMinutosFila());
    }

    @Test
    void aPeliculaConservaLosCuatroValores() {
        Pelicula pelicula = entityMapper.aPelicula(new PeliculaEntity("PEL003", "Ponyo", 2008, 101));
        assertEquals("PEL003", pelicula.getIdentificador());
        assertEquals("Ponyo", pelicula.getNombre());
        assertEquals(2008, pelicula.getAnioLanzamiento());
        assertEquals(101, pelicula.getMinutosProyeccion());
    }

    @Test
    void rowMapperLeeLasColumnasPorNombre() throws Exception {
        // ResultSet falso con Mockito: devuelve un valor por columna
        ResultSet rs = mock(ResultSet.class);
        when(rs.getString("pelicula_codigo")).thenReturn("PEL003");
        when(rs.getString("titulo_texto")).thenReturn("Ponyo");
        when(rs.getInt("estreno_anio")).thenReturn(2008);
        when(rs.getInt("duracion_minutos")).thenReturn(101);

        PeliculaEntity entity = new PeliculaRowMapper().mapRow(rs, 0);

        assertEquals("PEL003", entity.getCodigoFila());
        assertEquals("Ponyo", entity.getTituloFila());
        assertEquals(2008, entity.getAnioFila());
        assertEquals(101, entity.getMinutosFila());
    }
}
