package ies.puesto.peliculas.repository.jdbc;

import ies.puesto.peliculas.model.Pelicula;
import org.springframework.stereotype.Component;

/**
 * Frontera dominio - fila: convierte entre {@link Pelicula} y {@link PeliculaEntity}.
 * Visibilidad de paquete, igual que la entidad.
 *
 * @author AlejandroDonGar
 */
@Component
class PeliculaEntityMapper {

    PeliculaEntity aEntity(Pelicula pelicula) {
        return new PeliculaEntity(
                pelicula.getIdentificador(), pelicula.getNombre(),
                pelicula.getAnioLanzamiento(), pelicula.getMinutosProyeccion());
    }

    Pelicula aPelicula(PeliculaEntity entity) {
        return new Pelicula(
                entity.getCodigoFila(), entity.getTituloFila(),
                entity.getAnioFila(), entity.getMinutosFila());
    }
}
