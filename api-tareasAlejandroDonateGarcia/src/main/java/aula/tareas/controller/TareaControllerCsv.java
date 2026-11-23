package aula.tareas.controller;

import java.net.URI;
import java.util.List;

import aula.tareas.api.TareaRequest;
import aula.tareas.service.ITareaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST de las tareas guardadas en CSV ({@code data/tarea.csv}).
 * Atiende las peticiones HTTP en {@code /api/csv/tareas} y delega en {@link ITareaService}.
 *
 * @author AlejandroDonGar
 */
@RestController
@RequestMapping("/api/csv/tareas")
@Tag(name = "Tareas CSV", description = "Operaciones crud de tareas en csv")
public class TareaControllerCsv {
    private final ITareaService servicio;

    public TareaControllerCsv(@Qualifier("tareaServiceCsv") ITareaService servicio) {
        this.servicio = servicio;
    }

    /**
     * Lista todas las tareas.
     * {@code @GetMapping} sin ruta: responde a {@code GET /api/csv/tareas}.
     * {@code @Operation} y {@code @ApiResponses} solo documentan el endpoint en Swagger.
     *
     * @return lista de tareas (200 OK; Spring la convierte a JSON)
     */
    @GetMapping
    @Operation(summary = "Obtener todas las tareas desde el fichero csv")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Se obtiene el listado de tareas"))
    public List<TareaRequest> listar() {
        return servicio.listar();
    }

    /**
     * Busca una tarea por su id: {@code GET /api/csv/tareas/{id}}.
     * {@code @PathVariable} copia el {@code {id}} de la URL en el parámetro {@code id}.
     *
     * @param id identificador de la tarea
     * @return 200 con la tarea, o 404 si no existe ({@code ofNullable} hace 404 con null)
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener la tarea a traves de su id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tarea encontrada"),
            @ApiResponse(responseCode = "404", description = "Tarea no encontrada")
    })
    public ResponseEntity<TareaRequest> buscar(@PathVariable long id) {
        return ResponseEntity.ofNullable(servicio.buscar(id));
    }

    /**
     * Crea una tarea: {@code POST /api/csv/tareas} con {@code {"titulo":"..."}}.
     * {@code @RequestBody} convierte el JSON del cuerpo en un {@link NuevaTarea} y
     * {@code @Valid} comprueba su {@code @NotBlank}: si el título está en blanco, Spring responde 400.
     *
     * @param peticion cuerpo con el título
     * @return 201 Created con la cabecera {@code Location} y la tarea creada
     */
    @PostMapping
    @Operation(summary = "Crear una tarea")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tarea creada"),
            @ApiResponse(responseCode = "400", description = "Título en blanco")
    })
    public ResponseEntity<TareaRequest> crear(@Valid @RequestBody NuevaTarea peticion) {
        TareaRequest tarea = servicio.crear(peticion.titulo());
        return ResponseEntity.created(URI.create("/api/csv/tareas/" + tarea.id())).body(tarea);
    }

    /**
     * Cambia el título de una tarea: {@code PUT /api/csv/tareas/{id}}.
     * Usa {@code @PathVariable} para el id y {@code @Valid @RequestBody} para el título nuevo.
     *
     * @param id       identificador de la tarea
     * @param peticion cuerpo con el título nuevo
     * @return 200 con la tarea actualizada, 400 si el título está en blanco o 404 si no existe
     */
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar el título de una tarea")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tarea actualizada"),
            @ApiResponse(responseCode = "400", description = "Título en blanco"),
            @ApiResponse(responseCode = "404", description = "Tarea no encontrada")
    })
    public ResponseEntity<TareaRequest> actualizar(@PathVariable long id, @Valid @RequestBody NuevaTarea peticion) {
        return ResponseEntity.ofNullable(servicio.actualizar(id, peticion.titulo()));
    }

    /**
     * Borra una tarea: {@code DELETE /api/csv/tareas/{id}}.
     *
     * @param id identificador de la tarea
     * @return 204 No Content si se ha borrado, o 404 si no existía
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Borrar una tarea")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Tarea borrada"),
            @ApiResponse(responseCode = "404", description = "Tarea no encontrada")
    })
    public ResponseEntity<Void> borrar(@PathVariable long id) {
        if (servicio.borrar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Cuerpo JSON para crear o actualizar: solo el título, que no puede estar en blanco.
     *
     * @author AlejandroDonGar
     */
    public record NuevaTarea(@NotBlank String titulo) {}
}
