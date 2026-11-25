package aula.tareas.mapper;

import aula.tareas.api.TareaResponse;
import aula.tareas.modelo.Tarea;

/**
 * Se pueden hacer tambien:
 * 1 - Quitando el final
 * 2 - Añadiendo encima '@Component("ClaseMapperTipo")'
 * 3 - Quitando los static
 *
 * Esto con todos los mapper
 */
public final class TareaMapperApi {
    private TareaMapperApi() {}

    public static TareaResponse toResponse(Tarea tarea) {
        return new  TareaResponse(
                tarea.identificador(),
                tarea.nombre()
        );
    }
}
