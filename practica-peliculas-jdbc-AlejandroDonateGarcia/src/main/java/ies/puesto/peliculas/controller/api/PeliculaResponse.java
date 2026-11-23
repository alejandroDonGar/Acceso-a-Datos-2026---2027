package ies.puesto.peliculas.controller.api;

/**
 * Película que devuelve la API en GET, POST y PUT.
 *
 * @author AlejandroDonGar
 */
public record PeliculaResponse(String codigo, String titulo, int anioEstreno, int duracionMinutos) {
}
