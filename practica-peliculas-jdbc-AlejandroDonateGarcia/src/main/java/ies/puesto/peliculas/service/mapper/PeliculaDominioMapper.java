package ies.puesto.peliculas.service.mapper;

import ies.puesto.peliculas.model.Pelicula;
import ies.puesto.peliculas.service.modelo.DatosCreacionPelicula;
import ies.puesto.peliculas.service.modelo.DatosEdicionPelicula;
import org.springframework.stereotype.Component;

/**
 * Frontera servicio - dominio: construye la {@link Pelicula} con lo que se ha pedido.
 *
 * @author AlejandroDonGar
 */
@Component
public class PeliculaDominioMapper {

    public Pelicula aPelicula(DatosCreacionPelicula datos) {
        return new Pelicula(
                datos.referenciaSolicitada(), datos.nombreSolicitado(),
                datos.anioSolicitado(), datos.minutosSolicitados());
    }

    // en la edición el identificador viene de la URL, no del cuerpo
    public Pelicula aPelicula(String identificador, DatosEdicionPelicula datos) {
        return new Pelicula(
                identificador, datos.nombreSolicitado(),
                datos.anioSolicitado(), datos.minutosSolicitados());
    }
}
