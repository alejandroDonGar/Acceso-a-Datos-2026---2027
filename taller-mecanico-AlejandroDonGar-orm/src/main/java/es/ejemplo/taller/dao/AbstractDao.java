package es.ejemplo.taller.dao;

import es.ejemplo.taller.infra.ConexionBD;
import es.ejemplo.taller.orm.config.OrmFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DAO generico con JPA. Tiene lo que se repetia en los cinco DAO: listar, buscarPorId y eliminar,
 * lanzar consultas JPQL con parametros y envolver las escrituras en una transaccion con rollback.
 * Cada DAO hijo solo pone su JPQL, sus validaciones y como copia los datos en la entidad
 * @param <T> objeto de dominio que ve la aplicacion (Cliente, Vehiculo...)
 * @param <E> entidad JPA que se guarda en la base (ClienteEntity, VehiculoEntity...)
 * @param <ID> tipo de la clave: Integer en casi todas las tablas, String (matricula) en vehiculos
 * @author AlejandroDonGar
 */
public abstract class AbstractDao<T, E, ID> {
    private static final Logger LOG = Logger.getLogger(AbstractDao.class.getName());

    protected final EntityManagerFactory managerFactory;
    // No se puede escribir E.class con genericos: por eso la clase y el mapper llegan por el constructor
    private final Class<E> claseEntidad;
    private final Function<E, T> toDomain;
    private final String jpqlListar;

    protected AbstractDao(String url, Class<E> claseEntidad, Function<E, T> toDomain, String jpqlListar) {
        // ConexionBD es el unico punto que comprueba la base: si el archivo no vale, no seguimos
        try (Connection comprobacion = ConexionBD.abrir(url)) {
            // Solo queriamos saber que se puede abrir y que tiene las cinco tablas
        } catch (SQLException e) {
            throw new IllegalArgumentException("Base de datos no valida: " + e.getMessage(), e);
        }
        this.managerFactory = OrmFactory.crear(url);
        this.claseEntidad = claseEntidad;
        this.toDomain = toDomain;
        this.jpqlListar = jpqlListar;
    }

    public List<T> listar() {
        return consultar(jpqlListar, Map.of());
    }

    public Optional<T> buscarPorId(ID id) {
        if (id == null) return Optional.empty();
        try (EntityManager em = managerFactory.createEntityManager()) {
            return Optional.ofNullable(em.find(claseEntidad, id)).map(toDomain);
        } catch (PersistenceException e) {
            registrar("buscar por id", e);
            return Optional.empty();
        }
    }

    public int eliminar(ID id) {
        if (id == null) return 0;
        return escribir("eliminar", em -> {
            E entidad = em.find(claseEntidad, id);
            if (entidad == null) return 0;
            em.remove(entidad);
            return 1;
        });
    }

    /** Lanza una consulta JPQL con parametros con nombre (:nombre) y devuelve objetos de dominio. */
    protected List<T> consultar(String jpql, Map<String, Object> parametros) {
        try (EntityManager em = managerFactory.createEntityManager()) {
            TypedQuery<E> query = em.createQuery(jpql, claseEntidad);
            parametros.forEach(query::setParameter);
            return query.getResultStream().map(toDomain).toList();
        } catch (PersistenceException e) {
            registrar("consultar", e);
            return List.of();
        }
    }

    /**
     * Abre gestor y transaccion, ejecuta la accion y hace commit; si algo falla, rollback y 0.
     * Asi la gestion de la transaccion solo esta escrita aqui y no en cada metodo de cada DAO
     */
    protected int escribir(String operacion, Function<EntityManager, Integer> accion) {
        try (EntityManager em = managerFactory.createEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            try {
                tx.begin();
                int resultado = accion.apply(em);
                tx.commit();
                return resultado;
            } catch (PersistenceException e) {
                if (tx.isActive()) tx.rollback();
                registrar(operacion, e);
                return 0;
            }
        }
    }

    // Solo se registra la operacion y el motivo, sin los datos personales del registro
    protected void registrar(String operacion, PersistenceException e) {
        LOG.log(Level.WARNING, "Fallo al " + operacion + " (" + claseEntidad.getSimpleName() + "): " + e.getMessage());
    }

    /** NOT NULL no impide "" ni espacios: un texto obligatorio tiene que tener algo escrito. */
    protected static boolean texto(String valor) {
        return valor != null && !valor.isBlank();
    }

    /** Fecha con formato AAAA-MM-DD y que exista de verdad (2026-02-30 no vale). */
    protected static boolean fecha(String valor) {
        if (valor == null) return false;
        try {
            LocalDate.parse(valor);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
