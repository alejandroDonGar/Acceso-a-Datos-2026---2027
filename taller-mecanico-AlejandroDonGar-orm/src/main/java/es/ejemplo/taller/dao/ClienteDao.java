package es.ejemplo.taller.dao;
import es.ejemplo.taller.modelo.Cliente;
import es.ejemplo.taller.orm.config.OrmFactory;
import es.ejemplo.taller.orm.entity.ClienteEntity;
import es.ejemplo.taller.orm.mapper.ClienteMapper;
import jakarta.persistence.EntityManagerFactory;
import java.util.*;
/**
 * @author AlejandroDonGar
 * Clase ClienteDao
 */
public class ClienteDao  {

    private final String url;
    private final EntityManagerFactory managerFactory;

    public ClienteDao(String url) {
        this.url = url;
        managerFactory = OrmFactory.crear(url);
    }
    
    public List<Cliente> listar() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select c from ClienteEntity c order by c.nombre", ClienteEntity.class)
                    .getResultStream()
                    .map(ClienteMapper::toDomain)
                    .toList();
        }
    }
    public Optional<Cliente> buscarPorId(Integer id) {
        if(id == null) return Optional.empty();

        try (var em = managerFactory.createEntityManager()) {
            return Optional.ofNullable(em.find(ClienteEntity.class, id)).map(ClienteMapper::toDomain);
        }
    }

    public ClienteEntity buscarPorEmail(String email) {
        if(email == null) return null;

        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select c from ClienteEntity c where c.email = :email",
                                    ClienteEntity.class).setParameter("email", email).getSingleResult();
        }
    }

    public int insertar(Cliente cliente) {
        if(cliente == null) return 0;

        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                ClienteEntity entidad = ClienteMapper.toEntity(cliente);
                em.persist(entidad);
                em.flush(); // Sirve cuando hay ids autogenerados.
                cliente.setIdCliente(entidad.getIdCliente());
                tx.commit();
                return cliente.getIdCliente();
            } catch (RuntimeException ex) {
                if (tx.isActive()) tx.rollback();
                throw ex;
            }
        }
    }
    public int actualizar(Cliente cliente) {
        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                ClienteEntity c = em.find(ClienteEntity.class, cliente.getIdCliente());
                if(c==null) {
                    tx.commit();
                    return 0;
                }
                //Trabajamos con un nuevo clienteEntity porque cliente no se debe de conecatar a la bbdd, solo el clienteEntity
                ClienteEntity clienteEntityActualizado = ClienteMapper.toEntity(cliente);
                c.setNombre(clienteEntityActualizado.getNombre());
                c.setEmail(clienteEntityActualizado.getEmail());
                c.setTelefono(clienteEntityActualizado.getTelefono());
                tx.commit();
                return 1;
            } catch (RuntimeException ex) {
                if(tx.isActive()) tx.rollback();
                throw ex;
            }
        }
    }
    public int eliminar(Integer id) {
        if(buscarPorId(id).isEmpty()) return 0;

        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                ClienteEntity c = em.find(ClienteEntity.class, id);
                tx.commit();
                return 1;
            } catch (RuntimeException ex) {
                if(tx.isActive()) tx.rollback();
                throw ex;
            }
        }
    }
}
