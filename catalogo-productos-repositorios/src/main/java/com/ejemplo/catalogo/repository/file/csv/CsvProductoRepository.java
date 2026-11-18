package com.ejemplo.catalogo.repository.file.csv;

import com.ejemplo.catalogo.model.Producto;
import com.ejemplo.catalogo.repository.IProductoRepository;
import com.ejemplo.catalogo.repository.file.AbstractFileRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio de productos guardados en CSV (cabecera id,nombre,precio) con Apache Commons CSV
 * @author AlejandroDonGar
 */
public class CsvProductoRepository extends AbstractFileRepository<Producto, Long> implements IProductoRepository {

    private final CSVFormat inputFormat = CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).get();
    private final CSVFormat outputFormat = CSVFormat.DEFAULT.builder().setHeader("id", "nombre", "precio").get();

    public CsvProductoRepository(Path path) {
        super(path);
    }

    @Override
    protected List<Producto> readAll() {
        List<Producto> productos = new ArrayList<>();
        try (Reader reader = Files.newBufferedReader(getPath(), StandardCharsets.UTF_8);
             CSVParser parser = inputFormat.parse(reader)) {
            for (CSVRecord row : parser) {
                productos.add(new Producto(
                        Long.parseLong(row.get("id")),
                        row.get("nombre"),
                        Double.parseDouble(row.get("precio"))));
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return productos;
    }

    @Override
    protected void writeAll(List<Producto> elements) {
        try (Writer writer = Files.newBufferedWriter(getPath(), StandardCharsets.UTF_8);
             CSVPrinter printer = new CSVPrinter(writer, outputFormat)) {
            for (Producto p : elements) printer.printRecord(p.id(), p.nombre(), p.precio());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
