package es.ejemplo.cinefest.dao;

import es.ejemplo.cinefest.modelo.Sesion;
import es.ejemplo.cinefest.orm.config.OrmFactory;
import es.ejemplo.cinefest.orm.entity.EntradaEntity;
import es.ejemplo.cinefest.orm.entity.EspectadorEntity;
import es.ejemplo.cinefest.orm.entity.SalaEntity;
import es.ejemplo.cinefest.orm.entity.SesionEntity;
import es.ejemplo.cinefest.orm.mapper.SesionMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author AlejandroDonGar
 * Acceso a la tabla sesiones con JPA (EntityManager y JPQL).
 * Los errores se registran en el log y no se lanzan: los metodos
 * devuelven 0, Optional.empty() o una lista vacia.
 */
public class SesionDao implements AutoCloseable {

    private static final Logger LOG = Logger.getLogger(SesionDao.class.getName());
    private static final List<String> ESTADOS = List.of("PROGRAMADA", "CANCELADA", "FINALIZADA");
    private final EntityManagerFactory managerFactory;

    /**
     * Crea el DAO y su EntityManagerFactory.
     *
     * @param jdbcUrl URL de la base de datos SQLite
     */
    public SesionDao(String jdbcUrl) {
        managerFactory = OrmFactory.crear(jdbcUrl);
    }

    private void registrar(String metodo, Exception ex) {
        LOG.log(Level.WARNING, metodo + " no se pudo completar: " + ex.getMessage());
    }

    private boolean validar(Sesion dato) {
        return dato != null && dato.getIdPelicula() != null && dato.getIdSala() != null
                && dato.getFecha() != null && dato.getHora() != null
                && dato.getPrecio() != null && dato.getPrecio() >= 0 && ESTADOS.contains(dato.getEstado());
    }

    /**
     * Lista todas las sesiones de la bbdd.
     *
     * @return Todas las sesiones, o una lista vacia si no hay resultados o hay un error
     */
    public List<Sesion> listar() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select s from SesionEntity s order by s.fecha, s.hora, s.idSesion", SesionEntity.class)
                    .getResultStream()
                    .map(SesionMapper::desdeEntidad)
                    .toList();
        } catch (Exception e) {
            registrar("listar", e);
            return List.of();
        }
    }

    /**
     * Busca una sesion por su id.
     *
     * @param id Identificador de la sesion
     * @return La sesion encontrada, o Optional.empty() si no existe o hay un error
     */
    public Optional<Sesion> buscarPorId(int id) {
        try (var em = managerFactory.createEntityManager()) {
            return Optional.ofNullable(em.find(SesionEntity.class, id)).map(SesionMapper::desdeEntidad);
        } catch (Exception e) {
            registrar("buscarPorId", e);
            return Optional.empty();
        }
    }

    /**
     * Inserta una sesion nueva dentro de una transaccion.
     *
     * @param dato Datos a guardar (pelicula, sala, fecha, hora, precio y estado)
     * @return El id generado, o 0 si los datos no son validos, se incumple
     *         una restriccion de la bbdd o hay un error (se hace rollback)
     */
    public int insertar(Sesion dato) {
        if (!validar(dato)) return 0;

        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                SesionEntity entidad = SesionMapper.aEntidad(dato);
                em.persist(entidad);
                em.flush();
                dato.setIdSesion(entidad.getIdSesion());
                tx.commit();
                return entidad.getIdSesion();
            } catch (Exception e) {
                if (tx.isActive()) tx.rollback();
                throw e;
            }
        } catch (Exception e) {
            registrar("insertar", e);
            return 0;
        }
    }

    /**
     * Actualiza una sesion que ya existe dentro de una transaccion.
     *
     * @param dato Datos nuevos, con su id
     * @return 1 si se ha actualizado, o 0 si no existe, los datos no son validos
     *         o hay un error (se hace rollback)
     */
    public int actualizar(Sesion dato) {
        if (!validar(dato) || dato.getIdSesion() == null) return 0;

        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                SesionEntity s = em.find(SesionEntity.class, dato.getIdSesion());
                if (s == null) {
                    tx.rollback();
                    return 0;
                }
                s.setIdPelicula(dato.getIdPelicula());
                s.setIdSala(dato.getIdSala());
                s.setFecha(dato.getFecha());
                s.setHora(dato.getHora());
                s.setPrecio(dato.getPrecio());
                s.setEstado(dato.getEstado());
                tx.commit();
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

    /**
     * Borra una sesion por su id.
     *
     * @param id Identificador de la sesion
     * @return 1 si se ha borrado, o 0 si no existe, otras tablas dependen de
     *         este registro (ON DELETE RESTRICT) o hay un error
     */
    public int eliminar(int id) {
        if (id < 1) return 0;

        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                SesionEntity s = em.find(SesionEntity.class, id);
                if (s == null) {
                    tx.rollback();
                    return 0;
                }
                em.remove(s);
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

    /**
     * Lista las sesiones con su pelicula, sala y sede (C07).
     *
     * @return Filas con [0] id, [1] pelicula, [2] sede, [3] sala, [4] fecha y [5] hora, o una lista vacia si no hay resultados o hay un error
     */
    public List<Object[]> sesionesConDetalles() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select s.idSesion, p.titulo, se.nombre, sa.nombre, s.fecha, s.hora from SesionEntity s "
                    + "join PeliculaEntity p on p.idPelicula = s.idPelicula "
                    + "join SalaEntity sa on sa.idSala = s.idSala "
                    + "join SedeEntity se on se.idSede = sa.idSede "
                    + "order by s.idSesion", Object[].class)
                    .getResultList();
        } catch (Exception e) {
            registrar("sesionesConDetalles", e);
            return List.of();
        }
    }

    /**
     * Lista las sesiones de un dia (C04).
     *
     * @param fecha Fecha en formato YYYY-MM-DD
     * @return Filas con [0] fecha, [1] hora, [2] id y [3] precio, o una lista vacia si no hay resultados o hay un error
     */
    public List<Object[]> sesionesPorFecha(String fecha) {
        if (fecha == null || fecha.isBlank()) return List.of();

        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select s.fecha, s.hora, s.idSesion, s.precio from SesionEntity s "
                    + "where s.fecha = :fecha order by s.hora, s.idSesion", Object[].class)
                    .setParameter("fecha", fecha)
                    .getResultList();
        } catch (Exception e) {
            registrar("sesionesPorFecha", e);
            return List.of();
        }
    }

    /**
     * Lista las sesiones PROGRAMADA sin ninguna entrada valida (C17).
     *
     * @return Filas con [0] id, [1] pelicula y [2] fecha, o una lista vacia si no hay resultados o hay un error
     */
    public List<Object[]> sesionesSinVentas() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select s.idSesion, p.titulo, s.fecha from SesionEntity s "
                    + "join PeliculaEntity p on p.idPelicula = s.idPelicula "
                    + "where s.estado = 'PROGRAMADA' and not exists "
                    + "(select e from EntradaEntity e where e.idSesion = s.idSesion and e.estado in ('PAGADA', 'USADA')) "
                    + "order by s.idSesion", Object[].class)
                    .getResultList();
        } catch (Exception e) {
            registrar("sesionesSinVentas", e);
            return List.of();
        }
    }

    /**
     * Muestra por sesion el aforo, las entradas vendidas y las plazas libres (C15).
     *
     * @return Filas con [0] id, [1] aforo, [2] vendidas y [3] libres, o una lista vacia si no hay resultados o hay un error
     */
    public List<Object[]> disponibilidadSesiones() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select s.idSesion, sa.aforo, count(e.idEntrada), sa.aforo - count(e.idEntrada) from SesionEntity s "
                    + "join SalaEntity sa on sa.idSala = s.idSala "
                    + "left join EntradaEntity e on e.idSesion = s.idSesion and e.estado in ('PAGADA', 'USADA') "
                    + "group by s.idSesion, sa.aforo order by s.idSesion", Object[].class)
                    .getResultList();
        } catch (Exception e) {
            registrar("disponibilidadSesiones", e);
            return List.of();
        }
    }

    /**
     * Lista las sesiones sin plazas libres, sin contar las entradas canceladas (C18).
     *
     * @return Filas con [0] id, [1] pelicula y [2] aforo, o una lista vacia si no hay resultados o hay un error
     */
    public List<Object[]> sesionesAgotadas() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select s.idSesion, p.titulo, sa.aforo from SesionEntity s "
                    + "join PeliculaEntity p on p.idPelicula = s.idPelicula "
                    + "join SalaEntity sa on sa.idSala = s.idSala "
                    + "where (select count(e) from EntradaEntity e where e.idSesion = s.idSesion and e.estado in ('PAGADA', 'USADA')) >= sa.aforo "
                    + "order by s.idSesion", Object[].class)
                    .getResultList();
        } catch (Exception e) {
            registrar("sesionesAgotadas", e);
            return List.of();
        }
    }

    /**
     * Las 3 peliculas que mas han recaudado con entradas validas (C21).
     *
     * @return Filas con [0] pelicula, [1] entradas validas y [2] recaudacion, o una lista vacia si no hay resultados o hay un error
     */
    public List<Object[]> rankingRecaudacion() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select p.titulo, count(e.idEntrada), sum(s.precio) from PeliculaEntity p "
                    + "join SesionEntity s on s.idPelicula = p.idPelicula "
                    + "join EntradaEntity e on e.idSesion = s.idSesion "
                    + "where e.estado in ('PAGADA', 'USADA') "
                    + "group by p.idPelicula, p.titulo "
                    + "order by sum(s.precio) desc, p.titulo", Object[].class)
                    .setMaxResults(3)
                    .getResultList();
        } catch (Exception e) {
            registrar("rankingRecaudacion", e);
            return List.of();
        }
    }

    /**
     * Recaudacion de cada sede, tambien de las que no han vendido nada (C22).
     *
     * @return Filas con [0] sede y [1] recaudacion, o una lista vacia si no hay resultados o hay un error
     */
    public List<Object[]> ingresosPorSede() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select se.nombre, coalesce(sum(case when e.idEntrada is null then 0.0 else s.precio end), 0.0) from SedeEntity se "
                    + "left join SalaEntity sa on sa.idSede = se.idSede "
                    + "left join SesionEntity s on s.idSala = sa.idSala "
                    + "left join EntradaEntity e on e.idSesion = s.idSesion and e.estado in ('PAGADA', 'USADA') "
                    + "group by se.idSede, se.nombre "
                    + "order by coalesce(sum(case when e.idEntrada is null then 0.0 else s.precio end), 0.0) desc, se.nombre", Object[].class)
                    .getResultList();
        } catch (Exception e) {
            registrar("ingresosPorSede", e);
            return List.of();
        }
    }

    /**
     * Informe por fecha de sesion: sesiones programadas, entradas validas y recaudacion (C25).
     *
     * @return Filas con [0] fecha, [1] sesiones programadas, [2] entradas validas y [3] recaudacion, o una lista vacia si no hay resultados o hay un error
     */
    public List<Object[]> informeDiario() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select s.fecha, count(distinct case when s.estado = 'PROGRAMADA' then s.idSesion end), "
                    + "count(e.idEntrada), coalesce(sum(case when e.idEntrada is null then 0.0 else s.precio end), 0.0) from SesionEntity s "
                    + "left join EntradaEntity e on e.idSesion = s.idSesion and e.estado in ('PAGADA', 'USADA') "
                    + "group by s.fecha order by s.fecha", Object[].class)
                    .getResultList();
        } catch (Exception e) {
            registrar("informeDiario", e);
            return List.of();
        }
    }

    /**
     * Vende una entrada en una sola transaccion (C29). Solo se vende si la sesion esta
     * PROGRAMADA, el espectador existe y esta activo, no tiene ya entrada para esa sesion
     * y quedan plazas. Es synchronized para que dos ventas a la vez no superen el aforo.
     *
     * @param idSesion Identificador de la sesion
     * @param idEspectador Identificador del espectador
     * @param fechaCompra Fecha de compra en formato YYYY-MM-DD
     * @return 1 si se ha vendido, o 0 si no se cumple alguna condicion o hay un error (se hace rollback)
     */
    public synchronized int venderEntrada(int idSesion, int idEspectador, String fechaCompra) {
        if (fechaCompra == null || fechaCompra.isBlank()) return 0;

        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                if (!sePuedeVender(em, idSesion, idEspectador)) {
                    tx.rollback();
                    return 0;
                }
                EntradaEntity entrada = new EntradaEntity();
                entrada.setIdSesion(idSesion);
                entrada.setIdEspectador(idEspectador);
                entrada.setFechaCompra(fechaCompra);
                entrada.setEstado("PAGADA");
                em.persist(entrada);
                em.flush();
                tx.commit();
                return 1;
            } catch (Exception e) {
                if (tx.isActive()) tx.rollback();
                throw e;
            }
        } catch (Exception e) {
            registrar("venderEntrada", e);
            return 0;
        }
    }

    private boolean sePuedeVender(EntityManager em, int idSesion, int idEspectador) {
        SesionEntity sesion = em.find(SesionEntity.class, idSesion);
        if (sesion == null || !"PROGRAMADA".equals(sesion.getEstado())) return false;

        EspectadorEntity espectador = em.find(EspectadorEntity.class, idEspectador);
        if (espectador == null || !Integer.valueOf(1).equals(espectador.getActivo())) return false;

        long yaTiene = em.createQuery("select count(e) from EntradaEntity e where e.idSesion = :idSesion and e.idEspectador = :idEspectador", Long.class)
                .setParameter("idSesion", idSesion)
                .setParameter("idEspectador", idEspectador)
                .getSingleResult();
        if (yaTiene > 0) return false;

        SalaEntity sala = em.find(SalaEntity.class, sesion.getIdSala());
        long vendidas = em.createQuery("select count(e) from EntradaEntity e where e.idSesion = :idSesion and e.estado in ('PAGADA', 'USADA')", Long.class)
                .setParameter("idSesion", idSesion)
                .getSingleResult();
        return sala != null && vendidas < sala.getAforo();
    }

    /**
     * Cierra la EntityManagerFactory del DAO.
     */
    @Override
    public void close() {
        if (managerFactory.isOpen()) managerFactory.close();
    }
}
