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
@Service("tareaServiceJdbc")
public class TareaServiceJdbc implements ITareaService {

    private final ITareaRepository tareaRepositoryJdbc;

    public TareaServiceJdbc(@Qualifier("repositoryJdbc") ITareaRepository tareaRepositoryJdbc) {
        this.tareaRepositoryJdbc = tareaRepositoryJdbc;
    }

    @Override
    public List<TareaRequest> listar() {
        return tareaRepositoryJdbc.findAll();
    }

    @Override
    public TareaRequest buscar(long id) {
        return tareaRepositoryJdbc.findById(id);
    }

    @Override
    public TareaRequest crear(String titulo) {
        return tareaRepositoryJdbc.create(titulo);
    }

    @Override
    public TareaRequest actualizar(long id, String titulo) {
        return tareaRepositoryJdbc.update(new TareaRequest(id, titulo));
    }

    @Override
    public boolean borrar(long id) {
        return tareaRepositoryJdbc.delete(id);
    }
}
