package com.docencia.sqlite;
/**
 * Interfaz con los metodos de repositorio de los clientes
 * @author AlejandorDonGar
 */
import java.util.List;
public interface ClienteRepository {
    /**
     * Guarda los datos de un cliente
     * @param cliente cliente a guardar
     * @return devuelve true o false dependiendo del resultado de la ejecucion del guardado
     */
    Boolean save(Cliente cliente);
    /**
     * Busca a un cliente por su dni
     * @param dni dni del cliente a buscar
     * @return devuelve el cliente encontrado
     */
    Cliente findByDni(String dni);
    /**
     * Busca a todos los clientes
     * @return devuelve una lista con todos los clientes guardados
     */
    List<Cliente> findAll();
    /**
     * Actualiza la informacion de un cliente
     * @param cliente cliente a actualizar
     * @return devuelve true o false dependiendo del resultado de la ejecucion del actualizado
     */
    Boolean update(Cliente cliente);
    /**
     * Borra a un cliente por su dni
     * @param dni dni del cliente a borrar
     * @return devuelve true o false dependiendo del resultado de la ejecucion del borrado
     */
    Boolean deleteByDni(String dni);
    /**
     * Busca un cliente por su ciudad
     * @param ciudad ciudad del cliente a buscar
     * @return devuleve una lista con todos los clientes encontrados
     */
    List<Cliente> findByCiudad(String ciudad);
}
