package es.examen.sportrent.mapper;
import es.examen.sportrent.entity.*;
import es.examen.sportrent.model.*;

public final class ClienteMapper {
    private ClienteMapper(){}

    public static ClienteEntity toEntity(Cliente dato) {
        return new ClienteEntity(
                dato.getDni(),
                dato.getNombre(),
                dato.getEmail(),
                dato.getTelefono()
        );
    }

    public static Cliente toDomain(ClienteEntity entidad) {
        return new Cliente(
                entidad.getDni(),
                entidad.getNombre(),
                entidad.getEmail(),
                entidad.getTelefono()
        );
    }
}
