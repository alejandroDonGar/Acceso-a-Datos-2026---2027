package com.ejemplo.catalogo.repository;
import com.ejemplo.catalogo.model.Producto;

import java.util.List;
import java.util.Optional;

public interface IRepository{
    /**
     * Lista todos los productos
     * @return Devuelve una lista con todos los productos
     */
    List<Producto> findAll();
    /**
     * Busca un producto por su id
     * @param id id del producto
     * @return Devuelve el producto
     */
    Optional<Producto> findById(long id);
    /**
     * Crea un producto
     * @param producto Producto a crear
     */
    void create(Producto producto);
    /**
     * Actualiza un producto
     * @param producto Producto a actualizar
     * @return Devuelve true o false
     */
    boolean update(Producto producto);
    /**
     * Borra un producto
     * @param id ID del producto a borrar
     * @return Devuelve true o false
     */
    boolean delete(long id);
}
