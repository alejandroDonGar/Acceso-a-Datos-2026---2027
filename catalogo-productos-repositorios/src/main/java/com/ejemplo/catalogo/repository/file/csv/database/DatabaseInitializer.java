package com.ejemplo.catalogo.repository.file.csv.database;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class DatabaseInitializer {

    String url;
    Path path;

    public DatabaseInitializer(String url) {
        if(url == null || url.isBlank()) {
            url = "data/app.db";
        }
        path = Path.of(url);
        if(!Files.exists(path)) {
            try {
                Files.createFile(path);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void createSchema(String url) {
        String sql = """
            CREATE TABLE IF NOT EXISTS producto (
                id INTEGER PRIMARY KEY,
                nombre TEXT NOT NULL,
                precio REAL NOT NULL
            )
            """;

        try (Connection connection = DriverManager.getConnection(url);
             Statement sentencia = connection.createStatement()) {
            sentencia.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Error creando esquema", e);
        }
    }
}
