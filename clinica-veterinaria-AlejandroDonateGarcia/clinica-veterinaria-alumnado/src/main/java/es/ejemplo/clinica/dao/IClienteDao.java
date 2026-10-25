package es.ejemplo.clinica.dao;

import es.ejemplo.clinica.modelo.Cliente;
import java.util.List;
import java.util.Optional;
/**
 * @author AlejandroDonGar
 * IClienteDao
 */
public interface IClienteDao  {
    /**
     * Funcion que crea una ista de clientes
     * @return Devuelve una lista de clientes
     */
    List<Cliente> listar();

    /**
     * Funcion que busca un cliente en la base de datos usando su id
     * @param id Identificador del cliente
     * @return Devuelve un cliente por su id
     */
    Optional<Cliente> buscarPorId(Integer id);

    /**
     * Funcion que inserta un cliente en la base de datos
     * @param entidad Cliente a insertar
     * @return Devuelve el id generado de la mascota insertada
     */
    int insertar(Cliente entidad);

    /**
     * Funcion que actualiza un cliente
     * @param entidad Cliente a actualizar
     * @return Devuelve 1 o 0 dependiendo de si se actualizo o no correctamente el cliente
     */
    int actualizar(Cliente entidad);

    /**
     * Funcion que borra un elimina
     * @param id Identificador del cliente
     * @return Devuelve 1 o 0 dependiendo de si se elimino o no correctamente el cliente
     */
    int eliminar(Integer id);
}
