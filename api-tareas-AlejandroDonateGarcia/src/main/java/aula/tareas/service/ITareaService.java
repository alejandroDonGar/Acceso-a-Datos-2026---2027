package aula.tareas.service;
import aula.tareas.modelo.Tarea;
import java.util.List;
/**
 * @author AlejandroDonGar
 *
 * Interfaz ITareaService
 */
public interface ITareaService {
    // Documentar
    public List<Tarea> listar();
    // Documentar
    public Tarea buscar(long id);
    // Documentar
    public Tarea crear(String titulo);
}
