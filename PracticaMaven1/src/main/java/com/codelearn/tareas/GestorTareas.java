package com.codelearn.tareas;

import java.util.ArrayList;
import java.util.List;

/**
 * Guarda la lista de tareas en memoria y permite añadir, completar,
 * eliminar y buscar tareas.
 *
 * @author AlejandroDonGar
 */
public class GestorTareas {
    private final List<Tarea> tareas = new ArrayList<>();
    private int siguienteId = 1;

    /**
     * Crea un gestor de tareas vacío, empezando por el id 1.
     */
    public GestorTareas() {
    }

    /**
     * Crea un gestor de tareas a partir de unas tareas ya existentes
     * (se usa al cargar las tareas guardadas en el archivo).
     *
     * @param tareasIniciales tareas con las que empieza el gestor
     * @param siguienteId siguiente id que se asignará a una tarea nueva
     */
    public GestorTareas(List<Tarea> tareasIniciales, int siguienteId) {
        this.tareas.addAll(tareasIniciales);
        this.siguienteId = siguienteId;
    }

    /**
     * Añade una tarea nueva con el título indicado.
     *
     * @param titulo título de la tarea; no puede ser nulo ni estar vacío
     * @return la tarea creada
     */
    public Tarea anadir(String titulo) {
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("El título es obligatorio");
        }
        Tarea tarea = new Tarea(siguienteId, titulo, false);
        siguienteId++;
        tareas.add(tarea);
        return tarea;
    }

    /**
     * Marca como completada la tarea con el id indicado.
     *
     * @param id identificador de la tarea a completar
     */
    public void completar(int id) {
        Tarea tarea = buscar(id);
        tarea.marcarCompletada();
    }

    /**
     * Elimina la tarea con el id indicado.
     *
     * @param id identificador de la tarea a eliminar
     */
    public void eliminar(int id) {
        Tarea tarea = buscar(id);
        tareas.remove(tarea);
    }

    /**
     * Busca la tarea con el id indicado.
     *
     * @param id identificador de la tarea buscada
     * @return la tarea encontrada
     */
    public Tarea buscar(int id) {
        for (Tarea tarea : tareas) {
            if (tarea.getId() == id) {
                return tarea;
            }
        }
        throw new IllegalArgumentException("No existe una tarea con id " + id);
    }

    /**
     * Devuelve la lista de tareas actuales. La lista devuelta no se puede
     * modificar desde fuera.
     *
     * @return copia inmutable de la lista de tareas
     */
    public List<Tarea> listar() {
        return List.copyOf(tareas);
    }

    /**
     * Devuelve el siguiente id que se asignará a una tarea nueva.
     *
     * @return el siguiente id disponible
     */
    public int getSiguienteId() {
        return siguienteId;
    }
}
