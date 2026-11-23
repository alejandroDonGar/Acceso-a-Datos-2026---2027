package aula.tareas.repository;

import aula.tareas.api.TareaRequest;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio que guarda las tareas en el fichero {@code data/tarea.csv}
 * (cabecera {@code id,titulo}) con Apache Commons CSV.
 * Cada operación lee el fichero entero y, si cambia algo, lo vuelve a escribir.
 *
 * @author AlejandroDonGar
 */
@Repository("repositoryCsv")
public class TareaRepositoryCsv implements ITareaRepository {
    private final Path path;
    private final CSVFormat inputFormat = CSVFormat.DEFAULT.builder()
            .setHeader()
            .setSkipHeaderRecord(true).build();
    private final CSVFormat outputFormat = CSVFormat.DEFAULT.builder()
            .setHeader("id", "titulo")
            .build();

    public TareaRepositoryCsv() {
        path = Path.of("data", "tarea.csv");
    }

    @Override
    public TareaRequest create(String titulo) {
        List<TareaRequest> tareas = new ArrayList<>(findAll());
        TareaRequest tarea = new TareaRequest(siguienteId(tareas), titulo.strip());
        tareas.add(tarea);
        saveAll(tareas);
        return tarea;
    }

    @Override
    public boolean delete(long identificador) {
        List<TareaRequest> tareas = new ArrayList<>(findAll());
        boolean borrada = tareas.removeIf(tarea -> tarea.id() == identificador);
        if (borrada) {
            saveAll(tareas);
        }
        return borrada;
    }

    @Override
    public List<TareaRequest> findAll() {
        if (Files.notExists(path)) {
            return new ArrayList<>();
        }
        List<TareaRequest> result = new ArrayList<>();
        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
             CSVParser parser = inputFormat.parse(reader)) {
            for (CSVRecord row : parser) {
                result.add(new TareaRequest(
                        Long.parseLong(row.get("id")),
                        row.get("titulo")));
            }
        } catch (IOException e) {
            // si no se puede leer, mejor fallar que devolver una lista vacía
            // y que el siguiente create sobrescriba el fichero
            throw new RuntimeException("No se puede leer " + path, e);
        }
        return result;
    }

    @Override
    public TareaRequest findById(long identificador) {
        for (TareaRequest tarea : findAll()) {
            if (tarea.id() == identificador) {
                return tarea;
            }
        }
        return null;
    }

    @Override
    public TareaRequest update(TareaRequest tarea) {
        List<TareaRequest> tareas = new ArrayList<>(findAll());
        for (int i = 0; i < tareas.size(); i++) {
            if (tareas.get(i).id() == tarea.id()) {
                TareaRequest actualizada = new TareaRequest(tarea.id(), tarea.titulo().strip());
                tareas.set(i, actualizada);
                saveAll(tareas);
                return actualizada;
            }
        }
        return null;
    }

    // el siguiente id es el mayor que haya + 1 (1 si el fichero está vacío)
    private long siguienteId(List<TareaRequest> tareas) {
        long max = 0;
        for (TareaRequest tarea : tareas) {
            max = Math.max(max, tarea.id());
        }
        return max + 1;
    }

    private void saveAll(List<TareaRequest> tareas) {
        try {
            Files.createDirectories(path.getParent());
            try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8);
                 CSVPrinter printer = outputFormat.print(writer)) {
                for (TareaRequest tarea : tareas) {
                    printer.printRecord(tarea.id(), tarea.titulo());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("No se puede escribir " + path, e);
        }
    }
}
