package aula.tareas.service;

import aula.tareas.api.TareaRequest;
import aula.tareas.repository.ITareaRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio de tareas que trabaja con el repositorio JSON.
 *
 * @author AlejandroDonGar
 */
@Service("tareaServiceJson")
public class TareaServiceJson implements ITareaService {

    private final ITareaRepository tareaRepositoryJson;

    public TareaServiceJson(@Qualifier("repositoryJson") ITareaRepository tareaRepositoryJson) {
        this.tareaRepositoryJson = tareaRepositoryJson;
    }

    @Override
    public List<TareaRequest> listar() {
        return tareaRepositoryJson.findAll();
    }

    @Override
    public TareaRequest buscar(long id) {
        return tareaRepositoryJson.findById(id);
    }

    @Override
    public TareaRequest crear(String titulo) {
        return tareaRepositoryJson.create(titulo);
    }

    @Override
    public TareaRequest actualizar(long id, String titulo) {
        return tareaRepositoryJson.update(new TareaRequest(id, titulo));
    }

    @Override
    public boolean borrar(long id) {
        return tareaRepositoryJson.delete(id);
    }
}
