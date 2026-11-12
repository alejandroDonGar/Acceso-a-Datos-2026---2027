package es.ejemplo.cinefest.orm.mapper;

import es.ejemplo.cinefest.modelo.Pelicula;
import es.ejemplo.cinefest.orm.entity.PeliculaEntity;

public final class PeliculaMapper {
    private PeliculaMapper() {}
    public static Pelicula desdeEntidad(PeliculaEntity source) { // Equivalente a -> toDomain
        if (source == null) return null;
        Pelicula result = new Pelicula();
        result.setIdPelicula(source.getIdPelicula());
        result.setTitulo(source.getTitulo());
        result.setGenero(source.getGenero());
        result.setDuracionMin(source.getDuracionMin());
        result.setPais(source.getPais());
        result.setAnio(source.getAnio());
        return result;
    }
    public static PeliculaEntity aEntidad(Pelicula source) { // Equivalente a -> toEntity
        if (source == null) return null;
        PeliculaEntity result = new PeliculaEntity();
        result.setIdPelicula(source.getIdPelicula());
        result.setTitulo(source.getTitulo());
        result.setGenero(source.getGenero());
        result.setDuracionMin(source.getDuracionMin());
        result.setPais(source.getPais());
        result.setAnio(source.getAnio());
        return result;
    }
}
