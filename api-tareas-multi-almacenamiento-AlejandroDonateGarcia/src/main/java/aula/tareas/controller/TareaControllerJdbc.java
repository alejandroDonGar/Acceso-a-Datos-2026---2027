package aula.tareas.controller;

import aula.tareas.api.TareaRequest;
import aula.tareas.api.TareaResponse;
import aula.tareas.mapper.TareaMapperApi;
import aula.tareas.mapper.TareaMapperJdbc;
import aula.tareas.modelo.Tarea;
import aula.tareas.service.ITareaService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/jdbc/tareas")
public class TareaControllerJdbc {
    private final ITareaService servicio;

    public TareaControllerJdbc(@Qualifier("tareaServiceJdbc") ITareaService servicio) {
        this.servicio = servicio;
    }

    // TODO: Definir la ruta base /api/jdbc/tareas.

    @GetMapping()
    public List<TareaResponse> listar() {
        List<TareaResponse> list = new ArrayList<>();
        return list.add(TareaMapperApi.toResponse(servicio.listar()));
    }
    @GetMapping("/id")
    public ResponseEntity<TareaResponse> buscar(@PathVariable long id) {
        // TODO: Exponer el alta, validar el cuerpo y componer la respuesta HTTP.
        throw new UnsupportedOperationException("Pendiente de implementar");
    }
    @PostMapping()
    public ResponseEntity<TareaResponse> crear(@Valid @RequestBody TareaRequest request) {
        Tarea tareaCreada = servicio.crear(TareaMapperJdbc.)
    }
    @PutMapping()
    public ResponseEntity<TareaResponse> actualizar(long id, TareaRequest request) {
        // TODO: Exponer la actualización y validar la petición.
        throw new UnsupportedOperationException("Pendiente de implementar");
    }
    @DeleteMapping()
    public ResponseEntity<Void> eliminar(long id) {
        // TODO: Exponer el borrado y comunicar el resultado.
        throw new UnsupportedOperationException("Pendiente de implementar");
    }
}
