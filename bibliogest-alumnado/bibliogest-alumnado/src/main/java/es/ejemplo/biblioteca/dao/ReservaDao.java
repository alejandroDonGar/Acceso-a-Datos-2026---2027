package es.ejemplo.biblioteca.dao;
import es.ejemplo.biblioteca.modelo.Reserva;

import es.ejemplo.biblioteca.orm.mapper.ReservaMapper;
import es.ejemplo.biblioteca.orm.entity.ReservaEntity;
import es.ejemplo.biblioteca.orm.config.OrmFactory;
import jakarta.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * @author AlejandroDonGar
 * DAO de acceso a la tabla reservas mediante JPA (EntityManager y JPQL).
 * Los errores de persistencia se registran en el log y nunca se lanzan:
 * se devuelve 0, Optional.empty() o una lista vacia segun el metodo.
 */
public class ReservaDao implements AutoCloseable {
    private static final Logger LOG=Logger.getLogger(ReservaDao.class.getName());
    private final EntityManagerFactory managerFactory;
    private final boolean ownFactory;
    
    /**
     * Crea el DAO con su propia EntityManagerFactory, que se cierra en close()
     *
     * @param url URL JDBC de la base de datos SQLite
     */
    public ReservaDao(String url) {managerFactory=OrmFactory.crear(url);ownFactory=true;}
    
    /**
     * Crea el DAO usando una EntityManagerFactory compartida (no la cierra en close())
     *
     * @param factory Factoria de EntityManager ya creada
     */
    public ReservaDao(EntityManagerFactory factory) {managerFactory=factory;ownFactory=false;}
    private void registrar(String metodo,Exception ex) {LOG.log(Level.WARNING,metodo+" no se pudo completar: "+ex.getMessage());}
    
    /**
     * Busca todas las reservas de la bbdd
     *
     * @return Devuelve una lista con todas las reservas, o una lista vacia si no hay o hay un error
     */
    public List<Reserva> listar() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select r from ReservaEntity r order by r.idReserva", ReservaEntity.class)
            .getResultStream()
            .map(ReservaMapper::toDomain)
            .toList();
        } catch (Exception e) {
            registrar("listar", e);
            return List.of();
        }
    }

    /**
     * Busca un reserva por su identificador
     *
     * @param id Identificador del reserva
     * @return Devuelve el reserva encontrado, o Optional.empty() si no existe o hay un error
     */
    public Optional<Reserva> buscarPorId(Integer id) {
        if(id==null) return Optional.empty();

        try (var em = managerFactory.createEntityManager()){
            return Optional.ofNullable(em.find(ReservaEntity.class, id)).map(ReservaMapper::toDomain);
        } catch (Exception e) {
            registrar("buscarPorId", e);
            return Optional.empty();
        }
    }

    /**
     * Crea un nuevo reserva en la bbdd
     *
     * @param dato Datos del reserva a insertar (socio, libro, fecha y estado PENDIENTE, ATENDIDA o CANCELADA)
     * @return Devuelve el id generado del reserva, o 0 si los datos no son validos,
     *         se incumple una restriccion de la bbdd o hay un error (se hace rollback)
     */
    public int insertar(Reserva dato) {
        if (!validar(dato)) return 0;

        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                validar(dato);
                ReservaEntity entidad = ReservaMapper.toEntity(dato);
                em.persist(entidad);
                em.flush();
                dato.setIdReserva(entidad.getIdReserva());;
                tx.commit();
                return entidad.getIdReserva();
            } catch (Exception e) {
                if(tx.isActive()) tx.rollback();
                throw e;
            }
        } catch (Exception e) {
            registrar("insertar", e);
            return 0;
        }
    }

    /**
     * Actualiza la informacion de un reserva existente
     *
     * @param dato Datos nuevos del reserva, con su id
     * @return Devuelve 1 si se ha actualizado, o 0 si el reserva no existe,
     *         los datos no son validos o hay un error (se hace rollback)
     */
    public int actualizar(Reserva dato) {
        if (!validar(dato) || dato.getIdReserva() == null) return 0;

        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                ReservaEntity a = em.find(ReservaEntity.class, dato.getIdReserva());
                if(a==null) {
                    tx.commit();
                    return 0;
                }
                //Trabajamos con un nuevo clienteEntity porque cliente no se debe de conecatar a la bbdd, solo el clienteEntity
                ReservaEntity ReservaEntityActualizado = ReservaMapper.toEntity(dato);
                a.setIdSocio(ReservaEntityActualizado.getIdSocio());
                a.setIdLibro(ReservaEntityActualizado.getIdLibro());
                a.setFechaReserva(ReservaEntityActualizado.getFechaReserva());
                a.setEstado(ReservaEntityActualizado.getEstado());
                tx.commit();
                return 1;
            } catch (Exception e) {
                if(tx.isActive()) tx.rollback();
                throw e;
            }
        } catch (Exception e) {
            registrar("actualizar", e);
            return 0;
        }
    }

    /**
     * Borra un reserva de la bbdd
     *
     * @param id Identificador del reserva
     * @return Devuelve 1 si se ha borrado, o 0 si no existe, tiene registros
     *         que dependen de el (ON DELETE RESTRICT) o hay un error
     */
    public int eliminar(Integer id) {
        if(id==null||id<1) return 0;

        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                ReservaEntity a = em.find(ReservaEntity.class, id);
                if (a == null) {
                    tx.rollback();
                    return 0;
                }
                em.remove(a);
                tx.commit();
                return 1;
            } catch (Exception e) {
                if(tx.isActive()) tx.rollback();
                throw e;
            }
        } catch (Exception e) {
            registrar("eliminar", e);
            return 0;
        }
    }
    private boolean validar(Reserva dato) {return dato != null && dato.getIdSocio()!=null && dato.getIdLibro()!=null && dato.getFechaReserva()!=null && dato.getEstado()!=null && (dato.getEstado().equals("PENDIENTE") || dato.getEstado().equals("ATENDIDA") || dato.getEstado().equals("CANCELADA"));}
    
    /**
     * Lista las reservas en estado PENDIENTE
     *
     * @return Devuelve las reservas pendientes, o una lista vacia si no hay o hay un error
     */
    public List<Reserva> listarPendientes() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select r from ReservaEntity r where estado = 'PENDIENTE'", ReservaEntity.class)
                    .getResultStream()
                    .map(ReservaMapper::toDomain)
                    .toList();
        } catch (Exception e) {
            registrar("listarPendientes", e);
            return List.of();
        }
    }
    
    /**
     * Lista las reservas de un socio
     *
     * @param idSocio Identificador del socio
     * @return Devuelve las reservas del socio, o una lista vacia si no tiene o hay un error
     */
    public List<Reserva> buscarPorSocio(int idSocio) {
        if(idSocio<1)  return List.of();

        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select r from ReservaEntity r where idSocio = :idSocioAsociado", ReservaEntity.class)
                    .setParameter("idSocioAsociado", idSocio)
                    .getResultStream()
                    .map(ReservaMapper::toDomain)
                    .toList();
        } catch (Exception e) {
            registrar("buscarPorSocio", e);
            return List.of();
        }
    }
    
    /**
     * Lista las reservas de un libro
     *
     * @param idLibro Identificador del libro
     * @return Devuelve las reservas del libro, o una lista vacia si no tiene o hay un error
     */
    public List<Reserva> listarPorLibro(int idLibro) {
        if(idLibro<1)  return List.of();

        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select r from ReservaEntity r where idLibro = :idLibroReservados", ReservaEntity.class)
                    .setParameter("idLibroReservados", idLibro)
                    .getResultStream()
                    .map(ReservaMapper::toDomain)
                    .toList();
        } catch (Exception e) {
            registrar("listarPorLibro", e);
            return List.of();
        }
    }
    
    /**
     * Cuenta las reservas en estado PENDIENTE
     *
     * @return Devuelve el numero de reservas pendientes, o 0 si no hay o hay un error
     */
    public long contarReservasPendientes() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select count(r) from ReservaEntity r where estado = 'PENDIENTE'", Long.class)
                    .getSingleResult();
        } catch (Exception e) {
            registrar("buscarPorSocio", e);
            return 0;
        }
    }
    
    /**
     * Lista las reservas pendientes con el nombre del socio, el titulo del libro y su autor
     *
     * @return Devuelve filas con [0] nombre del socio, [1] titulo, [2] nombre del autor y [3] fecha de reserva,
     *         o una lista vacia si no hay o hay un error
     */
    public List<Object[]> reservasConDetalles() {
        try (var em = managerFactory.createEntityManager()) {
            // reserva -> socio, reserva -> libro -> autor (relaciones @ManyToOne)
            return em.createQuery("select s.nombre, l.titulo, a.nombre, r.fechaReserva from ReservaEntity r "
                    + "join r.socio s join r.libro l join l.autor a "
                    + "where r.estado = 'PENDIENTE' order by r.fechaReserva", Object[].class)
                    .getResultList();
        } catch (Exception e) {
            registrar("reservasConDetalles", e);
            return List.of();
        }
    }

    /**
     * Muestra la reserva pendiente mas antigua de cada libro (la que tiene prioridad)
     *
     * @return Devuelve filas con [0] titulo, [1] nombre del socio y [2] fecha de reserva, una por libro,
     *         o una lista vacia si no hay reservas pendientes o hay un error
     */
    public List<Object[]> prioridadReservas() {
        try (var em = managerFactory.createEntityManager()) {
            // La subconsulta busca la fecha mas antigua de las reservas pendientes de ESE libro
            return em.createQuery("select l.titulo, s.nombre, r.fechaReserva from ReservaEntity r "
                    + "join r.libro l join r.socio s where r.estado = 'PENDIENTE' and r.fechaReserva = "
                    + "(select min(r2.fechaReserva) from ReservaEntity r2 where r2.idLibro = r.idLibro and r2.estado = 'PENDIENTE') "
                    + "order by l.titulo", Object[].class)
                    .getResultList();
        } catch (Exception e) {
            registrar("prioridadReservas", e);
            return List.of();
        }
    }


    
    /**
     * Cierra la EntityManagerFactory solo si la ha creado este DAO
     */
    @Override public void close() {if(ownFactory && managerFactory.isOpen())managerFactory.close();}
}
