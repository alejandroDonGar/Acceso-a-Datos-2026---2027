package es.ejemplo.biblioteca.dao;
import es.ejemplo.biblioteca.modelo.Prestamo;

import es.ejemplo.biblioteca.orm.mapper.PrestamoMapper;
import es.ejemplo.biblioteca.orm.entity.PrestamoEntity;
import es.ejemplo.biblioteca.orm.entity.SocioEntity;
import es.ejemplo.biblioteca.orm.entity.LibroEntity;
import es.ejemplo.biblioteca.orm.config.OrmFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * @author AlejandroDonGar
 * DAO de acceso a la tabla prestamos mediante JPA (EntityManager y JPQL).
 * Los errores de persistencia se registran en el log y nunca se lanzan:
 * se devuelve 0, Optional.empty() o una lista vacia segun el metodo.
 */
public class PrestamoDao implements AutoCloseable {
    private static final Logger LOG=Logger.getLogger(PrestamoDao.class.getName());
    private final EntityManagerFactory managerFactory;
    private final boolean ownFactory;
    
    /**
     * Crea el DAO con su propia EntityManagerFactory, que se cierra en close()
     *
     * @param url URL JDBC de la base de datos SQLite
     */
    public PrestamoDao(String url) {managerFactory=OrmFactory.crear(url);ownFactory=true;}
    
    /**
     * Crea el DAO usando una EntityManagerFactory compartida (no la cierra en close())
     *
     * @param factory Factoria de EntityManager ya creada
     */
    public PrestamoDao(EntityManagerFactory factory) {managerFactory=factory;ownFactory=false;}
    private void registrar(String metodo,Exception ex) {LOG.log(Level.WARNING,metodo+" no se pudo completar: "+ex.getMessage());}
    
    /**
     * Busca todos los prestamos de la bbdd
     *
     * @return Devuelve una lista con todos los prestamos, o una lista vacia si no hay o hay un error
     */
    public List<Prestamo> listar() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select p from PrestamoEntity p order by p.idPrestamo", PrestamoEntity.class)
            .getResultStream()
            .map(PrestamoMapper::toDomain)
            .toList();
        } catch (Exception e) {
            registrar("listar", e);
            return List.of();
        }
    }

    /**
     * Busca un prestamo por su identificador
     *
     * @param id Identificador del prestamo
     * @return Devuelve el prestamo encontrado, o Optional.empty() si no existe o hay un error
     */
    public Optional<Prestamo> buscarPorId(Integer id) {
        if(id==null) return Optional.empty();

        try (var em = managerFactory.createEntityManager()){
            return Optional.ofNullable(em.find(PrestamoEntity.class, id)).map(PrestamoMapper::toDomain);
        } catch (Exception e) {
            registrar("buscarPorId", e);
            return Optional.empty();
        }
    }

    /**
     * Crea un nuevo prestamo en la bbdd
     *
     * @param dato Datos del prestamo a insertar (socio, libro y fechas coherentes)
     * @return Devuelve el id generado del prestamo, o 0 si los datos no son validos,
     *         se incumple una restriccion de la bbdd o hay un error (se hace rollback)
     */
    public int insertar(Prestamo dato) {
        if (!validar(dato)) return 0;

        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                validar(dato);
                PrestamoEntity entidad = PrestamoMapper.toEntity(dato);
                em.persist(entidad);
                em.flush();
                dato.setIdPrestamo(entidad.getIdPrestamo());;
                tx.commit();
                return entidad.getIdPrestamo();
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
     * Actualiza la informacion de un prestamo existente
     *
     * @param dato Datos nuevos del prestamo, con su id
     * @return Devuelve 1 si se ha actualizado, o 0 si el prestamo no existe,
     *         los datos no son validos o hay un error (se hace rollback)
     */
    public int actualizar(Prestamo dato) {
        if (!validar(dato) || dato.getIdPrestamo() == null) return 0;

        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                PrestamoEntity a = em.find(PrestamoEntity.class, dato.getIdPrestamo());
                if(a==null) {
                    tx.commit();
                    return 0;
                }
                //Trabajamos con un nuevo clienteEntity porque cliente no se debe de conecatar a la bbdd, solo el clienteEntity
                PrestamoEntity PrestamoEntityActualizado = PrestamoMapper.toEntity(dato);
                a.setIdSocio(PrestamoEntityActualizado.getIdSocio());
                a.setIdLibro(PrestamoEntityActualizado.getIdLibro());
                a.setFechaPrestamo(PrestamoEntityActualizado.getFechaPrestamo());
                a.setFechaLimite(PrestamoEntityActualizado.getFechaLimite());
                a.setFechaDevolucion(PrestamoEntityActualizado.getFechaDevolucion());
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
     * Borra un prestamo de la bbdd
     *
     * @param id Identificador del prestamo
     * @return Devuelve 1 si se ha borrado, o 0 si no existe, tiene registros
     *         que dependen de el (ON DELETE RESTRICT) o hay un error
     */
    public int eliminar(Integer id) {
        if(id==null||id<1) return 0;

        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                PrestamoEntity a = em.find(PrestamoEntity.class, id);
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
    private boolean validar(Prestamo dato) {return dato != null && dato.getIdSocio()!=null && dato.getIdLibro()!=null && dato.getFechaPrestamo()!=null && dato.getFechaLimite()!=null && dato.getFechaLimite().compareTo(dato.getFechaPrestamo())>=0 && (dato.getFechaDevolucion()==null || dato.getFechaDevolucion().compareTo(dato.getFechaPrestamo())>=0);}
    
    /**
     * Lista los prestamos activos (sin devolver) de un socio
     *
     * @param idSocio Identificador del socio
     * @return Devuelve los prestamos activos del socio ordenados por fecha, o una lista vacia si no tiene o hay un error
     */
    public List<Prestamo> listarPorSocio(int idSocio) {
        if(idSocio<1) return List.of();

        try (var em = managerFactory.createEntityManager()) {
            // Un prestamo esta activo cuando todavia no tiene fecha de devolucion
            return em.createQuery("select p from PrestamoEntity p where p.idSocio = :idSocio and p.fechaDevolucion is null order by p.fechaPrestamo", PrestamoEntity.class)
                    .setParameter("idSocio", idSocio)
                    .getResultStream()
                    .map(PrestamoMapper::toDomain)
                    .toList();
        } catch (Exception e) {
            registrar("listarPorSocio", e);
            return List.of();
        }
    }

    /**
     * Lista todos los prestamos que todavia no se han devuelto
     *
     * @return Devuelve los prestamos activos, o una lista vacia si no hay o hay un error
     */
    public List<Prestamo> listarActivos() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select p from PrestamoEntity p where p.fechaDevolucion is null order by p.fechaPrestamo", PrestamoEntity.class)
                    .getResultStream()
                    .map(PrestamoMapper::toDomain)
                    .toList();
        } catch (Exception e) {
            registrar("listarActivos", e);
            return List.of();
        }
    }

    /**
     * Lista los prestamos vencidos en una fecha: siguen sin devolver y su fecha limite ya ha pasado
     *
     * @param fecha Fecha de referencia en formato YYYY-MM-DD
     * @return Devuelve los prestamos vencidos, o una lista vacia si no hay, la fecha es nula o hay un error
     */
    public List<Prestamo> listarVencidos(String fecha) {
        if(fecha==null || fecha.isBlank()) return List.of();

        try (var em = managerFactory.createEntityManager()) {
            // Con el formato YYYY-MM-DD comparar textos es lo mismo que comparar fechas
            return em.createQuery("select p from PrestamoEntity p where p.fechaDevolucion is null and p.fechaLimite < :fecha order by p.fechaLimite", PrestamoEntity.class)
                    .setParameter("fecha", fecha)
                    .getResultStream()
                    .map(PrestamoMapper::toDomain)
                    .toList();
        } catch (Exception e) {
            registrar("listarVencidos", e);
            return List.of();
        }
    }

    /**
     * Cuenta los prestamos historicos (devueltos o no) de un libro
     *
     * @param idLibro Identificador del libro
     * @return Devuelve el numero de prestamos del libro, o 0 si no tiene o hay un error
     */
    public long contarPrestamosPorLibro(int idLibro) {
        if(idLibro<1) return 0;

        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select count(p) from PrestamoEntity p where p.idLibro = :idLibro",Long.class)
                    .setParameter("idLibro",idLibro)
                    .getSingleResult();
        } catch (Exception e) {
            registrar("contarPrestamosPorLibro", e);
            return 0;
        }
    }
    
    /**
     * Lista los prestamos junto al nombre del socio y el titulo del libro
     *
     * @return Devuelve filas con [0] nombre del socio, [1] titulo del libro, [2] fecha de prestamo,
     *         [3] fecha limite y [4] fecha de devolucion (null si no se ha devuelto), o una lista vacia si hay un error
     */
    public List<Object[]> prestamosConSocioYLibro() {
        try (var em = managerFactory.createEntityManager()) {
            // Navegamos por las relaciones @ManyToOne de PrestamoEntity: p.socio y p.libro
            return em.createQuery("select s.nombre, l.titulo, p.fechaPrestamo, p.fechaLimite, p.fechaDevolucion from PrestamoEntity p join p.socio s join p.libro l order by p.fechaPrestamo", Object[].class)
                    .getResultList();
        } catch (Exception e) {
            registrar("prestamosConSocioYLibro", e);
            return List.of();
        }
    }

    /**
     * Ranking de los libros mas prestados en un mes
     *
     * @param anioMes Mes en formato YYYY-MM (por ejemplo 2025-01)
     * @param limite Numero maximo de libros que se devuelven
     * @return Devuelve filas con [0] titulo, [1] nombre del autor y [2] numero de prestamos (Long), de mas a menos,
     *         o una lista vacia si los datos no son validos o hay un error
     */
    public List<Object[]> rankingMensual(String anioMes, int limite) {
        if(anioMes==null || anioMes.isBlank() || limite<1) return List.of();

        try (var em = managerFactory.createEntityManager()) {
            // like '2025-01%' -> todos los prestamos cuya fecha empieza por ese mes
            return em.createQuery("select l.titulo, a.nombre, count(p) from PrestamoEntity p join p.libro l join l.autor a where p.fechaPrestamo like concat(:mes, '%') group by l.idLibro, l.titulo, a.nombre order by count(p) desc", Object[].class)
                    .setParameter("mes", anioMes)
                    .setMaxResults(limite) // equivale al LIMIT de SQL
                    .getResultList();
        } catch (Exception e) {
            registrar("rankingMensual", e);
            return List.of();
        }
    }

    /**
     * Registra un prestamo de forma atomica. Solo se crea si el socio existe y esta activo,
     * el libro existe y le queda al menos un ejemplar disponible. Es synchronized para que dos
     * hilos no puedan comprobar a la vez que queda un ejemplar y prestarlo los dos.
     *
     * @param idSocio Identificador del socio
     * @param idLibro Identificador del libro
     * @param inicio Fecha del prestamo (YYYY-MM-DD)
     * @param limite Fecha limite de devolucion (YYYY-MM-DD), no puede ser anterior al inicio
     * @return Devuelve el id del prestamo creado, o 0 si no se cumple alguna condicion o hay un error (se hace rollback)
     */
    public synchronized int registrarPrestamo(int idSocio, int idLibro, String inicio, String limite) {
        // ponytail: synchronized solo protege dentro de este DAO; con varios procesos haria falta un bloqueo en la BD
        if(inicio==null || limite==null || limite.compareTo(inicio)<0) return 0;

        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                if(!sePuedePrestar(em, idSocio, idLibro)) {
                    tx.rollback();
                    return 0;
                }
                PrestamoEntity entidad = PrestamoMapper.toEntity(new Prestamo(null, idSocio, idLibro, inicio, limite, null));
                em.persist(entidad);
                em.flush();
                tx.commit();
                return entidad.getIdPrestamo();
            } catch (Exception e) {
                if(tx.isActive()) tx.rollback();
                throw e;
            }
        } catch (Exception e) {
            registrar("registrarPrestamo", e);
            return 0;
        }
    }

    // Comprueba las reglas del prestamo dentro de la transaccion abierta
    private boolean sePuedePrestar(EntityManager em, int idSocio, int idLibro) {
        SocioEntity socio = em.find(SocioEntity.class, idSocio);
        if(socio==null || !Boolean.TRUE.equals(socio.getActivo())) return false;

        LibroEntity libro = em.find(LibroEntity.class, idLibro);
        if(libro==null) return false;

        // Disponibles = ejemplares - prestamos activos (sin fecha de devolucion)
        long activos = em.createQuery("select count(p) from PrestamoEntity p where p.idLibro = :idLibro and p.fechaDevolucion is null", Long.class)
                .setParameter("idLibro", idLibro)
                .getSingleResult();
        return activos < libro.getEjemplares();
    }

    /**
     * Marca un prestamo como devuelto guardando la fecha de devolucion
     *
     * @param id Identificador del prestamo
     * @param fecha Fecha de devolucion (YYYY-MM-DD), no puede ser anterior a la fecha del prestamo
     * @return Devuelve 1 si se ha devuelto, o 0 si el prestamo no existe, ya estaba devuelto,
     *         la fecha no es valida o hay un error
     */
    public int devolver(int id, String fecha) {
        if(id<1 || fecha==null || fecha.isBlank()) return 0;

        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                PrestamoEntity p = em.find(PrestamoEntity.class, id);
                // No existe, ya estaba devuelto o la fecha es anterior al prestamo
                if(p==null || p.getFechaDevolucion()!=null || fecha.compareTo(p.getFechaPrestamo())<0) {
                    tx.rollback();
                    return 0;
                }
                p.setFechaDevolucion(fecha); // la entidad esta gestionada: Hibernate hace el UPDATE en el commit
                tx.commit();
                return 1;
            } catch (Exception e) {
                if(tx.isActive()) tx.rollback();
                throw e;
            }
        } catch (Exception e) {
            registrar("devolver", e);
            return 0;
        }
    }

    
    /**
     * Cierra la EntityManagerFactory solo si la ha creado este DAO
     */
    @Override public void close() {if(ownFactory && managerFactory.isOpen())managerFactory.close();}
}
