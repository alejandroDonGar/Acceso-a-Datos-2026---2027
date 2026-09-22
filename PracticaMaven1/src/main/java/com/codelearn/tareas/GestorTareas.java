package com.codelearn.tareas;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

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
        var tarea = new Tarea(siguienteId, titulo, false);
        siguienteId++;
        tareas.add(tarea);
        return tarea;
    }

    public void completar(int id) {
        var tarea = buscar(id);
        tarea.marcarCompletada();
    }

    public void eliminar(int id) {
        var tarea = buscar(id);
        tareas.remove(tarea);
    }

    public Tarea buscar(int id) {
        return tareas.stream()
                .filter(t -> t.getId() == id)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("No existe una tarea con id " + id));
    }

    public List<Tarea> listar() {
        return List.copyOf(tareas);
    }

    public int getSiguienteId() {
        return siguienteId;
    }
}
