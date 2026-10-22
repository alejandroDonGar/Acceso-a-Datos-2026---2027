package es.ejemplo.taller.infra;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
    protected final String url;
    public ConexionBD(String url) {
        this.url = url;
        Path path = Path.of(url);
        if(!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    /**
     * TODO: verificar el archivo antes de conectar y comprobar las tablas.
     * Devuelve una conexión abierta cuya responsabilidad de cierre tiene el llamador.
     * Si la ruta, el archivo o el esquema no son válidos, lanza SQLException.
     */
    public static Connection abrir(String url) {
        try {
            return DriverManager.getConnection(url);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
