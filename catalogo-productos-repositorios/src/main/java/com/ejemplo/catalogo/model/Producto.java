package com.ejemplo.catalogo.model;

/**
 * Producto del catalogo. Usa tipos envoltorio (Long, Double) para que el id
 * pueda ser null y se compare con equals en el repositorio generico
 * @author AlejandroDonGar
 */
public record Producto(Long id, String nombre, Double precio) implements Identifiable<Long>{}
