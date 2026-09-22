package com.codelearn.tareas;

import java.util.ArrayList;
import java.util.List;

public class GestorTareas {
    private final List<Tarea> tareas = new ArrayList<>();
    private int siguienteId = 1;

    public GestorTareas() {
    }

    public GestorTareas(List<Tarea> tareasIniciales, int siguienteId) {
        this.tareas.addAll(tareasIniciales);
        this.siguienteId = siguienteId;
    }

    public Tarea anadir(String titulo) {
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("El título es obligatorio");
        }
        Tarea tarea = new Tarea(siguienteId, titulo, false);
        siguienteId++;
        tareas.add(tarea);
        return tarea;
    }

    public void completar(int id) {
        Tarea tarea = buscar(id);
        tarea.marcarCompletada();
    }

    public void eliminar(int id) {
        Tarea tarea = buscar(id);
        tareas.remove(tarea);
    }

    public Tarea buscar(int id) {
        for (Tarea tarea : tareas) {
            if (tarea.getId() == id) {
                return tarea;
            }
        }
        throw new IllegalArgumentException("No existe una tarea con id " + id);
    }

    public List<Tarea> listar() {
        return List.copyOf(tareas);
    }

    public int getSiguienteId() {
        return siguienteId;
    }
}
