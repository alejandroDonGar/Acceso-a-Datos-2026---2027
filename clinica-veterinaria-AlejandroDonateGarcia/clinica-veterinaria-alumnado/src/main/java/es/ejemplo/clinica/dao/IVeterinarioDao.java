package es.ejemplo.clinica.dao;
import es.ejemplo.clinica.modelo.Veterinario;
import java.util.List;
import java.util.Optional;
/**
 * @author AlejandroDonGar
 * IVeterinarioDao
 */
public interface IVeterinarioDao  {
    /**
     * Funcion que crea una lista de veterinarios
     * @return Devuelve una lista con todos los veterinarios
     */
    List<Veterinario> listar();

    /**
     * Funcion que busca un veterinario en la base de datos usando su id
     * @param id Identificador del veterinario
     * @return Devuelve el veterinario si existe o un Optional vacio si no
     */
    Optional<Veterinario> buscarPorId(Integer id);

    /**
     * Funcion que inserta un veterinario en la base de datos
     * @param entidad Veterinario a insertar
     * @return Devuelve el id generado del veterinario insertado
     */
    int insertar(Veterinario entidad);

    /**
     * Funcion que actualiza un veterinario
     * @param entidad Veterinario a actualizar
     * @return Devuelve 1 o 0 dependiendo de si se actualizo o no correctamente el veterinario
     */
    int actualizar(Veterinario entidad);

    /**
     * Funcion que elimina un veterinario
     * @param id Identificador del veterinario
     * @return Devuelve 1 o 0 dependiendo de si se elimino o no correctamente el veterinario
     */
    int eliminar(Integer id);

    /**
     * Funcion que busca los veterinarios de una especialidad, ordenados por tarifa
     * @param especialidad Especialidad de los veterinarios (por ejemplo, Medicina general)
     * @return Devuelve una lista con los veterinarios de esa especialidad, de menor a mayor tarifa
     */
    List<Veterinario> buscarPorEspecialidad(String especialidad);
}
