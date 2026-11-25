package aula.tareas.repository;

import aula.tareas.api.TareaRequest;
import aula.tareas.entity.TareaEntity;

import java.util.List;

// ¿Por que se usa extends CrudRepository<> en vez de implements ITareaRepository? EL profesor dijo error de diseño. CRUD diferente separado de los ficheros

/**
 *
 * Se usa una clase la clase JpaRepository con <ClaseEntity,Long> con su CRUD basico
 * Aquí no se usan los metodos PrepareStatement o ResultSet.
 */

public class TareaRepositoryORM implements ITareaRepository {

    private final ITareaRepositoryJpa tareaRepositoryJpa;

    public TareaRepositoryORM(ITareaRepositoryJpa tareaRepositoryJpa) {
        this.tareaRepositoryJpa = tareaRepositoryJpa;
    }

    @Override
    public TareaRequest create(String titulo) {
        TareaEntity tareaEntitySave = tareaRepositoryJpa.save(new TareaEntity(titulo)); // Falta constructor con titulo unicamente
        //Hacer mapper a TareaRequest de tareaEntity
        return null;
    }

    @Override
    public boolean delete(long identificador) {
        return false;
    }

    @Override
    public List<TareaRequest> findAll() {
        return List.of();
    }

    @Override
    public TareaRequest findById(long identificador) {
        return null;
    }

    @Override
    public TareaRequest update(TareaRequest tarea) {
        return null;
    }
}