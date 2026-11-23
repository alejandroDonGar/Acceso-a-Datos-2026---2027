package aula.tareas.service;

import aula.tareas.api.TareaRequest;

import java.util.List;

/**
 * Casos de uso de las tareas que usan los controladores. Cada implementación
 * delega en un repositorio distinto (CSV, JSON o JDBC).
 *
 * @author AlejandroDonGar
 */
public interface ITareaService {

    /**
     * Devuelve todas las tareas.
     *
     * @return lista de tareas (vacía si no hay ninguna)
     */
    public List<TareaRequest> listar();

    /**
     * Busca una tarea por su identificador.
     *
     * @param id identificador de la tarea
     * @return la tarea, o {@code null} si no existe
     */
    public TareaRequest buscar(long id);

    /**
     * Crea una tarea nueva.
     *
     * @param titulo título de la tarea (no puede estar en blanco)
     * @return la tarea creada con su identificador
     */
    public TareaRequest crear(String titulo);

    /**
     * Cambia el título de una tarea.
     *
     * @param id     identificador de la tarea
     * @param titulo título nuevo
     * @return la tarea actualizada, o {@code null} si no existe
     */
    public TareaRequest actualizar(long id, String titulo);

    /**
     * Borra una tarea.
     *
     * @param id identificador de la tarea
     * @return {@code true} si se ha borrado, {@code false} si no existía
     */
    public boolean borrar(long id);
}
