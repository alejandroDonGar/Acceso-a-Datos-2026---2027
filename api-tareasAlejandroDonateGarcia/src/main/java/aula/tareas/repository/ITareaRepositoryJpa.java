package aula.tareas.repository;

import aula.tareas.entity.TareaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ITareaRepositoryJpa extends JpaRepository<TareaEntity, Long> {
    /**
     * Aqui no se crean metodos porque se instancian directamente en servicio al crear la variable:
     *
     * private final ITareaRepositoryJpa tareaRepositoryJpa;
     *
     * public TareaRepositoryORM(ITareaRepositoryJpa tareaRepositoryJpa) {
     *     this.tareaRepositoryJpa = tareaRepositoryJpa;
     * }
     *
     * Los metodos aparecen al hacer tareaRepositoryJpa.metodo() automaticamente
     */
}
