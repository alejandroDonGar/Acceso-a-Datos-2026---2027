package aula.tareas.service;

import java.util.List;

import aula.tareas.api.TareaRequest;
import aula.tareas.repository.ITareaRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Servicio de tareas que trabaja con el repositorio CSV.
 *
 * @author AlejandroDonGar
 */
@Service("tareaServiceCsv")
public class TareaServiceCsv implements ITareaService {
    private final ITareaRepository repository;

    //El pasado
    /**
    public TareaService() {
        this.repository = new TareaRepository();
    }
     **/

    //Spring: inyecta el repositorio por constructor; @Qualifier elige cuál de los 4
    public TareaServiceCsv(@Qualifier("repositoryCsv") ITareaRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<TareaRequest> listar() {
        return repository.findAll();
    }

    @Override
    public TareaRequest buscar(long id) {
        return repository.findById(id);
    }

    @Override
    public TareaRequest crear(String titulo) {
        if (titulo == null || titulo.isBlank()) {
            return null;
        }
        return repository.create(titulo);
    }

    @Override
    public TareaRequest actualizar(long id, String titulo) {
        return repository.update(new TareaRequest(id, titulo));
    }

    @Override
    public boolean borrar(long id) {
        return repository.delete(id);
    }
}
