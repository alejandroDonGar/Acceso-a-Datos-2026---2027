package com.ejemplo.catalogo.configuration;

import com.ejemplo.catalogo.repository.IProducutoRepository;
import com.ejemplo.catalogo.repository.file.csv.JsonRepository;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class AppConfiguration {
    private static Path path;
    private static Properties properties;
    static IProducutoRepository repository;

    public static void main(String[] args) {
        path = Path.of("data","app.properties");
        properties = new Properties();
        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            properties.load(reader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Formato fichero: " +properties.getProperty("storage.format"));
        System.out.println("Ruta fichero: " +properties.getProperty("storage.fichero", "Valor por defecto"));

        properties.setProperty("app.name", "FileLab");
        try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            properties.store(writer, "Configuración de la aplicación");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if(properties.getProperty("storage.format").equals("json")) {
            repository = new JsonRepository(Path.of(properties.getProperty("storage.path")));
        }
    }
}