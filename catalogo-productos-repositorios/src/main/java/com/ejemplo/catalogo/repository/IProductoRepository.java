package com.ejemplo.catalogo.repository;

import com.ejemplo.catalogo.model.Producto;

/**
 * Repositorio de productos: fija los genericos de IRepository a Producto con id Long.
 * Asi se puede trabajar con "cualquier repositorio de productos" sin saber si es CSV, JSON o XML
 * @author AlejandroDonGar
 */
public interface IProductoRepository extends IRepository<Producto , Long>{

}
