package com.docencia.sqlite;

import java.util.List;

/**
 * Interfaz con los metodos de servicio de los clientes.
 * Si ocurre un error de base de datos (por ejemplo un dni duplicado) los metodos no lanzan excepcion:
 * devuelven false, null o una lista vacia segun el caso.
 * @author AlejandroDonGar
 */
public interface ClienteDbService {
    /**
     * Guarda los datos de un cliente nuevo
     * @param cliente cliente a guardar
     * @return devuelve true si se guardo, o false si hubo un error (por ejemplo dni repetido)
     */
    Boolean create(Cliente cliente);

    /**
     * Busca a un cliente por su dni
     * Si hay un error de base de datos no se lanza excepcion y se devuelve null
     * @param dni dni del cliente a buscar
     * @return devuelve el cliente encontrado, o null si no existe
     */
    Cliente findByDni(String dni);

    /**
     * Busca a todos los clientes guardados
     * Si hay un error de base de datos no se lanza excepcion y se devuelve una lista vacia
     * @return devuelve una lista con todos los clientes, vacia si no hay ninguno
     */
    List<Cliente> findAll();

    /**
     * Actualiza el nombre, el email y la ciudad de un cliente existente
     * @param cliente cliente con los datos nuevos, identificado por su dni
     * @return devuelve true si se actualizo, o false si no existe o hubo un error
     */
    Boolean update(Cliente cliente);

    /**
     * Borra a un cliente por su dni
     * @param dni dni del cliente a borrar
     * @return devuelve true si se borro, o false si no existe o hubo un error
     */
    Boolean deleteByDni(String dni);

    /**
     * Busca a todos los clientes de una ciudad
     * Si hay un error de base de datos no se lanza excepcion y se devuelve una lista vacia
     * @param ciudad ciudad de los clientes a buscar
     * @return devuelve una lista con los clientes encontrados, vacia si no hay ninguno
     */
    List<Cliente> findByCiudad(String ciudad);
}
