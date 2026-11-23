package ies.puesto.peliculas.repository;

import ies.puesto.peliculas.model.Pelicula;

import java.util.List;
import java.util.Optional;

/**
 * Contrato de acceso a datos de las películas. Solo trabaja con {@link Pelicula}:
 * el servicio no sabe si debajo hay JDBC, ficheros u otra cosa.
 *
 * @author AlejandroDonGar
 */
public interface PeliculaRepository {

    /**
     * Devuelve todas las películas ordenadas por código.
     *
     * @return lista de películas (vacía si no hay ninguna)
     */
    List<Pelicula> buscarTodas();

    /**
     * Busca una película por su código.
     *
     * @param identificador código de la película (p. ej. {@code PEL001})
     * @return la película, o {@code Optional.empty()} si no existe
     */
    Optional<Pelicula> buscarPorCodigo(String identificador);

    /**
     * Inserta la película solo si su código no existe todavía.
     *
     * @param pelicula película a insertar
     * @return {@code true} si se ha insertado, {@code false} si el código ya existía
     */
    boolean insertarSiAusente(Pelicula pelicula);

    /**
     * Sustituye los datos de una película que ya existe (nunca inserta).
     *
     * @param pelicula película con el código a buscar y los datos nuevos
     * @return {@code true} si se ha actualizado, {@code false} si no existe
     */
    boolean actualizarSiExiste(Pelicula pelicula);

    /**
     * Borra una película.
     *
     * @param identificador código de la película
     * @return {@code true} si se ha borrado, {@code false} si no existía
     */
    boolean eliminar(String identificador);
}
