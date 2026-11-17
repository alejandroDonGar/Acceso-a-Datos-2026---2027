package aula.tareas.service;

import java.util.List;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicLong;

import aula.tareas.modelo.Tarea;
import org.springframework.stereotype.Service;
/**
 * @author AlejandroDonGar
 *
 * Clase TareaService
 */
@Service
public class TareaService {
    private final AtomicLong secuencia = new AtomicLong();
    private final ConcurrentSkipListMap<Long, Tarea> tareas = new ConcurrentSkipListMap<>();

    public List<Tarea> listar() {
        return List.copyOf(tareas.values()); // Genera lista nueva con copyOf
    }
    public Tarea buscar(long id) {
        return tareas.get(id);
    }
    public Tarea crear(String titulo) {
        long id = secuencia.incrementAndGet();
        var tarea = new Tarea(id, titulo.strip());
        tareas.put(id, tarea);
        return tarea;
    }
}

/*--------------------*/
// GET -> Obtener info
// POST -> Crear info
// PUT -> Actualizar info
// DELETE -> Borrar info