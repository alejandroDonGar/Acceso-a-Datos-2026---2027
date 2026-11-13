package es.examen.sportrent.mapper;
import es.examen.sportrent.entity.*;
import es.examen.sportrent.model.*;

public final class EquipoMapper {
    private EquipoMapper(){}

    public static EquipoEntity toEntity(Equipo dato) {
        return new EquipoEntity(
                dato.getCodigo(),
                dato.getNombre(),
                dato.getCategoria(),
                dato.getPrecioDia(),
                dato.getEstado()
        );
    }

    public static Equipo toDomain(EquipoEntity entidad) {
        return new Equipo(
                entidad.getCodigo(),
                entidad.getNombre(),
                entidad.getCategoria(),
                entidad.getPrecioDia(),
                entidad.getEstado()
        );
    }
    
}
