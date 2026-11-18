package aula.tareas.repository;

import aula.tareas.modelo.Tarea;
import java.util.List;
/**
 * @author AlejandroDonGar
 *
 * Interfaz ITareaRepository
 */
public interface ITareaRepository {
    // Documentar
    public List<Tarea> findAll();
    // Documentar
    public Tarea findById(long identificador);
    // Documentar
    public Tarea create(String titulo);
    // Documentar
    public boolean delete(long identificador);
    // Documentar
    public Tarea update(Tarea tarea);
}
