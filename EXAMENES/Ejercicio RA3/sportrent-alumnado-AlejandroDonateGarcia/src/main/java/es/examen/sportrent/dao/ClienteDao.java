package es.examen.sportrent.dao;
import es.examen.sportrent.entity.ClienteEntity;
import es.examen.sportrent.infra.OrmFactory;
import es.examen.sportrent.mapper.ClienteMapper;
import es.examen.sportrent.model.Cliente;
import es.examen.sportrent.model.ClienteAlquileres;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClienteDao {
    private final EntityManagerFactory managerFactory;

    public ClienteDao() {
        String url = "jdbc:sqlite:datos/sportrent.db";
        managerFactory = OrmFactory.crear(url);
    }

    public List<Cliente> listarTodos() {
        try (var em = managerFactory.createEntityManager()){
            return em.createQuery("select c from ClienteEntity c order by c.dni asc", ClienteEntity.class)
                    .getResultStream()
                    .map(ClienteMapper::toDomain)
                    .toList();
        } catch (Exception e) {
            System.err.println(e);
            return List.of();
        }
    }

    public Cliente buscarPorId(String dni) {
        if(dni == null || dni.isBlank()) return null;

        try (var em = managerFactory.createEntityManager()) {
            return ClienteMapper.toDomain(em.find(ClienteEntity.class, dni));
        } catch (Exception e) {
            System.err.println(e);
            return null;
        }
    }

    public boolean insertar(Cliente m) {
        if(m==null) return false;

        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                ClienteEntity ce = ClienteMapper.toEntity(m);
                em.persist(ce);
                em.flush();
                m.setDni(ce.getDni());
                tx.commit();
                return true;
            } catch (Exception e) {
                if(tx.isActive()) tx.rollback();
                throw e;
            }
        } catch (Exception e) {
            System.err.println(e);
            return false;
        }
    }

    public boolean actualizar(Cliente m) {
        if(m==null) return false;

        try (var em = managerFactory.createEntityManager()){
            var tx = em.getTransaction();
            try {
                tx.begin();
                ClienteEntity ce = em.find(ClienteEntity.class, m.getDni());
                if(ce == null) {
                    tx.commit();
                    return false;
                }
                ClienteEntity ceActualizado = ClienteMapper.toEntity(m);
                ce.setDni(ceActualizado.getDni());
                ce.setNombre(ceActualizado.getNombre());
                ce.setEmail(ceActualizado.getEmail());
                ce.setTelefono(ceActualizado.getTelefono());
                tx.commit();
                return true;
            } catch (Exception e) {
                if(tx.isActive()) tx.rollback();
                throw e;
            }
        } catch (Exception e) {
            System.err.println(e);
            return false;
        }
    }

    public boolean eliminar(String id) {
        if(id==null || id.isBlank()) return false;

        try (var em = managerFactory.createEntityManager()){
            var tx = em.getTransaction();
            try {
                tx.begin();
                ClienteEntity ce = em.find(ClienteEntity.class, id);
                if(ce == null) {
                    tx.rollback();
                    return false;
                }
                em.remove(ce);
                tx.commit();
                return true;
            } catch (Exception e) {
                if(tx.isActive()) tx.rollback();
                throw e;
            }
        } catch (Exception e) {
            System.err.println(e);
            return false;
        }
    }

    public List<ClienteAlquileres> alquileresPorCliente() {
        List<ClienteAlquileres> alquileresPorCliente = new ArrayList<>();
        try (var em = managerFactory.createEntityManager()){
            ClienteAlquileres cliAlq = (ClienteAlquileres) em.createQuery("select c.dni, c.nombre, count(a) from ClienteEntity c join AlquilerEntity a group by c.dni");
            alquileresPorCliente.add(cliAlq);
            return alquileresPorCliente;
        } catch (Exception e) {
            System.err.println(e);
            return new ArrayList<>();
        }
    }
    
    public List<ClienteAlquileres> clientesConMasDeDosAlquileres() {
        throw new UnsupportedOperationException("No implementado");
    }

    public List<ClienteAlquileres> clientesSuperanReferencia(String dniReferencia) {
        throw new UnsupportedOperationException("No implementado");    }
}
