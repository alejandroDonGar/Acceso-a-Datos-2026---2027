package es.ejemplo.cinefest.dao;

import es.ejemplo.cinefest.modelo.Entrada;
import es.ejemplo.cinefest.orm.config.OrmFactory;
import es.ejemplo.cinefest.orm.entity.EntradaEntity;
import es.ejemplo.cinefest.orm.entity.EspectadorEntity;
import es.ejemplo.cinefest.orm.mapper.EntradaMapper;
import jakarta.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;


public class EntradaDao implements AutoCloseable {

    private static final Logger LOG = Logger.getLogger(EntradaDao.class.getName());
    private final EntityManagerFactory managerFactory;


    public EntradaDao(String jdbcUrl) {
        managerFactory = OrmFactory.crear(jdbcUrl);
    }

    private void registrar(String metodo, Exception ex) {
        LOG.log(Level.WARNING, metodo + " no se pudo completar: " + ex.getMessage());
    }

    private boolean validar(Entrada dato) {
        return dato != null && dato.getIdSesion() != null &&
                dato.getIdEspectador() != null && 
                dato.getFechaCompra() != null && !dato.getFechaCompra().isBlank() && 
                dato.getEstado() != null && dato.getEstado().isBlank();
    }

    // Usar transacciones y rollback en operaciones de escritura.
    public List<Entrada> listar() {
        try (var em = managerFactory.createEntityManager()) {
            return  em.createQuery("select e from EntradaEntity e order by e.idSesion", EntradaEntity.class)
            .getResultStream()
            .map(EntradaMapper::desdeEntidad)
            .toList();
        } catch (Exception e) {
        return List.of();
        }
    }
    public Optional<Entrada> buscarPorId(int id) {
        if(id<1) return Optional.empty();

        try (var em = managerFactory.createEntityManager()) {
            return Optional.ofNullable(em.find(EntradaEntity.class, id)).map(EntradaMapper::desdeEntidad);
        } catch (Exception e) {
            System.err.println(e);
            return Optional.empty();
        }

    }
    public int insertar(Entrada dato) {
        if(!validar(dato)) return 0;

        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                EntradaEntity entidad = EntradaMapper.aEntidad(dato);
                em.persist(entidad);
                em.flush();
                dato.setIdEntrada(entidad.getIdEntrada());
                tx.commit();
                return entidad.getIdEntrada();
            } catch (Exception e) {
                if (tx.isActive()) tx.rollback();
                throw e;
            }
        } catch (Exception e) {
            registrar("insertar", e);
            return 0;
        }
    }
    public int actualizar(Entrada dato) {
        if(!validar(dato) && dato.getIdEntrada()<1) return 0;

        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                EntradaEntity en = em.find(EntradaEntity.class, dato.getIdEntrada());
                if (en == null) {
                    tx.rollback();
                    return 0;
                }
                en.setFechaCompra(dato.getFechaCompra());
                en.setEstado(dato.getEstado());
                return 1;
            } catch (Exception e) {
                if (tx.isActive()) tx.rollback();
                throw e;
            }
        } catch (Exception e) {
            registrar("actualizar", e);
            return 0;
        }
    }
    public int eliminar(int id) {
        if (id < 1) return 0;

        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                EntradaEntity en = em.find(EntradaEntity.class, id);
                if (en == null) {
                    tx.rollback();
                    return 0;
                }
                em.remove(en);
                tx.commit();
                return 1;
            } catch (Exception e) {
                if (tx.isActive()) tx.rollback();
                throw e;
            }
        } catch (Exception e) {
            registrar("eliminar", e);
            return 0;
        }
    }
    public List<Object[]> entradasConDetalles() {
        // TODO: implementar conforme al contrato del README.md.
        return List.of();
    }

    public List<Object[]> entradasPorEspectador(int idEspectador) {
        if(idEspectador<1) return List.of();

        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select e from EntradaEntity e where idEspectador = :idEspectador",Object[].class)
            .setParameter("idEspectador", idEspectador)
            .getResultStream()
            .toList();
        } catch (Exception e) {
            System.err.println(e);
            return List.of();
        }
    }
    public List<Object[]> ventasPorDia() {
        // TODO: implementar conforme al contrato del README.md.
        return List.of();
    }
    public int cancelarEntrada(int idEntrada) {
        // TODO: implementar conforme al contrato del README.md.
        return 0;
    }
    /**
     * Cierra la EntityManagerFactory del DAO.
     */
    @Override
    public void close() {
        if (managerFactory.isOpen()) managerFactory.close();
    }

}
