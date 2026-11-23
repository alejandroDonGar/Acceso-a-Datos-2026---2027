package ies.puesto.peliculas.repository.jdbc;

import ies.puesto.peliculas.model.Pelicula;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pruebas del repositorio contra una copia temporal de {@code data/peliculas.sqlite3}
 * (cada prueba con su copia, así no dependen del orden y la base original no se toca).
 *
 * @author AlejandroDonGar
 */
class PeliculaJdbcRepositoryTest {

    @TempDir
    Path carpeta;

    private String url;
    private PeliculaJdbcRepository repository;

    @BeforeEach
    void copiarBase() throws Exception {
        Path copia = carpeta.resolve("peliculas.sqlite3");
        Files.copy(Path.of("data", "peliculas.sqlite3"), copia);
        url = "jdbc:sqlite:" + copia;
        repository = nuevoRepositorio();
    }

    // un repositorio nuevo = una conexión nueva, como al reiniciar la aplicación
    private PeliculaJdbcRepository nuevoRepositorio() {
        JdbcTemplate jdbc = new JdbcTemplate(new DriverManagerDataSource(url));
        return new PeliculaJdbcRepository(jdbc, new PeliculaRowMapper(), new PeliculaEntityMapper());
    }

    @Test
    void buscarTodasDevuelveLasDosDeEjemploOrdenadas() {
        var todas = repository.buscarTodas();
        assertEquals(2, todas.size());
        assertEquals("PEL001", todas.get(0).getIdentificador());
        assertEquals("PEL002", todas.get(1).getIdentificador());
    }

    @Test
    void buscarPorCodigoInexistenteDevuelveVacio() {
        assertTrue(repository.buscarPorCodigo("PEL999").isEmpty());
    }

    @Test
    void insertarDuplicadaDevuelveFalse() {
        assertFalse(repository.insertarSiAusente(new Pelicula("PEL001", "Otra", 2000, 90)));
    }

    @Test
    void actualizarInexistenteNoInserta() {
        assertFalse(repository.actualizarSiExiste(new Pelicula("PEL999", "Nueva", 2000, 90)));
        assertTrue(repository.buscarPorCodigo("PEL999").isEmpty());
    }

    @Test
    void actualizarCambiaLosDatos() {
        assertTrue(repository.actualizarSiExiste(new Pelicula("PEL002", "Totoro", 1988, 88)));
        Pelicula leida = repository.buscarPorCodigo("PEL002").orElseThrow();
        assertEquals("Totoro", leida.getNombre());
        assertEquals(88, leida.getMinutosProyeccion());
    }

    @Test
    void eliminarDevuelveTrueYLuegoFalse() {
        assertTrue(repository.eliminar("PEL001"));
        assertFalse(repository.eliminar("PEL001"));
    }

    @Test
    void losDatosSiguenTrasReiniciar() {
        assertTrue(repository.insertarSiAusente(new Pelicula("PEL003", "Ponyo", 2008, 101)));

        Optional<Pelicula> leida = nuevoRepositorio().buscarPorCodigo("PEL003");

        assertTrue(leida.isPresent());
        assertEquals("Ponyo", leida.get().getNombre());
        assertEquals(2008, leida.get().getAnioLanzamiento());
        assertEquals(101, leida.get().getMinutosProyeccion());
    }
}
