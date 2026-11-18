package com.ejemplo.catalogo.repository;

import com.ejemplo.catalogo.model.Producto;
import com.ejemplo.catalogo.repository.file.JsonRepository;
import com.ejemplo.catalogo.repository.file.XmlRepository;
import com.ejemplo.catalogo.repository.file.csv.CsvProductoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pasa el mismo CRUD por las tres implementaciones (CSV, JSON y XML) sobre ficheros
 * temporales: si las tres cumplen IProductoRepository, deben comportarse igual
 * @author AlejandroDonGar
 */
class ProductoRepositoryTest {

    @TempDir
    Path carpeta;

    @Test
    void csvCumpleElContrato() {
        probarCrud(carpeta.resolve("productos.csv"), CsvProductoRepository::new);
    }

    @Test
    void jsonCumpleElContrato() {
        probarCrud(carpeta.resolve("productos.json"), JsonRepository::new);
    }

    @Test
    void xmlCumpleElContrato() {
        probarCrud(carpeta.resolve("productos.xml"), XmlRepository::new);
    }

    private void probarCrud(Path fichero, Function<Path, IProductoRepository> crear) {
        IProductoRepository repo = crear.apply(fichero);
        assertEquals(List.of(), repo.findAll(), "un fichero nuevo empieza vacio");

        // 200 es mayor que 127: comprueba que los Long se comparan con equals y no con ==
        assertTrue(repo.create(new Producto(200L, "Teclado", 49.99)));
        assertTrue(repo.create(new Producto(2L, "Monitor, 27 pulgadas", 219.90)));
        assertFalse(repo.create(new Producto(200L, "Repetido", 1.0)), "id duplicado");
        assertFalse(repo.create(null));
        assertFalse(repo.create(new Producto(null, "Sin id", 1.0)));

        assertEquals("Teclado", repo.findById(200L).orElseThrow().nombre());
        assertTrue(repo.findById(999L).isEmpty());
        assertTrue(repo.findById(null).isEmpty());

        assertTrue(repo.update(new Producto(200L, "Teclado mecanico", 79.90)));
        assertFalse(repo.update(new Producto(999L, "No existe", 1.0)));

        assertTrue(repo.delete(2L));
        assertFalse(repo.delete(2L));

        // Un repositorio nuevo sobre el mismo fichero lee lo que se guardo
        IProductoRepository otro = crear.apply(fichero);
        assertEquals(List.of(new Producto(200L, "Teclado mecanico", 79.90)), otro.findAll());
    }
}
