package es.codelearnacademy.filelab.repository;

import java.util.List;
import java.util.Optional;

public interface IRepository<T, ID> {

    List<T> findAll();
    /**
     * Encuentra un elemento por su identificador
     * @param id Identificador del elemento a buscar
     * @return Devuelkv
     */
    Optional<T> findById(ID id);
    /**
     * Crea una entidad
     * @param entity Entidad a crear
     * @return Devuelve true o false si se creo o no
     */
    boolean create(T entity);
    /**
     * Actualiza una entidad
     * @param entity Entidad a actualizar
     * @return Devuelve true o false si se actualizo o no
     */
    boolean update(T entity);
    /**
     * Borra un elemento usando su identificador
     * @param id Identificador del objeto
     * @return Devuelve true o false si se elimino o no
     */
    boolean delete(ID id);
}