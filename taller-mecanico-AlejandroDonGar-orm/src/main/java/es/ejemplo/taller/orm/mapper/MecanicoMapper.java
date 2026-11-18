package es.ejemplo.taller.orm.mapper;

import es.ejemplo.taller.modelo.Mecanico;
import es.ejemplo.taller.orm.entity.MecanicoEntity;

/**
 * Convierte entre Mecanico (dominio) y MecanicoEntity (JPA); la tarifa se copia tal cual, sin redondear
 * @author AlejandroDonGar
 */
public class MecanicoMapper {
    public static MecanicoEntity toEntity(Mecanico m) {
        return new MecanicoEntity(m.getIdMecanico(), m.getNombre(), m.getEspecialidad(), m.getTarifaHora());
    }

    public static Mecanico toDomain(MecanicoEntity e) {
        return new Mecanico(e.getIdMecanico(), e.getNombre(), e.getEspecialidad(), e.getTarifaHora());
    }
}
