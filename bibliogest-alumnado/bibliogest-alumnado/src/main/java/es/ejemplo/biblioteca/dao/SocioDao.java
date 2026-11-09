package es.ejemplo.biblioteca.dao;
import es.ejemplo.biblioteca.modelo.Socio;

import es.ejemplo.biblioteca.orm.mapper.SocioMapper;
import es.ejemplo.biblioteca.orm.entity.SocioEntity;
import es.ejemplo.biblioteca.orm.config.OrmFactory;
import jakarta.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * @author AlejandroDonGar
 * DAO de acceso a la tabla socios mediante JPA (EntityManager y JPQL).
 * Los errores de persistencia se registran en el log y nunca se lanzan:
 * se devuelve 0, Optional.empty() o una lista vacia segun el metodo.
 */
public class SocioDao implements AutoCloseable {
    private static final Logger LOG=Logger.getLogger(SocioDao.class.getName());
    private final EntityManagerFactory managerFactory;
    private final boolean ownFactory;
    
    /**
     * Crea el DAO con su propia EntityManagerFactory, que se cierra en close()
     *
     * @param url URL JDBC de la base de datos SQLite
     */
    public SocioDao(String url) {managerFactory=OrmFactory.crear(url);ownFactory=true;}
    
    /**
     * Crea el DAO usando una EntityManagerFactory compartida (no la cierra en close())
     *
     * @param factory Factoria de EntityManager ya creada
     */
    public SocioDao(EntityManagerFactory factory) {managerFactory=factory;ownFactory=false;}
    private void registrar(String metodo,Exception ex) {LOG.log(Level.WARNING,metodo+" no se pudo completar: "+ex.getMessage());}
    
    /**
     * Busca todos los socios de la bbdd
     *
     * @return Devuelve una lista con todos los socios, o una lista vacia si no hay o hay un error
     */
    public List<Socio> listar() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select s from SocioEntity s order by s.nombre", SocioEntity.class)
            .getResultStream()
            .map(SocioMapper::toDomain)
            .toList();
        } catch (Exception e) {
            registrar("listar", e);
            return List.of();
        }
    }

    /**
     * Busca un socio por su identificador
     *
     * @param id Identificador del socio
     * @return Devuelve el socio encontrado, o Optional.empty() si no existe o hay un error
     */
    public Optional<Socio> buscarPorId(Integer id) {
        if(id==null) return Optional.empty();

        try (var em = managerFactory.createEntityManager()){
            return Optional.ofNullable(em.find(SocioEntity.class, id)).map(SocioMapper::toDomain);
        } catch (Exception e) {
            registrar("buscarPorId", e);
            return Optional.empty();
        }
    }

    /**
     * Crea un nuevo socio en la bbdd
     *
     * @param dato Datos del socio a insertar (nombre, email, fecha de alta y activo)
     * @return Devuelve el id generado del socio, o 0 si los datos no son validos,
     *         se incumple una restriccion de la bbdd o hay un error (se hace rollback)
     */
    public int insertar(Socio dato) {
        if (!validar(dato)) return 0;

        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                validar(dato);
                SocioEntity entidad = SocioMapper.toEntity(dato);
                em.persist(entidad);
                em.flush();
                dato.setIdSocio(entidad.getIdSocio());;
                tx.commit();
                return entidad.getIdSocio();
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
     * Actualiza la informacion de un socio existente
     *
     * @param dato Datos nuevos del socio, con su id
     * @return Devuelve 1 si se ha actualizado, o 0 si el socio no existe,
     *         los datos no son validos o hay un error (se hace rollback)
     */
    public int actualizar(Socio dato) {
        if (!validar(dato) || dato.getIdSocio() == null) return 0;

        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                SocioEntity a = em.find(SocioEntity.class, dato.getIdSocio());
                if(a==null) {
                    tx.commit();
                    return 0;
                }
                //Trabajamos con un nuevo clienteEntity porque cliente no se debe de conecatar a la bbdd, solo el clienteEntity
                SocioEntity SocioEntityActualizado = SocioMapper.toEntity(dato);
                a.setNombre(SocioEntityActualizado.getNombre());
                a.setEmail(SocioEntityActualizado.getEmail());
                a.setTelefono(SocioEntityActualizado.getTelefono());
                a.setFechaAlta(SocioEntityActualizado.getFechaAlta());
                a.setActivo(SocioEntityActualizado.getActivo());
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
     * Borra un socio de la bbdd
     *
     * @param id Identificador del socio
     * @return Devuelve 1 si se ha borrado, o 0 si no existe, tiene registros
     *         que dependen de el (ON DELETE RESTRICT) o hay un error
     */
    public int eliminar(Integer id) {
        if(id==null||id<1) return 0;

        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                SocioEntity a = em.find(SocioEntity.class, id);
                if (a == null) {
                    tx.rollback();
                    return 0;
                }
                // Si el socio tiene prestamos o reservas, la FK (ON DELETE RESTRICT) hace fallar el commit
                // y el catch hace rollback y devuelve 0
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
    private boolean validar(Socio dato) {return dato != null && dato.getNombre()!=null && !dato.getNombre().isBlank() && dato.getEmail()!=null && !dato.getEmail().isBlank() && dato.getFechaAlta()!=null && dato.getActivo()!=null;}
    
    /**
     * Lista los socios activos, de alta mas reciente a mas antigua
     *
     * @return Devuelve los socios activos, o una lista vacia si no hay o hay un error
     */
    public List<Socio> listarActivos() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select s from SocioEntity s where s.activo = true order by s.fechaAlta desc", SocioEntity.class)
                    .getResultStream()
                    .map(SocioMapper::toDomain)
                    .toList();
        } catch (Exception e) {
            registrar("listarActivos", e);
            return List.of();
        }
    }
    
    /**
     * Busca un socio por su correo electronico
     *
     * @param email Correo del socio
     * @return Devuelve el socio encontrado, o Optional.empty() si no existe, el email esta vacio o hay un error
     */
    public Optional<Socio> buscarPorEmail(String email) {
        if(email==null || email.isEmpty()) return Optional.empty();

        try (var em = managerFactory.createEntityManager()){
            return Optional.ofNullable(em.createQuery("select s from SocioEntity s where email = :emailBuscar", SocioEntity.class)
                    .setParameter("emailBuscar", email)
                    .getSingleResult())
                    .map(SocioMapper::toDomain);
        } catch (Exception e) {
            registrar("buscarPorEmail", e);
            return Optional.empty();
        }
    }
    
    /**
     * Lista los socios que en una fecha tienen algun prestamo vencido (sin devolver y con la fecha limite pasada)
     *
     * @param fecha Fecha de referencia en formato YYYY-MM-DD
     * @return Devuelve los socios sin repetir, o una lista vacia si no hay, la fecha es nula o hay un error
     */
    public List<Socio> listarConPrestamosVencidos(String fecha) {
        if(fecha==null || fecha.isBlank()) return List.of();

        try (var em = managerFactory.createEntityManager()) {
            // distinct: un socio con varios prestamos vencidos solo debe salir una vez
            return em.createQuery("select distinct s from PrestamoEntity p join p.socio s where p.fechaDevolucion is null and p.fechaLimite < :fecha order by s.nombre", SocioEntity.class)
                    .setParameter("fecha", fecha)
                    .getResultStream()
                    .map(SocioMapper::toDomain)
                    .toList();
        } catch (Exception e) {
            registrar("listarConPrestamosVencidos", e);
            return List.of();
        }
    }

    /**
     * Lista los socios que nunca han hecho ningun prestamo
     *
     * @return Devuelve los socios sin prestamos, o una lista vacia si no hay o hay un error
     */
    public List<Socio> sociosSinPrestamos() {
        try (var em = managerFactory.createEntityManager()) {
            // Subconsulta: ids de socios que aparecen en prestamos; nos quedamos con los que NO estan
            return em.createQuery("select s from SocioEntity s where s.idSocio not in (select p.idSocio from PrestamoEntity p) order by s.nombre", SocioEntity.class)
                    .getResultStream()
                    .map(SocioMapper::toDomain)
                    .toList();
        } catch (Exception e) {
            registrar("sociosSinPrestamos", e);
            return List.of();
        }
    }

    /**
     * Ranking de los socios con mas prestamos historicos
     *
     * @param limite Numero maximo de socios que se devuelven
     * @return Devuelve filas con [0] nombre del socio y [1] numero de prestamos (Long), de mas a menos,
     *         o una lista vacia si el limite no es valido o hay un error
     */
    public List<Object[]> rankingSocios(int limite) {
        if(limite<1) return List.of();

        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select s.nombre, count(p) from PrestamoEntity p join p.socio s group by s.idSocio, s.nombre order by count(p) desc", Object[].class)
                    .setMaxResults(limite) // equivale al LIMIT de SQL
                    .getResultList();
        } catch (Exception e) {
            registrar("rankingSocios", e);
            return List.of();
        }
    }

    /**
     * Historial completo de prestamos de un socio, indicando si se devolvio y si hubo retraso
     *
     * @param idSocio Identificador del socio
     * @return Devuelve filas con [0] titulo, [1] fecha de prestamo, [2] fecha limite, [3] fecha de devolucion,
     *         [4] "DEVUELTO" o "PENDIENTE" y [5] "SI" si se devolvio despues de la fecha limite o "NO" en otro caso,
     *         o una lista vacia si no tiene prestamos o hay un error
     */
    public List<Object[]> historialSocio(int idSocio) {
        if(idSocio<1) return List.of();

        try (var em = managerFactory.createEntityManager()) {
            // case when ... then ... else ... end funciona como un if dentro de la consulta
            return em.createQuery("select l.titulo, p.fechaPrestamo, p.fechaLimite, p.fechaDevolucion, "
                    + "case when p.fechaDevolucion is null then 'PENDIENTE' else 'DEVUELTO' end, "
                    + "case when p.fechaDevolucion > p.fechaLimite then 'SI' else 'NO' end "
                    + "from PrestamoEntity p join p.libro l where p.idSocio = :idSocio order by p.fechaPrestamo", Object[].class)
                    .setParameter("idSocio", idSocio)
                    .getResultList();
        } catch (Exception e) {
            registrar("historialSocio", e);
            return List.of();
        }
    }


    
    /**
     * Cierra la EntityManagerFactory solo si la ha creado este DAO
     */
    @Override public void close() {if(ownFactory && managerFactory.isOpen())managerFactory.close();}
}
