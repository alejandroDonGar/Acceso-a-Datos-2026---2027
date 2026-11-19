package es.examen.sportrent.mapper;
import es.examen.sportrent.entity.AlquilerEntity;
import es.examen.sportrent.model.Alquiler;

/**
 * Convierte AlquilerEntity en el modelo Alquiler. No hay toEntity porque para crear la entidad
 * hacen falta el cliente y el equipo gestionados por el EntityManager, y eso lo hace el DAO
 * @author AlejandroDonGar
 */
public final class AlquilerMapper {
    private AlquilerMapper() { }

    /**
     * Pasa la entidad al modelo; hay que llamarlo con el EntityManager abierto (cliente y equipo son LAZY)
     * @param entidad alquiler leido de la base
     * @return el alquiler como modelo, con el dni del cliente y el codigo del equipo
     */
    public static Alquiler toDomain(AlquilerEntity entidad) {
        return new Alquiler(
                entidad.getId(),
                entidad.getCliente().getDni(),
                entidad.getEquipo().getCodigo(),
                entidad.getFechaInicio(),
                entidad.getFechaFin(),
                entidad.getFechaDevolucion()
        );
    }
}
