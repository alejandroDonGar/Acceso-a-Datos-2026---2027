package es.ejemplo.clinica.dao;
import es.ejemplo.clinica.modelo.Consulta;
import java.util.List;
import java.util.Optional;
/**
 * @author AlejandroDonGar
 * IConsultaDao
 */
public interface IConsultaDao  {
    /**
     * Funcion que crea una lista de consultas
     * @return Devuelve una lista con todas las consultas
     */
    List<Consulta> listar();

    /**
     * Funcion que busca una consulta en la base de datos usando su id
     * @param id Identificador de la consulta
     * @return Devuelve la consulta si existe o un Optional vacio si no
     */
    Optional<Consulta> buscarPorId(Integer id);

    /**
     * Funcion que inserta una consulta en la base de datos
     * @param entidad Consulta a insertar
     * @return Devuelve el id generado de la consulta insertada
     */
    int insertar(Consulta entidad);

    /**
     * Funcion que actualiza una consulta
     * @param entidad Consulta a actualizar
     * @return Devuelve 1 o 0 dependiendo de si se actualizo o no correctamente la consulta
     */
    int actualizar(Consulta entidad);

    /**
     * Funcion que elimina una consulta
     * @param id Identificador de la consulta
     * @return Devuelve 1 o 0 dependiendo de si se elimino o no correctamente la consulta
     */
    int eliminar(Integer id);

    /**
     * Funcion que busca las consultas de una mascota
     * @param idMascota Identificador de la mascota
     * @return Devuelve una lista con las consultas de la mascota
     */
    List<Consulta> buscarPorMascota(Integer idMascota);

    /**
     * Funcion que busca las consultas de un veterinario
     * @param idVeterinario Identificador del veterinario
     * @return Devuelve una lista con las consultas del veterinario
     */
    List<Consulta> buscarPorVeterinario(Integer idVeterinario);

    /**
     * Funcion que busca las consultas con estado PROGRAMADA
     * @return Devuelve una lista con las consultas programadas
     */
    List<Consulta> listarProgramadas();
}
