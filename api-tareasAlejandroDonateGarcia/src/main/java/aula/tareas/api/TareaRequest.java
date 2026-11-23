package aula.tareas.api;

/**
 * Tarea que devuelve la API y que guardan los repositorios: identificador y título.
 *
 * @author AlejandroDonGar
 */
public record TareaRequest(long id, String titulo) {}
