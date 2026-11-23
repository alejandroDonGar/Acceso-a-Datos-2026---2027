package aula.tareas.repository;

import aula.tareas.api.TareaRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Repositorio en memoria: las tareas viven en un mapa y se pierden al reiniciar.
 *
 * @author AlejandroDonGar
 */
@Repository("repositoryMemoria")
public class TareaRepository implements ITareaRepository {
    private final AtomicLong secuencia = new AtomicLong();
    private final ConcurrentSkipListMap<Long, TareaRequest> tareas = new ConcurrentSkipListMap<>();

    @Override
    public TareaRequest create(String titulo) {
        long id = secuencia.incrementAndGet();
        var tarea = new TareaRequest(id, titulo.strip());
        tareas.put(id, tarea);
        return tarea;
    }

    @Override
    public boolean delete(long identificador) {
        // remove devuelve el valor borrado, o null si no existía
        return tareas.remove(identificador) != null;
    }

    @Override
    public List<TareaRequest> findAll() {
        return List.copyOf(tareas.values());
    }

    @Override
    public TareaRequest findById(long identificador) {
        return tareas.get(identificador);
    }

    @Override
    public TareaRequest update(TareaRequest tarea) {
        if (!tareas.containsKey(tarea.id())) {
            return null;
        }
        // el record es inmutable: se guarda uno nuevo con el mismo id
        var actualizada = new TareaRequest(tarea.id(), tarea.titulo().strip());
        tareas.put(tarea.id(), actualizada);
        return actualizada;
    }
}
