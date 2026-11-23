package aula.tareas.repository;

import aula.tareas.api.TareaRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio que guarda las tareas en el fichero {@code data/tarea.json} con Jackson.
 * Carga la lista al arrancar, trabaja en memoria y reescribe el fichero en cada cambio.
 *
 * @author AlejandroDonGar
 */
@Repository("repositoryJson")
public class TareaRepositoryJson implements ITareaRepository {
    private final Path path;
    private final ObjectMapper mapper = new ObjectMapper();
    private final List<TareaRequest> lista;

    public TareaRepositoryJson() {
        this("data/tarea.json");
    }

    public TareaRepositoryJson(String ruta) {
        path = Path.of(ruta);
        lista = getAll();
    }

    @Override
    public TareaRequest create(String titulo) {
        var tarea = new TareaRequest(siguienteId(), titulo.strip());
        lista.add(tarea);
        saveAll(lista);
        return tarea;
    }

    @Override
    public boolean delete(long identificador) {
        boolean borrada = lista.removeIf(tarea -> tarea.id() == identificador);
        if (borrada) {
            saveAll(lista);
        }
        return borrada;
    }

    @Override
    public List<TareaRequest> findAll() {
        return List.copyOf(lista);
    }

    @Override
    public TareaRequest findById(long identificador) {
        for (TareaRequest tarea : lista) {
            if (tarea.id() == identificador) {
                return tarea;
            }
        }
        return null;
    }

    @Override
    public TareaRequest update(TareaRequest tarea) {
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).id() == tarea.id()) {
                TareaRequest actualizada = new TareaRequest(tarea.id(), tarea.titulo().strip());
                lista.set(i, actualizada);
                saveAll(lista);
                return actualizada;
            }
        }
        return null;
    }

    // el siguiente id es el mayor que haya + 1, así no se repiten ids al reiniciar
    private long siguienteId() {
        long max = 0;
        for (TareaRequest tarea : lista) {
            max = Math.max(max, tarea.id());
        }
        return max + 1;
    }

    private void saveAll(List<TareaRequest> items) {
        try {
            Path parent = path.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            mapper.writerWithDefaultPrettyPrinter().writeValue(path.toFile(), items);
        } catch (IOException e) {
            throw new RuntimeException("No se puede escribir " + path, e);
        }
    }

    private List<TareaRequest> getAll() {
        try {
            // fichero que no existe o vacío = todavía no hay tareas
            if (Files.notExists(path) || Files.size(path) == 0) {
                return new ArrayList<>();
            }
            return new ArrayList<>(mapper.readValue(
                    path.toFile(), new TypeReference<List<TareaRequest>>() {}));
        } catch (IOException e) {
            throw new RuntimeException("No se puede leer " + path, e);
        }
    }
}
