package es.ejemplo.taller.orm.mapper;

import es.ejemplo.taller.modelo.Vehiculo;
import es.ejemplo.taller.orm.entity.VehiculoEntity;

/**
 * Convierte entre Vehiculo y VehiculoEntity usando la FK plana idCliente, sin cargar la relacion LAZY
 * @author AlejandroDonGar
 */
public class VehiculoMapper {
    public static VehiculoEntity toEntity(Vehiculo vehiculo) {
        return new VehiculoEntity(
                vehiculo.getMatricula(),
                vehiculo.getMarca(),
                vehiculo.getModelo(),
                vehiculo.getAnio(),
                vehiculo.getIdCliente()
        );
    }
    public static Vehiculo toDomain(VehiculoEntity entity) {
        return new Vehiculo(
                entity.getMatricula(),
                entity.getMarca(),
                entity.getModelo(),
                entity.getAnio(),
                entity.getIdCliente()
        );
    }
}