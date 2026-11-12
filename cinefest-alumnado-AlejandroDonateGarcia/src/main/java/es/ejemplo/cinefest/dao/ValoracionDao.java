package es.ejemplo.cinefest.dao;

import es.ejemplo.cinefest.modelo.Valoracion;
import es.ejemplo.cinefest.orm.config.OrmFactory;
import es.ejemplo.cinefest.orm.entity.ValoracionEntity;
import es.ejemplo.cinefest.orm.mapper.ValoracionMapper;
import jakarta.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author AlejandroDonGar
 * Acceso a la tabla valoraciones con JPA (EntityManager y JPQL).
 * Los errores se registran en el log y no se lanzan: los metodos
 * devuelven 0, Optional.empty() o una lista vacia.
 */
public class ValoracionDao implements AutoCloseable {

    private static final Logger LOG = Logger.getLogger(ValoracionDao.class.getName());
    private final EntityManagerFactory managerFactory;

    /**
     * Crea el DAO y su EntityManagerFactory.
     *
     * @param jdbcUrl URL de la base de datos SQLite
     */
    public ValoracionDao(String jdbcUrl) {
        managerFactory = OrmFactory.crear(jdbcUrl);
    }

    private void registrar(String metodo, Exception ex) {
        LOG.log(Level.WARNING, metodo + " no se pudo completar: " + ex.getMessage());
    }

    private boolean validar(Valoracion dato) {
        return dato != null && dato.getIdPelicula() != null && dato.getIdEspectador() != null
                && dato.getFecha() != null && dato.getPuntuacion() != null
                && dato.getPuntuacion() >= 1 && dato.getPuntuacion() <= 5;
    }

    /**
     * Lista todas las valoraciones de la bbdd.
     *
     * @return Todas las valoraciones, o una lista vacia si no hay resultados o hay un error
     */
    public List<Valoracion> listar() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select v from ValoracionEntity v order by v.idValoracion", ValoracionEntity.class)
                    .getResultStream()
                    .map(ValoracionMapper::desdeEntidad)
                    .toList();
        } catch (Exception e) {
            registrar("listar", e);
            return List.of();
        }
    }

    /**
     * Busca una valoracion por su id.
     *
     * @param id Identificador de la valoracion
     * @return La valoracion encontrada, o Optional.empty() si no existe o hay un error
     */
    public Optional<Valoracion> buscarPorId(int id) {
        try (var em = managerFactory.createEntityManager()) {
            return Optional.ofNullable(em.find(ValoracionEntity.class, id)).map(ValoracionMapper::desdeEntidad);
        } catch (Exception e) {
            registrar("buscarPorId", e);
            return Optional.empty();
        }
    }

    /**
     * Inserta una valoracion nueva dentro de una transaccion.
     *
     * @param dato Datos a guardar (pelicula, espectador, fecha y puntuacion de 1 a 5)
     * @return El id generado, o 0 si los datos no son validos, se incumple
     *         una restriccion de la bbdd o hay un error (se hace rollback)
     */
    public int insertar(Valoracion dato) {
        if (!validar(dato)) return 0;

        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                ValoracionEntity entidad = ValoracionMapper.aEntidad(dato);
                em.persist(entidad);
                em.flush();
                dato.setIdValoracion(entidad.getIdValoracion());
                tx.commit();
                return entidad.getIdValoracion();
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
     * Actualiza una valoracion que ya existe dentro de una transaccion.
     *
     * @param dato Datos nuevos, con su id
     * @return 1 si se ha actualizado, o 0 si no existe, los datos no son validos
     *         o hay un error (se hace rollback)
     */
    public int actualizar(Valoracion dato) {
        if (!validar(dato) || dato.getIdValoracion() == null) return 0;

        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                ValoracionEntity v = em.find(ValoracionEntity.class, dato.getIdValoracion());
                if (v == null) {
                    tx.rollback();
                    return 0;
                }
                v.setIdPelicula(dato.getIdPelicula());
                v.setIdEspectador(dato.getIdEspectador());
                v.setPuntuacion(dato.getPuntuacion());
                v.setComentario(dato.getComentario());
                v.setFecha(dato.getFecha());
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
     * Borra una valoracion por su id.
     *
     * @param id Identificador de la valoracion
     * @return 1 si se ha borrado, o 0 si no existe, otras tablas dependen de
     *         este registro (ON DELETE RESTRICT) o hay un error
     */
    public int eliminar(int id) {
        if (id < 1) return 0;

        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                ValoracionEntity v = em.find(ValoracionEntity.class, id);
                if (v == null) {
                    tx.rollback();
                    return 0;
                }
                em.remove(v);
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
     * Lista las valoraciones con la pelicula y el espectador (C10).
     *
     * @return Filas con [0] pelicula, [1] espectador y [2] puntuacion, o una lista vacia si no hay resultados o hay un error
     */
    public List<Object[]> valoracionesConDetalles() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select p.titulo, es.nombre, v.puntuacion from ValoracionEntity v "
                    + "join PeliculaEntity p on p.idPelicula = v.idPelicula "
                    + "join EspectadorEntity es on es.idEspectador = v.idEspectador "
                    + "order by p.titulo, es.nombre", Object[].class)
                    .getResultList();
        } catch (Exception e) {
            registrar("valoracionesConDetalles", e);
            return List.of();
        }
    }

    /**
     * Lista las peliculas que no tienen ninguna valoracion.
     *
     * @return Filas con [0] titulo y [1] genero, o una lista vacia si no hay resultados o hay un error
     */
    public List<Object[]> peliculasSinValoraciones() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select p.titulo, p.genero from PeliculaEntity p where not exists "
                    + "(select v from ValoracionEntity v where v.idPelicula = p.idPelicula) order by p.titulo", Object[].class)
                    .getResultList();
        } catch (Exception e) {
            registrar("peliculasSinValoraciones", e);
            return List.of();
        }
    }

    /**
     * Lista las valoraciones de una pelicula.
     *
     * @param idPelicula Identificador de la pelicula
     * @return Filas con [0] espectador, [1] puntuacion, [2] comentario y [3] fecha, o una lista vacia si no hay resultados o hay un error
     */
    public List<Object[]> valoracionesPorPelicula(int idPelicula) {
        if (idPelicula < 1) return List.of();

        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select es.nombre, v.puntuacion, v.comentario, v.fecha from ValoracionEntity v "
                    + "join EspectadorEntity es on es.idEspectador = v.idEspectador "
                    + "where v.idPelicula = :idPelicula order by v.fecha, es.nombre", Object[].class)
                    .setParameter("idPelicula", idPelicula)
                    .getResultList();
        } catch (Exception e) {
            registrar("valoracionesPorPelicula", e);
            return List.of();
        }
    }

    /**
     * Cuenta cuantas valoraciones hay de cada puntuacion.
     *
     * @return Filas con [0] puntuacion y [1] cantidad, o una lista vacia si no hay resultados o hay un error
     */
    public List<Object[]> estadisticasPuntuaciones() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select v.puntuacion, count(v.idValoracion) from ValoracionEntity v "
                    + "group by v.puntuacion order by v.puntuacion", Object[].class)
                    .getResultList();
        } catch (Exception e) {
            registrar("estadisticasPuntuaciones", e);
            return List.of();
        }
    }

    /**
     * Cierra la EntityManagerFactory del DAO.
     */
    @Override
    public void close() {
        if (managerFactory.isOpen()) managerFactory.close();
    }
}
