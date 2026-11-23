package aula.tareas.repository;

import aula.tareas.api.TareaRequest;

import java.util.List;

/**
 * Operaciones de acceso a datos de las tareas. Cada implementación guarda
 * las tareas en un sitio distinto (memoria, CSV, JSON o SQLite).
 *
 * @author AlejandroDonGar
 */
public interface ITareaRepository {

    /**
     * Guarda una tarea nueva y le asigna el siguiente identificador.
     *
     * @param titulo título de la tarea (se guarda sin espacios al principio ni al final)
     * @return la tarea creada con su identificador
     */
    public TareaRequest create(String titulo);

    /**
     * Borra la tarea con ese identificador.
     *
     * @param identificador id de la tarea
     * @return {@code true} si existía y se ha borrado, {@code false} si no existía
     */
    public boolean delete(long identificador);

    /**
     * Devuelve todas las tareas guardadas.
     *
     * @return lista de tareas (vacía si no hay ninguna)
     */
    public List<TareaRequest> findAll();

    /**
     * Busca una tarea por su identificador.
     *
     * @param identificador id de la tarea
     * @return la tarea, o {@code null} si no existe
     */
    public TareaRequest findById(long identificador);

    /**
     * Cambia el título de una tarea que ya existe.
     *
     * @param tarea tarea con el id a buscar y el título nuevo
     * @return la tarea actualizada, o {@code null} si no existe
     */
    public TareaRequest update(TareaRequest tarea);
}
