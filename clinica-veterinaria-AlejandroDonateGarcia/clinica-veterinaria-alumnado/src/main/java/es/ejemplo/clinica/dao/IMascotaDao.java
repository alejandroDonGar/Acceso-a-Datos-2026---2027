package es.ejemplo.clinica.dao;
import es.ejemplo.clinica.modelo.Mascota;
import java.util.List;
import java.util.Optional;
/**
 * @author AlejandroDonGar
 * IMascotaDao
 */
public interface IMascotaDao  {
    /**
     * Funcion que crea una lista de mascotas
     * @return Devuelve una lista con todas las mascotas
     */
    List<Mascota> listar();

    /**
     * Funcion que busca una mascota en la base de datos usando su id
     * @param id Identificador de la mascota
     * @return Devuelve la mascota si existe o un Optional vacio si no
     */
    Optional<Mascota> buscarPorId(Integer id);

    /**
     * Funcion que inserta una mascota en la base de datos
     * @param entidad Mascota a insertar
     * @return Devuelve el id generado de la mascota insertada
     */
    int insertar(Mascota entidad);

    /**
     * Funcion que actualiza una mascota
     * @param entidad Mascota a actualizar
     * @return Devuelve 1 o 0 dependiendo de si se actualizo o no correctamente la mascota
     */
    int actualizar(Mascota entidad);

    /**
     * Funcion que elimina una mascota
     * @param id Identificador de la mascota
     * @return Devuelve 1 o 0 dependiendo de si se elimino o no correctamente la mascota
     */
    int eliminar(Integer id);

    /**
     * Funcion que busca las mascotas de un cliente
     * @param idCliente Identificador del cliente propietario
     * @return Devuelve una lista con las mascotas del cliente
     */
    List<Mascota> buscarPorCliente(Integer idCliente);

    /**
     * Funcion que busca las mascotas de una especie
     * @param especie Especie de las mascotas (por ejemplo, Perro o Gato)
     * @return Devuelve una lista con las mascotas de esa especie
     */
    List<Mascota> listarPorEspecie(String especie);
}
