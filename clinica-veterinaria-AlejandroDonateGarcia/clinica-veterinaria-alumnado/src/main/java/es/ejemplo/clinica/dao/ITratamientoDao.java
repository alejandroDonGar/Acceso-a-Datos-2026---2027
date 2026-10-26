package es.ejemplo.clinica.dao;
import es.ejemplo.clinica.modelo.Tratamiento;
import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;
/**
 * @author AlejandroDonGar
 * ITratamientoDao
 */
public interface ITratamientoDao  {
    /**
     * Funcion que crea una lista de tratamientos
     * @return Devuelve una lista con todos los tratamientos
     */
    List<Tratamiento> listar();

    /**
     * Funcion que busca un tratamiento en la base de datos usando su id
     * @param id Identificador del tratamiento
     * @return Devuelve el tratamiento si existe o un Optional vacio si no
     */
    Optional<Tratamiento> buscarPorId(Integer id);

    /**
     * Funcion que inserta un tratamiento en la base de datos
     * @param entidad Tratamiento a insertar
     * @return Devuelve el id generado del tratamiento insertado
     */
    int insertar(Tratamiento entidad);

    /**
     * Funcion que actualiza un tratamiento
     * @param entidad Tratamiento a actualizar
     * @return Devuelve 1 o 0 dependiendo de si se actualizo o no correctamente el tratamiento
     */
    int actualizar(Tratamiento entidad);

    /**
     * Funcion que elimina un tratamiento
     * @param id Identificador del tratamiento
     * @return Devuelve 1 o 0 dependiendo de si se elimino o no correctamente el tratamiento
     */
    int eliminar(Integer id);

    /**
     * Funcion que busca los tratamientos de una consulta
     * @param idConsulta Identificador de la consulta
     * @return Devuelve una lista con los tratamientos de la consulta
     */
    List<Tratamiento> listarPorConsulta(Integer idConsulta);

    /**
     * Funcion que calcula el coste total de los tratamientos de una consulta
     * (suma de cantidad por precio unitario de cada tratamiento)
     * @param idConsulta Identificador de la consulta
     * @return Devuelve el coste total, o 0 si la consulta no tiene tratamientos
     */
    BigDecimal calcularCosteTratamientos(Integer idConsulta);
}
