package es.ejemplo.taller.infra;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Unico punto donde se abren las conexiones SQLite: comprueba el archivo antes de conectar,
 * activa las claves foraneas en cada conexion y verifica que estan las cinco tablas del taller
 * @author AlejandroDonGar
 */
public class ConexionBD {
    private static final String PREFIJO = "jdbc:sqlite:";
    protected String url;

    public ConexionBD(String url) {
        this.url = url;
    }

    public static Connection abrir(String url) throws SQLException {
        if (url == null || !url.startsWith(PREFIJO)) {
            throw new SQLException("URL no valida: debe empezar por " + PREFIJO);
        }
        // SQLite crea una base vacia si la ruta no existe: por eso lo comprobamos antes de llamar a JDBC
        Path ruta = Path.of(url.substring(PREFIJO.length()));
        if (!Files.isRegularFile(ruta) || !Files.isReadable(ruta)) {
            throw new SQLException("No existe o no se puede leer la base de datos: " + ruta.getFileName());
        }
        Connection conexion = DriverManager.getConnection(url);
        try (Statement st = conexion.createStatement()) {
            // ON DELETE RESTRICT solo funciona si se activa en CADA conexion
            st.execute("PRAGMA foreign_keys = ON");
            // Si el archivo no es SQLite, esta consulta lanza SQLException
            try (ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM sqlite_master WHERE type='table' AND name IN "
                    + "('clientes','vehiculos','mecanicos','reparaciones','piezas_reparacion')")) {
                if (!rs.next() || rs.getInt(1) != 5) {
                    throw new SQLException("La base no tiene las cinco tablas del taller: " + ruta.getFileName());
                }
            }
            return conexion;
        } catch (SQLException e) {
            // Cerramos la conexion antes de lanzar el error para no dejar el archivo bloqueado
            conexion.close();
            throw e;
        }
    }
}
