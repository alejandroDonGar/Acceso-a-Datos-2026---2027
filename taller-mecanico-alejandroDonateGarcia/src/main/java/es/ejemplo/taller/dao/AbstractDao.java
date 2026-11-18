package es.ejemplo.taller.dao;

import es.ejemplo.taller.infra.ConexionBD;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DAO generico del taller. Tiene lo que se repite en los cinco DAO: listar, buscarPorId y eliminar
 * (solo cambian la tabla y la columna clave), ejecutar sentencias con parametros, recuperar el id
 * generado y registrar los errores. Cada DAO hijo pone su SQL de insercion/actualizacion,
 * sus validaciones y como se convierte una fila en objeto (mapear)
 * @param <T> entidad (Cliente, Vehiculo...)
 * @param <ID> tipo de la clave: Integer en casi todas las tablas, String (matricula) en vehiculos
 * @author AlejandroDonGar
 */
public abstract class AbstractDao<T, ID> extends ConexionBD {
    private static final Logger LOG = Logger.getLogger(AbstractDao.class.getName());

    private final String tabla;
    private final String columnaId;
    protected final String select;

    protected AbstractDao(String url, String tabla, String columnaId, String columnas) {
        super(url);
        this.tabla = tabla;
        this.columnaId = columnaId;
        // Los nombres de tabla y columnas son constantes del codigo, nunca datos del usuario
        this.select = "SELECT " + columnas + " FROM " + tabla;
    }

    /** Convierte la fila actual del ResultSet en un objeto de la entidad. */
    protected abstract T mapear(ResultSet rs) throws SQLException;

    public List<T> listar() {
        return consultarLista(select + " ORDER BY " + columnaId);
    }

    public Optional<T> buscarPorId(ID id) {
        if (id == null) return Optional.empty();
        List<T> encontrados = consultarLista(select + " WHERE " + columnaId + " = ?", id);
        return encontrados.isEmpty() ? Optional.empty() : Optional.of(encontrados.get(0));
    }

    public int eliminar(ID id) {
        if (id == null) return 0;
        // Con las claves foraneas activas, borrar un registro con hijos falla (RESTRICT) y devuelve 0
        return ejecutar("DELETE FROM " + tabla + " WHERE " + columnaId + " = ?", id);
    }

    /** Ejecuta una consulta con parametros y devuelve la lista (vacia si no hay filas o si falla). */
    protected List<T> consultarLista(String sql, Object... parametros) {
        List<T> lista = new ArrayList<>();
        try (Connection con = abrir(url);
             PreparedStatement ps = preparar(con, sql, parametros);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            registrar("consultar " + tabla, e);
            return new ArrayList<>();
        }
        return lista;
    }

    /** Ejecuta un UPDATE o DELETE; devuelve las filas afectadas o 0 si falla. */
    protected int ejecutar(String sql, Object... parametros) {
        try (Connection con = abrir(url);
             PreparedStatement ps = preparar(con, sql, parametros)) {
            return ps.executeUpdate();
        } catch (SQLException e) {
            registrar("modificar " + tabla, e);
            return 0;
        }
    }

    /** Ejecuta un INSERT en una tabla con id autogenerado y devuelve ese id, o 0 si falla. */
    protected int insertarDevolviendoId(String sql, Object... parametros) {
        try (Connection con = abrir(url);
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            asignar(ps, parametros);
            if (ps.executeUpdate() == 0) return 0;
            // No suponemos que el id sea 1: se lo pedimos a la base de datos
            try (ResultSet claves = ps.getGeneratedKeys()) {
                return claves.next() ? claves.getInt(1) : 0;
            }
        } catch (SQLException e) {
            registrar("insertar en " + tabla, e);
            return 0;
        }
    }

    private PreparedStatement preparar(Connection con, String sql, Object... parametros) throws SQLException {
        PreparedStatement ps = con.prepareStatement(sql);
        asignar(ps, parametros);
        return ps;
    }

    private void asignar(PreparedStatement ps, Object... parametros) throws SQLException {
        for (int i = 0; i < parametros.length; i++) {
            ps.setObject(i + 1, parametros[i]);
        }
    }

    // Solo se registra la operacion y el motivo, sin los datos personales del registro
    protected void registrar(String operacion, SQLException e) {
        LOG.log(Level.WARNING, "Fallo al " + operacion + ": " + e.getMessage());
    }

    /** NOT NULL no impide "" ni espacios: un texto obligatorio tiene que tener algo escrito. */
    protected static boolean texto(String valor) {
        return valor != null && !valor.isBlank();
    }

    /** Fecha con formato AAAA-MM-DD y que exista de verdad (2026-02-30 no vale). */
    protected static boolean fecha(String valor) {
        if (valor == null) return false;
        try {
            LocalDate.parse(valor);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
