package ies.puesto.peliculas.repository.jdbc;

import ies.puesto.peliculas.model.Pelicula;
import ies.puesto.peliculas.repository.PeliculaRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementación JDBC del contrato con {@link JdbcTemplate} sobre {@code data/peliculas.sqlite3}.
 * Solo existe con el perfil {@code jdbc}.
 *
 * @author AlejandroDonGar
 */
@Repository
@Profile("jdbc")
public class PeliculaJdbcRepository implements PeliculaRepository {

    private static final String SELECT =
            "SELECT pelicula_codigo, titulo_texto, estreno_anio, duracion_minutos FROM peliculas";

    private final JdbcTemplate jdbc;
    private final PeliculaRowMapper rowMapper;
    private final PeliculaEntityMapper entityMapper;

    PeliculaJdbcRepository(JdbcTemplate jdbc, PeliculaRowMapper rowMapper, PeliculaEntityMapper entityMapper) {
        this.jdbc = jdbc;
        this.rowMapper = rowMapper;
        this.entityMapper = entityMapper;
    }

    @Override
    public List<Pelicula> buscarTodas() {
        List<Pelicula> peliculas = new ArrayList<>();
        for (PeliculaEntity entity : jdbc.query(SELECT + " ORDER BY pelicula_codigo", rowMapper)) {
            peliculas.add(entityMapper.aPelicula(entity));
        }
        return peliculas;
    }

    @Override
    public Optional<Pelicula> buscarPorCodigo(String identificador) {
        // query + findFirst en vez de queryForObject, que lanza excepción si no hay fila
        return jdbc.query(SELECT + " WHERE pelicula_codigo = ?", rowMapper, identificador)
                .stream()
                .findFirst()
                .map(entityMapper::aPelicula);
    }

    @Override
    public boolean insertarSiAusente(Pelicula pelicula) {
        PeliculaEntity entity = entityMapper.aEntity(pelicula);
        // ON CONFLICT ... DO NOTHING: si el código ya existe no inserta y devuelve 0 filas
        int filas = jdbc.update(
                "INSERT INTO peliculas (pelicula_codigo, titulo_texto, estreno_anio, duracion_minutos) "
                        + "VALUES (?, ?, ?, ?) ON CONFLICT(pelicula_codigo) DO NOTHING",
                entity.getCodigoFila(), entity.getTituloFila(), entity.getAnioFila(), entity.getMinutosFila());
        return filas == 1;
    }

    @Override
    public boolean actualizarSiExiste(Pelicula pelicula) {
        PeliculaEntity entity = entityMapper.aEntity(pelicula);
        // el código va el último porque es el ? del WHERE
        int filas = jdbc.update(
                "UPDATE peliculas SET titulo_texto = ?, estreno_anio = ?, duracion_minutos = ? "
                        + "WHERE pelicula_codigo = ?",
                entity.getTituloFila(), entity.getAnioFila(), entity.getMinutosFila(), entity.getCodigoFila());
        return filas == 1;
    }

    @Override
    public boolean eliminar(String identificador) {
        return jdbc.update("DELETE FROM peliculas WHERE pelicula_codigo = ?", identificador) == 1;
    }
}
