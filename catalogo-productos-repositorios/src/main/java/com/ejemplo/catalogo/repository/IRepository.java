package com.ejemplo.catalogo.repository;

import java.util.List;
import java.util.Optional;

/**
 * Contrato generico de un repositorio CRUD: dice que se puede hacer con las
 * entidades, no como se guardan (CSV, JSON, XML...)
 * @param <T> tipo de la entidad
 * @param <ID> tipo de su identificador
 * @author AlejandroDonGar
 */
public interface IRepository<T, ID>{
    /**
     * Devuelve todas las entidades guardadas
     * @return lista con las entidades, vacia si no hay ninguna
     */
    List<T> findAll();

    /**
     * Busca una entidad por su identificador
     * @param id identificador a buscar
     * @return la entidad si existe, o Optional.empty() si no existe o el id es null
     */
    Optional<T> findById(ID id);

    /**
     * Guarda una entidad nueva
     * @param entity entidad a guardar
     * @return true si se ha guardado; false si es null, no tiene id o el id ya existe
     */
    boolean create(T entity);

    /**
     * Sustituye la entidad que tenga el mismo identificador
     * @param entity entidad con los datos nuevos
     * @return true si existia y se ha actualizado; false en otro caso
     */
    boolean update(T entity);

    /**
     * Borra la entidad con el identificador indicado
     * @param id identificador de la entidad a borrar
     * @return true si existia y se ha borrado; false en otro caso
     */
    boolean delete(ID id);
}
