package aula.tareas.controller;
import aula.tareas.modelo.Tarea;
import aula.tareas.service.ITareaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author AlejandroDonGar
 *
 * Clase TareaControllerJson
 */
@RestController // Soy servicio red controlado
@RequestMapping("/api/json/tareas")
@Tag(name="tareaJson", description = "Operaciones CRUD de tareas en JSON")
public class TareaControllerJson {

    private final ITareaService tareaServiceJson;

    public TareaControllerJson(@Qualifier("tareaServiceJson")ITareaService tareaServiceJson) {
        this.tareaServiceJson = tareaServiceJson;
    }

    @GetMapping
    @Operation(summary = "Obtener todas las tareas desde un fichero JSON")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Se obtiene el listado de tareas"))
    public List<Tarea> listar() {
        return new ArrayList<>();
    }
    @GetMapping("/{id}")
    @Operation(summary = "Obtener la tarea usando su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tarea encontrada"),
            @ApiResponse(responseCode = "404", description = "Tarea no encontrada")
    })
    public ResponseEntity<Tarea> buscar(@PathVariable long id) {
        //return ResponseEntity.ofNullable(servicio.buscar(id));
        return null;
    }

    @PostMapping
    @Operation(summary = "Obtener la tarea usando su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tarea creada"),
            @ApiResponse(responseCode = "400", description = "Tarea existente")
    })
    public ResponseEntity<Tarea> crear(@Valid @RequestBody TareaControllerCsv.NuevaTarea peticion) {
        //@Valid @RequestBody -> Importante, ¿Por que?
        //Tarea tarea = servicio.crear(peticion.titulo());
        //return ResponseEntity.created(URI.create("/api/tareas/" + tarea.id())).body(tarea);
        return null;
    }
}
