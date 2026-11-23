package aula.tareas.entity;

@Entity(name = "tarea_orm")
public class TareaEntityORM {

    private Long id;
    private String titulo;

    public TareaEntityORM(Long id) {
        this.id = id;
    }

    public TareaEntityORM(Long id, String titulo) {
        this.id = id;
        this.titulo = titulo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}
