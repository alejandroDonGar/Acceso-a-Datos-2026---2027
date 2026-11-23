package ies.puesto.peliculas.controller.mapper;

import ies.puesto.peliculas.controller.api.PeliculaEdicionRequest;
import ies.puesto.peliculas.controller.api.PeliculaRequest;
import ies.puesto.peliculas.controller.api.PeliculaResponse;
import ies.puesto.peliculas.model.Pelicula;
import ies.puesto.peliculas.service.modelo.DatosCreacionPelicula;
import ies.puesto.peliculas.service.modelo.DatosEdicionPelicula;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Frontera API - servicio: convierte las peticiones JSON en datos del servicio
 * y las películas del dominio en respuestas JSON.
 *
 * @author AlejandroDonGar
 */
@Component
public class PeliculaApiMapper {

    public DatosCreacionPelicula aDatosCreacion(PeliculaRequest peticion) {
        return new DatosCreacionPelicula(
                peticion.codigo(), peticion.titulo(),
                peticion.anioEstreno(), peticion.duracionMinutos());
    }

    public DatosEdicionPelicula aDatosEdicion(PeliculaEdicionRequest peticion) {
        return new DatosEdicionPelicula(
                peticion.titulo(), peticion.anioEstreno(), peticion.duracionMinutos());
    }

    public PeliculaResponse aRespuesta(Pelicula pelicula) {
        return new PeliculaResponse(
                pelicula.getIdentificador(), pelicula.getNombre(),
                pelicula.getAnioLanzamiento(), pelicula.getMinutosProyeccion());
    }

    public List<PeliculaResponse> aRespuestas(List<Pelicula> peliculas) {
        List<PeliculaResponse> respuestas = new ArrayList<>();
        for (Pelicula pelicula : peliculas) {
            respuestas.add(aRespuesta(pelicula));
        }
        return respuestas;
    }
}
