package aula.tareas.service;

import aula.tareas.api.TareaRequest;
import aula.tareas.repository.ITareaRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio de tareas que trabaja con el repositorio JDBC (SQLite).
 *
 * @author AlejandroDonGar
 */
@Service("tareaServiceEntity")
public class TareaServiceEntity implements ITareaService {

    private final ITareaRepository tareaRepositoryEntity;

    public TareaServiceEntity(@Qualifier("repositoryEntity") ITareaRepository tareaRepositoryEntity) {
        this.tareaRepositoryEntity = tareaRepositoryEntity;
    }

    @Override
    public List<TareaRequest> listar() {
        return tareaRepositoryEntity.findAll();
    }

    @Override
    public TareaRequest buscar(long id) {
        return tareaRepositoryEntity.findById(id);
    }

    @Override
    public TareaRequest crear(String titulo) {
        return tareaRepositoryEntity.create(titulo);
    }

    @Override
    public TareaRequest actualizar(long id, String titulo) {
        return tareaRepositoryEntity.update(new TareaRequest(id, titulo));
    }

    @Override
    public boolean borrar(long id) {
        return tareaRepositoryEntity.delete(id);
    }
}
