package ies.puesto.peliculas.service.modelo;

/**
 * Lo que se pide al servicio para editar una película. Sin código: lo pone la URL.
 *
 * @author AlejandroDonGar
 */
public record DatosEdicionPelicula(String nombreSolicitado, int anioSolicitado, int minutosSolicitados) {
}
