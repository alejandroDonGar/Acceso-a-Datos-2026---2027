package es.ejemplo.taller.dao;
import es.ejemplo.taller.modelo.Cliente;
import es.ejemplo.taller.orm.entity.ClienteEntity;
import es.ejemplo.taller.orm.mapper.ClienteMapper;
import java.util.*;

/**
 * DAO de clientes con JPA. listar, buscarPorId y eliminar vienen de AbstractDao
 * @author AlejandroDonGar
 */
public class ClienteDao extends AbstractDao<Cliente, ClienteEntity, Integer> {

    public ClienteDao(String url) {
        super(url, ClienteEntity.class, ClienteMapper::toDomain,
                "select c from ClienteEntity c order by c.nombre");
    }

    public Optional<Cliente> buscarPorEmail(String email) {
        if (!texto(email)) return Optional.empty();
        return consultar("select c from ClienteEntity c where c.email = :email", Map.of("email", email))
                .stream().findFirst();
    }

    public int insertar(Cliente cliente) {
        if (!esValido(cliente)) return 0;
        return escribir("insertar", em -> {
            ClienteEntity entidad = ClienteMapper.toEntity(cliente);
            em.persist(entidad);
            em.flush(); // Sirve cuando hay ids autogenerados: fuerza el INSERT y ya tenemos el id
            cliente.setIdCliente(entidad.getIdCliente());
            return entidad.getIdCliente();
        });
    }

    public int actualizar(Cliente cliente) {
        if (!esValido(cliente) || cliente.getIdCliente() == null) return 0;
        return escribir("actualizar", em -> {
            ClienteEntity c = em.find(ClienteEntity.class, cliente.getIdCliente());
            if (c == null) return 0;
            // c esta administrada por el EntityManager: al hacer commit se guardan sus cambios sin llamar a merge
            c.setNombre(cliente.getNombre());
            c.setEmail(cliente.getEmail());
            c.setTelefono(cliente.getTelefono());
            return 1;
        });
    }

    // Nombre y email obligatorios; el telefono puede ser null
    private boolean esValido(Cliente cliente) {
        return cliente != null && texto(cliente.getNombre()) && texto(cliente.getEmail());
    }
}
