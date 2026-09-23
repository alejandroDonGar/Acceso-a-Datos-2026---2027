package com.codelearn.tareas;

/**
 * Una tarea: tiene un identificador, un título y si está completada o no.
 *
 * @author AlejandroDonGar
 */
public class Tarea {
    private final int id;
    private final String titulo;
    private boolean completada;

    /**
     * Crea una tarea nueva.
     *
     * @param id identificador de la tarea
     * @param titulo título de la tarea
     * @param completada true si la tarea ya está completada
     */
    public Tarea(int id, String titulo, boolean completada) {
        this.id = id;
        this.titulo = titulo;
        this.completada = completada;
    }

    /**
     * Devuelve el identificador de la tarea.
     *
     * @return el id de la tarea
     */
    public int getId() {
        return id;
    }

    /**
     * Devuelve el título de la tarea.
     *
     * @return el título de la tarea
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Indica si la tarea está completada.
     *
     * @return true si está completada, false si está pendiente
     */
    public boolean isCompletada() {
        return completada;
    }

    /**
     * Marca la tarea como completada.
     */
    public void marcarCompletada() {
        this.completada = true;
    }

    /**
     * Convierte la tarea en un texto para mostrarla en la consola.
     *
     * @return el id, el estado (completada o pendiente) y el título, en una línea
     */
    @Override
    public String toString() {
        return id + " [" + (completada ? "completada" : "pendiente") + "] " + titulo;
    }
}
