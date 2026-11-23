package aula.tareas.entity;

/**
 * Futura entidad JPA de la tabla {@code tareas_orm}.
 * El {@code @Entity} está comentado porque el pom aún no tiene
 * {@code spring-boot-starter-data-jpa}: sin esa dependencia no compila.
 * Al añadir JPA harán falta además {@code @Id}, {@code @GeneratedValue} y un constructor vacío.
 *
 * @author AlejandroDonGar
 */
//@Entity(name = "tarea_orm")
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
