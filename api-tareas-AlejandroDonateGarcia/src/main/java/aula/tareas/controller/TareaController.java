package aula.tareas.controller;

import java.net.URI;
import java.util.List;

import aula.tareas.modelo.Tarea;
import aula.tareas.service.TareaService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
/**
 * @author AlejandroDonGar
 *
 * Clase TareaController
 */
@RestController // Soy servicio red controlado
@RequestMapping("/api/tareas")
class TareaController {
    private final TareaService servicio;

    TareaController(TareaService servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<Tarea> listar() {
        return servicio.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tarea> buscar(@PathVariable long id) {
        return ResponseEntity.ofNullable(servicio.buscar(id));
    }

    @PostMapping
    public ResponseEntity<Tarea> crear(@Valid @RequestBody NuevaTarea peticion) {
        //@Valid @RequestBody -> Importante, ¿Por que?
        Tarea tarea = servicio.crear(peticion.titulo());
        return ResponseEntity.created(URI.create("/api/tareas/" + tarea.id())).body(tarea);
    }

    public record NuevaTarea(@NotBlank String titulo) {}
}

