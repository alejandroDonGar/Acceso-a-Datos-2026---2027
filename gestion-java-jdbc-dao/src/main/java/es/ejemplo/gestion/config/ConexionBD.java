package es.ejemplo.gestion.config;

import es.ejemplo.gestion.exception.AplicationException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
    private String url;

    public ConexionBD(String url) {
        this.url = url;
    }

    public Connection obtenerConexion() {
        Path fichero = Path.of(url.replace("jdbc:sqlite:", ""));
        if (!Files.exists(fichero)) {
            throw new AplicationException("No existe la base de datos: " + fichero);
        }
        try {
            return DriverManager.getConnection(url);
        } catch (SQLException e) {
            throw new AplicationException("Error al conectarse con SQLite", e);
        }
    }
}
