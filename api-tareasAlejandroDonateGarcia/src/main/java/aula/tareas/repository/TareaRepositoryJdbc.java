package aula.tareas.repository;

import aula.tareas.api.TareaRequest;
import org.springframework.stereotype.Repository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio que guarda las tareas en la tabla {@code tareas_jdbc} de
 * {@code data/tareas.sqlite3} con JDBC puro ({@code PreparedStatement} y try-with-resources).
 *
 * @author AlejandroDonGar
 */
@Repository("repositoryJdbc")
public class TareaRepositoryJdbc implements ITareaRepository {

    private final String jdbcUrl;

    public TareaRepositoryJdbc() {
        this("jdbc:sqlite:data/tareas.sqlite3");
    }

    public TareaRepositoryJdbc(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
        if (!validarUrl(jdbcUrl)) {
            throw new RuntimeException("No se encuentra la bbdd");
        }
    }

    private boolean validarUrl(String jdbcUrl) {
        String url = jdbcUrl.replace("jdbc:sqlite:", "");
        Path path = Path.of(url);
        return Files.exists(path);
    }

    @Override
    public TareaRequest create(String titulo) {
        // RETURNING id: el INSERT devuelve el id que genera SQLite (AUTOINCREMENT)
        // como si fuera un SELECT, por eso se usa executeQuery en vez de executeUpdate
        String sql = "insert into tareas_jdbc (titulo) values (?) returning id";
        try (Connection c = DriverManager.getConnection(jdbcUrl);
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, titulo.strip());
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return new TareaRequest(rs.getLong("id"), titulo.strip());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(long identificador) {
        String sql = "delete from tareas_jdbc where id = ?";
        try (Connection c = DriverManager.getConnection(jdbcUrl);
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, identificador);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<TareaRequest> findAll() {
        String sql = "select id, titulo from tareas_jdbc order by id";
        List<TareaRequest> tareas = new ArrayList<>();
        try (Connection c = DriverManager.getConnection(jdbcUrl);
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                tareas.add(new TareaRequest(
                        rs.getLong("id"),
                        rs.getString("titulo")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return tareas;
    }

    @Override
    public TareaRequest findById(long identificador) {
        String sql = "select id, titulo from tareas_jdbc where id = ?";
        try (Connection c = DriverManager.getConnection(jdbcUrl);
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, identificador);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new TareaRequest(rs.getLong("id"), rs.getString("titulo"));
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public TareaRequest update(TareaRequest tarea) {
        String sql = "update tareas_jdbc set titulo = ? where id = ?";
        try (Connection c = DriverManager.getConnection(jdbcUrl);
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, tarea.titulo().strip());
            ps.setLong(2, tarea.id());
            // 0 filas afectadas = no existe ninguna tarea con ese id
            if (ps.executeUpdate() == 0) {
                return null;
            }
            return new TareaRequest(tarea.id(), tarea.titulo().strip());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
