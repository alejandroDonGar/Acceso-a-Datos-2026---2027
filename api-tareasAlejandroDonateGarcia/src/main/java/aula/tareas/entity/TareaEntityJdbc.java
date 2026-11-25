package aula.tareas.entity;

/**
 * Fila de la tabla {@code tareas_jdbc}; se convierte a {@code TareaRequest} con {@code TareaMapperJdbc}.
 *
 * @author AlejandroDonGar
 *
 * Aqui no se pone que tabla es porque se hace en el repositorio al hacer 'select' u otra accion de sqlite
 * En este caso la tabla no es una ClaseEntity sino un table_name -> 'tareas_jdbc'
 */

public class TareaEntityJdbc {

    private Long id;
    private String titulo;

    public TareaEntityJdbc(Long id) {
        this.id = id;
    }

    public TareaEntityJdbc(Long id, String titulo) {
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

/**
 * Tabajo para mejorar la documentacion de Spring
 * Detallar las diferencias entre JDBC y ORM con JPA y sus respectivas clases de Entity, Repository, Mapper y Service
 */