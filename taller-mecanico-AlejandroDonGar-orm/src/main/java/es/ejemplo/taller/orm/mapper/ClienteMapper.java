package es.ejemplo.taller.orm.mapper;

import es.ejemplo.taller.modelo.Cliente;
import es.ejemplo.taller.orm.entity.ClienteEntity;

/**
 * Convierte entre Cliente (dominio) y ClienteEntity (JPA)
 * @author AlejandroDonGar
 */
public class ClienteMapper {

    public static ClienteEntity toEntity(Cliente cliente) {
        return new ClienteEntity(cliente.getIdCliente(), cliente.getNombre(),
                cliente.getEmail(), cliente.getTelefono());
    }

    public static Cliente toDomain(ClienteEntity entidad) {
        return new Cliente(entidad.getIdCliente(), entidad.getNombre(),
                entidad.getEmail(), entidad.getTelefono());
    }
}
