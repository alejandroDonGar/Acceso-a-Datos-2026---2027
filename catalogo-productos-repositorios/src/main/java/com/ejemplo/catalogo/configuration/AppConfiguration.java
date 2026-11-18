package com.ejemplo.catalogo.configuration;

import com.ejemplo.catalogo.model.Producto;
import com.ejemplo.catalogo.repository.IProductoRepository;
import com.ejemplo.catalogo.repository.file.JsonRepository;
import com.ejemplo.catalogo.repository.file.XmlRepository;
import com.ejemplo.catalogo.repository.file.csv.CsvProductoRepository;

import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/**
 * Lee data/app.properties y crea el repositorio de productos que indique
 * storage.format (csv, json o xml) sobre el fichero de storage.path.
 * El main hace un CRUD de prueba con ese repositorio
 * @author AlejandroDonGar
 */
public class AppConfiguration {

    public static Properties cargarPropiedades(Path path) {
        Properties properties = new Properties();
        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            properties.load(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return properties;
    }

    // El resto del programa solo conoce IProductoRepository:
    // cambiar de CSV a JSON o XML es cambiar una linea del .properties
    public static IProductoRepository crearRepositorio(Properties properties) {
        String formato = properties.getProperty("storage.format", "csv");
        Path ruta = Path.of(properties.getProperty("storage.path", "data/productos.csv"));
        if (formato.equals("csv")) {
            return new CsvProductoRepository(ruta);
        } else if (formato.equals("json")) {
            return new JsonRepository(ruta);
        } else if (formato.equals("xml")) {
            return new XmlRepository(ruta);
        }
        throw new IllegalArgumentException("Formato no soportado: " + formato);
    }

    public static void main(String[] args) {
        Properties properties = cargarPropiedades(Path.of("data", "app.properties"));
        System.out.println("Formato fichero: " + properties.getProperty("storage.format"));
        System.out.println("Ruta fichero: " + properties.getProperty("storage.path", "Valor por defecto"));

        IProductoRepository repository = crearRepositorio(properties);
        System.out.println("Productos: " + repository.findAll());

        // CRUD de prueba con un id que no esta en el fichero; al final se borra y el fichero queda igual
        System.out.println("create: " + repository.create(new Producto(99L, "Alfombrilla", 9.95)));
        System.out.println("create repetido: " + repository.create(new Producto(99L, "Otra", 1.0)));
        System.out.println("findById(99): " + repository.findById(99L).orElse(null));
        System.out.println("update: " + repository.update(new Producto(99L, "Alfombrilla XL", 14.95)));
        System.out.println("findById(99): " + repository.findById(99L).orElse(null));
        System.out.println("delete: " + repository.delete(99L));
        System.out.println("Productos: " + repository.findAll());
    }
}
