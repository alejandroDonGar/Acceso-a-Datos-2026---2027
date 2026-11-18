package com.ejemplo.catalogo.model;

/**
 * Cualquier entidad que tenga identificador. El repositorio generico la usa
 * para buscar, actualizar y borrar sin saber de que clase concreta se trata
 * @param <ID> tipo del identificador (por ejemplo Long)
 * @author AlejandroDonGar
 */
public interface Identifiable<ID> {
    /**
     * Devuelve el identificador de la entidad
     * @return identificador; en un record lo genera solo el componente id
     */
    ID id();
}
