package aula.tareas.mapper;

import aula.tareas.entity.TareaEntityJdbc;
import aula.tareas.api.TareaRequest;

/**
 * Convierte entre la tarea de la API ({@link TareaRequest}) y la entidad JDBC
 * ({@link TareaEntityJdbc}). Métodos estáticos, igual que en el taller ORM.
 *
 * @author AlejandroDonGar
 */
public class TareaMapperJdbc {

    static public TareaRequest toDomain(TareaEntityJdbc entity) {
        return new TareaRequest(entity.getId(), entity.getTitulo());
    }

    static public TareaEntityJdbc toJdbc(TareaRequest tarea) {
        return new TareaEntityJdbc(tarea.id(), tarea.titulo());
    }

    //crear mappers o constructores en tarea dependiento de lo que encesitemos
    //En este caso si crearamos un mapper por solo un long id, tendremos que crear un constructor en modelo/Tarea un constructor con solo el identificador

    // Si la base de datos autogenera el ID, en los mapper no se pone.

    // API rest / Request(Capa controller) -> Mapper(MapperDomain) -> Servicios -> Mapper(MapperJdbc) -> Repository
    // Repository -> Mapper(MapperJdbc) -> Servicios -> Mapper(MapperDomain) -> API rest / Request(Capa controller)
}
