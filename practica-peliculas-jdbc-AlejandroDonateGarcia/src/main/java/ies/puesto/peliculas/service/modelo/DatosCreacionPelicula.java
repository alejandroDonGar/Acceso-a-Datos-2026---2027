package ies.puesto.peliculas.service.modelo;

/**
 * Lo que se pide al servicio para crear una película (ya validado por la API).
 *
 * @author AlejandroDonGar
 */
public record DatosCreacionPelicula(String referenciaSolicitada, String nombreSolicitado,
                                    int anioSolicitado, int minutosSolicitados) {
}
