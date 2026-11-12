package es.ejemplo.cinefest.dao;

import es.ejemplo.cinefest.modelo.Pelicula;
import es.ejemplo.cinefest.orm.config.OrmFactory;
import es.ejemplo.cinefest.orm.entity.PeliculaEntity;
import es.ejemplo.cinefest.orm.mapper.PeliculaMapper;
import jakarta.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author AlejandroDonGar
 * Acceso a la tabla peliculas con JPA (EntityManager y JPQL).
 * Los errores se registran en el log y no se lanzan: los metodos
 * devuelven 0, Optional.empty() o una lista vacia.
 */
public class PeliculaDao implements AutoCloseable {

    private static final Logger LOG = Logger.getLogger(PeliculaDao.class.getName());
    private final EntityManagerFactory managerFactory;

    /**
     * Crea el DAO y su EntityManagerFactory.
     *
     * @param jdbcUrl URL de la base de datos SQLite
     */
    public PeliculaDao(String jdbcUrl) {
        managerFactory = OrmFactory.crear(jdbcUrl);
    }

    private void registrar(String metodo, Exception ex) {
        LOG.log(Level.WARNING, metodo + " no se pudo completar: " + ex.getMessage());
    }

    private boolean validar(Pelicula dato) {
        return dato != null && dato.getTitulo() != null && !dato.getTitulo().isBlank()
                && dato.getGenero() != null && !dato.getGenero().isBlank()
                && dato.getPais() != null && !dato.getPais().isBlank()
                && dato.getDuracionMin() != null && dato.getDuracionMin() > 0 && dato.getAnio() != null;
    }

    /**
     * Lista todas las peliculas de la bbdd.
     *
     * @return Todas las peliculas, o una lista vacia si no hay resultados o hay un error
     */
    public List<Pelicula> listar() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select p from PeliculaEntity p order by p.titulo", PeliculaEntity.class)
                    .getResultStream()
                    .map(PeliculaMapper::desdeEntidad)
                    .toList();
        } catch (Exception e) {
            registrar("listar", e);
            return List.of();
        }
    }

    /**
     * Busca una pelicula por su id.
     *
     * @param id Identificador de la pelicula
     * @return La pelicula encontrada, o Optional.empty() si no existe o hay un error
     */
    public Optional<Pelicula> buscarPorId(int id) {
        try (var em = managerFactory.createEntityManager()) {
            return Optional.ofNullable(em.find(PeliculaEntity.class, id)).map(PeliculaMapper::desdeEntidad);
        } catch (Exception e) {
            registrar("buscarPorId", e);
            return Optional.empty();
        }
    }

    /**
     * Inserta una pelicula nueva dentro de una transaccion.
     *
     * @param dato Datos a guardar (titulo, genero, duracion, pais y anio)
     * @return El id generado, o 0 si los datos no son validos, se incumple
     *         una restriccion de la bbdd o hay un error (se hace rollback)
     */
    public int insertar(Pelicula dato) {
        if (!validar(dato)) return 0;

        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                PeliculaEntity entidad = PeliculaMapper.aEntidad(dato);
                em.persist(entidad);
                em.flush();
                dato.setIdPelicula(entidad.getIdPelicula());
                tx.commit();
                return entidad.getIdPelicula();
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
     * Actualiza una pelicula que ya existe dentro de una transaccion.
     *
     * @param dato Datos nuevos, con su id
     * @return 1 si se ha actualizado, o 0 si no existe, los datos no son validos
     *         o hay un error (se hace rollback)
     */
    public int actualizar(Pelicula dato) {
        if (!validar(dato) || dato.getIdPelicula() == null) return 0;

        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                PeliculaEntity p = em.find(PeliculaEntity.class, dato.getIdPelicula());
                if (p == null) {
                    tx.rollback();
                    return 0;
                }
                p.setTitulo(dato.getTitulo());
                p.setGenero(dato.getGenero());
                p.setDuracionMin(dato.getDuracionMin());
                p.setPais(dato.getPais());
                p.setAnio(dato.getAnio());
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
     * Borra una pelicula por su id.
     *
     * @param id Identificador de la pelicula
     * @return 1 si se ha borrado, o 0 si no existe, otras tablas dependen de
     *         este registro (ON DELETE RESTRICT) o hay un error
     */
    public int eliminar(int id) {
        if (id < 1) return 0;

        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                PeliculaEntity p = em.find(PeliculaEntity.class, id);
                if (p == null) {
                    tx.rollback();
                    return 0;
                }
                em.remove(p);
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
     * Lista las peliculas de un genero ordenadas por titulo (C03).
     *
     * @param genero Genero que se busca
     * @return Las peliculas de ese genero, o una lista vacia si no hay resultados o hay un error
     */
    public List<Pelicula> buscarPorGenero(String genero) {
        if (genero == null || genero.isBlank()) return List.of();

        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select p from PeliculaEntity p where p.genero = :genero order by p.titulo", PeliculaEntity.class)
                    .setParameter("genero", genero)
                    .getResultStream()
                    .map(PeliculaMapper::desdeEntidad)
                    .toList();
        } catch (Exception e) {
            registrar("buscarPorGenero", e);
            return List.of();
        }
    }

    /**
     * Lista las peliculas estrenadas desde un anio.
     *
     * @param anio Anio minimo (incluido)
     * @return Las peliculas desde ese anio, o una lista vacia si no hay resultados o hay un error
     */
    public List<Pelicula> peliculasDesdeAnio(int anio) {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select p from PeliculaEntity p where p.anio >= :anio order by p.anio, p.titulo", PeliculaEntity.class)
                    .setParameter("anio", anio)
                    .getResultStream()
                    .map(PeliculaMapper::desdeEntidad)
                    .toList();
        } catch (Exception e) {
            registrar("peliculasDesdeAnio", e);
            return List.of();
        }
    }

    /**
     * Lista las peliculas que no tienen ninguna sesion (C16).
     *
     * @return Las peliculas sin sesiones, o una lista vacia si no hay resultados o hay un error
     */
    public List<Pelicula> peliculasSinSesiones() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select p from PeliculaEntity p where not exists "
                    + "(select s from SesionEntity s where s.idPelicula = p.idPelicula) order by p.titulo", PeliculaEntity.class)
                    .getResultStream()
                    .map(PeliculaMapper::desdeEntidad)
                    .toList();
        } catch (Exception e) {
            registrar("peliculasSinSesiones", e);
            return List.of();
        }
    }

    /**
     * Cuenta las valoraciones y calcula la media de cada pelicula, tambien de las que no tienen (C26).
     *
     * @return Filas con [0] titulo, [1] numero de valoraciones y [2] media (null si no tiene),
     *         o una lista vacia si no hay resultados o hay un error
     */
    public List<Object[]> peliculasConMediaValoraciones() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select p.titulo, count(v.idValoracion), round(avg(v.puntuacion), 2) from PeliculaEntity p "
                    + "left join ValoracionEntity v on v.idPelicula = p.idPelicula "
                    + "group by p.idPelicula, p.titulo order by p.titulo", Object[].class)
                    .getResultList();
        } catch (Exception e) {
            registrar("peliculasConMediaValoraciones", e);
            return List.of();
        }
    }

    /**
     * Lista las peliculas con un minimo de valoraciones, de mayor a menor media (C14).
     *
     * @param minimo Numero minimo de valoraciones
     * @return Filas con [0] titulo, [1] numero de valoraciones y [2] media, o una lista vacia si no hay resultados o hay un error
     */
    public List<Object[]> mejorValoradas(int minimo) {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select p.titulo, count(v.idValoracion), round(avg(v.puntuacion), 2) from PeliculaEntity p "
                    + "join ValoracionEntity v on v.idPelicula = p.idPelicula "
                    + "group by p.idPelicula, p.titulo having count(v.idValoracion) >= :minimo "
                    + "order by avg(v.puntuacion) desc, p.titulo", Object[].class)
                    .setParameter("minimo", (long) minimo)
                    .getResultList();
        } catch (Exception e) {
            registrar("mejorValoradas", e);
            return List.of();
        }
    }

    /**
     * Lista las peliculas con mas entradas validas que la media de todas las peliculas,
     * contando tambien las que no han vendido ninguna (C19).
     *
     * @return Filas con [0] titulo y [1] entradas validas, o una lista vacia si no hay resultados o hay un error
     */
    public List<Object[]> masVendidasQueMedia() {
        try (var em = managerFactory.createEntityManager()) {
            long totalPeliculas = em.createQuery("select count(p) from PeliculaEntity p", Long.class).getSingleResult();
            if (totalPeliculas == 0) return List.of();
            long totalValidas = em.createQuery("select count(e) from EntradaEntity e where e.estado in ('PAGADA', 'USADA')", Long.class)
                    .getSingleResult();
            return em.createQuery("select p.titulo, count(e.idEntrada) from PeliculaEntity p "
                    + "join SesionEntity s on s.idPelicula = p.idPelicula "
                    + "join EntradaEntity e on e.idSesion = s.idSesion "
                    + "where e.estado in ('PAGADA', 'USADA') "
                    + "group by p.idPelicula, p.titulo having count(e.idEntrada) * :totalPeliculas > :totalValidas "
                    + "order by count(e.idEntrada) desc, p.titulo", Object[].class)
                    .setParameter("totalPeliculas", totalPeliculas)
                    .setParameter("totalValidas", totalValidas)
                    .getResultList();
        } catch (Exception e) {
            registrar("masVendidasQueMedia", e);
            return List.of();
        }
    }

    /**
     * Cuenta por genero los titulos, las sesiones y las entradas validas (C27).
     *
     * @return Filas con [0] genero, [1] titulos, [2] sesiones y [3] entradas validas, o una lista vacia si no hay resultados o hay un error
     */
    public List<Object[]> estadisticasGenero() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select p.genero, count(distinct p.idPelicula), count(distinct s.idSesion), count(e.idEntrada) from PeliculaEntity p "
                    + "left join SesionEntity s on s.idPelicula = p.idPelicula "
                    + "left join EntradaEntity e on e.idSesion = s.idSesion and e.estado in ('PAGADA', 'USADA') "
                    + "group by p.genero order by p.genero", Object[].class)
                    .getResultList();
        } catch (Exception e) {
            registrar("estadisticasGenero", e);
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
