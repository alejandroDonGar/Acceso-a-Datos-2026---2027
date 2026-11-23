package aula.tareas.modelo;

/**
 * Modelo de dominio de una tarea (de momento no lo usa ninguna capa).
 *
 * @author AlejandroDonGar
 */
public class Tarea {

    private long id;
    private String titulo;

    public Tarea(long id, String titulo) {
        this.id = id;
        this.titulo = titulo;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}
