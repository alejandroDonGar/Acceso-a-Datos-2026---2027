package aula.tareas.repository;

import aula.tareas.entity.TareaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

// ¿Por que se usa extends JpaRepository<> en vez de implements ITareaRepository? EL profesor dijo error de diseño. CRUD diferente separado de los ficheros
public interface ITareaRepositoryEntity extends JpaRepository<TareaEntity, Long> {

}