package es.ejemplo.taller.orm.mapper;

import es.ejemplo.taller.modelo.PiezaReparacion;
import es.ejemplo.taller.orm.entity.PiezaReparacionEntity;

/**
 * Convierte entre PiezaReparacion y PiezaReparacionEntity usando la FK plana idReparacion
 * @author AlejandroDonGar
 */
public class PiezaReparacionMapper {
    public static PiezaReparacionEntity toEntity(PiezaReparacion p) {
        return new PiezaReparacionEntity(p.getIdPieza(), p.getIdReparacion(), p.getNombre(),
                p.getCantidad(), p.getPrecioUnitario());
    }

    public static PiezaReparacion toDomain(PiezaReparacionEntity e) {
        return new PiezaReparacion(e.getIdPieza(), e.getIdReparacion(), e.getNombre(),
                e.getCantidad(), e.getPrecioUnitario());
    }
}
