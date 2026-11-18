package aula.tareas.service;

import java.util.List;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicLong;

import aula.tareas.modelo.Tarea;
import aula.tareas.repository.ITareaRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
/**
 * @author AlejandroDonGar
 *
 * Clase TareaServiceCsv
 */
@Service("tareaServiceCsv")
public class TareaServiceCsv implements ITareaService{
    private final AtomicLong secuencia = new AtomicLong();
    private final ConcurrentSkipListMap<Long, Tarea> tareas = new ConcurrentSkipListMap<>();
    private final ITareaRepository tareaRepository;

    // Como se hacia antes:
    // public TareaService(ITareaRepository tareaRepository) {
    //     this.tareaRepository = new ITareaRepository();
    // }

    // Como se hace en Spring
    public TareaServiceCsv(@Qualifier("repositorioCsv") ITareaRepository tareaRepository) {
        this.tareaRepository = tareaRepository;
    }

    public List<Tarea> listar() {
        return tareaRepository.findAll();
    }
    public Tarea buscar(long id) {
        return tareaRepository.findById(id);
    }
    public Tarea crear(String titulo) {
        if(titulo==null || titulo.isEmpty()) {
            return null;
        }
        return tareaRepository.create(titulo);
    }
}

/*--------------------*/
// GET -> Obtener info
// POST -> Crear info
// PUT -> Actualizar info
// DELETE -> Borrar info