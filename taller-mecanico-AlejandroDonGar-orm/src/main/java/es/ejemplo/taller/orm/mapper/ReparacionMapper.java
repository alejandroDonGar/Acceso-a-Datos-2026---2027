package es.ejemplo.taller.orm.mapper;

import es.ejemplo.taller.modelo.Reparacion;
import es.ejemplo.taller.orm.entity.ReparacionEntity;

/**
 * Convierte entre Reparacion y ReparacionEntity usando las FK planas (matricula, idMecanico),
 * sin tocar las relaciones LAZY
 * @author AlejandroDonGar
 */
public class ReparacionMapper {
    public static ReparacionEntity toEntity(Reparacion r) {
        return new ReparacionEntity(r.getIdReparacion(), r.getMatricula(), r.getIdMecanico(),
                r.getDescripcion(), r.getFecha(), r.getHorasTrabajo(), r.getEstado());
    }

    public static Reparacion toDomain(ReparacionEntity e) {
        return new Reparacion(e.getIdReparacion(), e.getMatricula(), e.getIdMecanico(),
                e.getDescripcion(), e.getFecha(), e.getHorasTrabajo(), e.getEstado());
    }
}
