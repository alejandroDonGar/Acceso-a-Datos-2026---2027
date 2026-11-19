package es.examen.sportrent.dao;
import es.examen.sportrent.entity.AlquilerEntity;
import es.examen.sportrent.entity.ClienteEntity;
import es.examen.sportrent.entity.EquipoEntity;
import es.examen.sportrent.mapper.AlquilerMapper;
import es.examen.sportrent.model.Alquiler;
import es.examen.sportrent.model.EquipoAlquileres;
import es.examen.sportrent.model.EquipoIngresos;
import es.examen.sportrent.test.config.JpaUtil;
import jakarta.persistence.EntityManager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.function.Function;

/**
 * DAO de alquileres con JPA y JPQL. Calcula el importe historico, dias naturales por precio por dia
 * al insertar y actualizar, y no deja solapar alquileres no devueltos del mismo equipo.
 * Ningun metodo publico lanza excepciones ni escribe por consola
 * @author AlejandroDonGar
 */
public class AlquilerDao {

    /**
     * Crea el DAO; cada metodo pide su EntityManager a JpaUtil, la base que configuran los tests
     */
    public AlquilerDao() {
        // No guarda estado: la conexion se abre en cada operacion
    }

    /**
     * Lista todos los alquileres ordenados por id
     * @return lista de alquileres, vacia si no hay o si falla la consulta
     */
    public List<Alquiler> listarTodos() {
        return consultar("select a from AlquilerEntity a order by a.id");
    }

    /**
     * Busca un alquiler por su id
     * @param id identificador del alquiler
     * @return el alquiler, o null si el id es nulo o no existe
     */
    public Alquiler buscarPorId(Long id) {
        if (id == null) return null;
        try (EntityManager em = JpaUtil.abrir()) {
            AlquilerEntity entidad = em.find(AlquilerEntity.class, id);
            return entidad == null ? null : AlquilerMapper.toDomain(entidad);
        } catch (RuntimeException e) {
            return null;
        }
    }

    /**
     * Inserta un alquiler nuevo y calcula su importe historico
     * @param alquiler datos del alquiler, sin id: lo genera la base
     * @return true si se ha guardado; false si los datos o fechas no son validos, el cliente o el
     *         equipo no existen o el equipo ya esta alquilado en esas fechas
     */
    public boolean insertar(Alquiler alquiler) {
        if (!esValido(alquiler)) return false;
        return escribir(em -> {
            AlquilerEntity entidad = new AlquilerEntity(null, alquiler.getFechaInicio(), alquiler.getFechaFin(),
                    alquiler.getFechaDevolucion(), null);
            if (!rellenar(em, entidad, alquiler, -1L)) return false;
            em.persist(entidad);
            em.flush(); // fuerza el INSERT para tener el id generado
            alquiler.setId(entidad.getId());
            return true;
        });
    }

    /**
     * Actualiza fechas, cliente y equipo de un alquiler existente y recalcula el importe
     * @param alquiler datos nuevos, con el id del alquiler
     * @return true si existia y se ha guardado; false si no existe o se incumple alguna regla
     */
    public boolean actualizar(Alquiler alquiler) {
        if (!esValido(alquiler) || alquiler.getId() == null) return false;
        return escribir(em -> {
            AlquilerEntity entidad = em.find(AlquilerEntity.class, alquiler.getId());
            if (entidad == null) return false;
            entidad.setFechaInicio(alquiler.getFechaInicio());
            entidad.setFechaFin(alquiler.getFechaFin());
            entidad.setFechaDevolucion(alquiler.getFechaDevolucion());
            // Al comprobar solapes se excluye el propio alquiler
            return rellenar(em, entidad, alquiler, alquiler.getId());
        });
    }

    /**
     * Borra un alquiler de forma definitiva dentro de una transaccion
     * @param id identificador del alquiler
     * @return true si se ha borrado; false si no existe o falla
     */
    public boolean eliminar(Long id) {
        if (id == null) return false;
        return escribir(em -> {
            AlquilerEntity entidad = em.find(AlquilerEntity.class, id);
            if (entidad == null) return false;
            em.remove(entidad);
            return true;
        });
    }

    /**
     * A1: alquileres sin devolver, por fecha de fin, empate por id
     * @return alquileres pendientes, vacia si no hay o si falla
     */
    public List<Alquiler> pendientesDevolucion() {
        // Los join traen cliente y equipo en la misma consulta (el mapper necesita su dni y su codigo)
        return consultar("select a from AlquilerEntity a join a.cliente c join a.equipo e "
                + "where a.fechaDevolucion is null order by a.fechaFin, a.id");
    }

    /**
     * A2: equipos alquilados al menos dos veces, de mas a menos, empate por codigo
     * @return equipos con su numero de alquileres, vacia si no hay o si falla
     */
    public List<EquipoAlquileres> equiposAlquiladosDosVeces() {
        try (EntityManager em = JpaUtil.abrir()) {
            return em.createQuery("select new es.examen.sportrent.model.EquipoAlquileres(e.codigo, e.nombre, count(a)) "
                    + "from AlquilerEntity a join a.equipo e group by e.codigo, e.nombre having count(a) >= 2 "
                    + "order by count(a) desc, e.codigo", EquipoAlquileres.class).getResultList();
        } catch (RuntimeException e) {
            return List.of();
        }
    }

    /**
     * A3: los tres equipos que mas han ingresado sumando el importe historico, empate por codigo
     * @return hasta tres equipos con alquileres e ingresos, vacia si no hay o si falla
     */
    public List<EquipoIngresos> tresEquiposMasIngresos() {
        try (EntityManager em = JpaUtil.abrir()) {
            return em.createQuery("select new es.examen.sportrent.model.EquipoIngresos(e.codigo, e.nombre, count(a), sum(a.importeTotal)) "
                    + "from AlquilerEntity a join a.equipo e group by e.codigo, e.nombre "
                    + "order by sum(a.importeTotal) desc, e.codigo", EquipoIngresos.class)
                    .setMaxResults(3) // genera el LIMIT 3
                    .getResultList();
        } catch (RuntimeException e) {
            return List.of();
        }
    }

    /**
     * Comprueba cliente, equipo y solapes y deja la entidad lista con sus relaciones e importe.
     * @param idExcluido id del propio alquiler al actualizar, -1 al insertar
     */
    private boolean rellenar(EntityManager em, AlquilerEntity entidad, Alquiler datos, Long idExcluido) {
        ClienteEntity cliente = em.find(ClienteEntity.class, datos.getClienteDni());
        EquipoEntity equipo = em.find(EquipoEntity.class, datos.getEquipoCodigo());
        if (cliente == null || equipo == null || haySolape(em, datos, idExcluido)) return false;
        entidad.setCliente(cliente);
        entidad.setEquipo(equipo);
        // Dias naturales con inicio y fin incluidos x precio del dia: queda guardado aunque cambie la tarifa
        long dias = ChronoUnit.DAYS.between(LocalDate.parse(datos.getFechaInicio()), LocalDate.parse(datos.getFechaFin())) + 1;
        entidad.setImporteTotal(equipo.getPrecioDia().multiply(BigDecimal.valueOf(dias)));
        return true;
    }

    // Otro alquiler NO devuelto del mismo equipo se solapa si inicio1 <= fin2 y fin1 >= inicio2 (fechas ISO comparadas como texto)
    private boolean haySolape(EntityManager em, Alquiler datos, Long idExcluido) {
        return em.createQuery("select count(a) from AlquilerEntity a where a.equipo.codigo = :equipo "
                + "and a.fechaDevolucion is null and a.id <> :id and a.fechaInicio <= :fin and a.fechaFin >= :inicio", Long.class)
                .setParameter("equipo", datos.getEquipoCodigo())
                .setParameter("id", idExcluido)
                .setParameter("inicio", datos.getFechaInicio())
                .setParameter("fin", datos.getFechaFin())
                .getSingleResult() > 0;
    }

    // El mapper necesita el EntityManager abierto porque cliente y equipo son LAZY
    private List<Alquiler> consultar(String jpql) {
        try (EntityManager em = JpaUtil.abrir()) {
            return em.createQuery(jpql, AlquilerEntity.class).getResultList().stream()
                    .map(AlquilerMapper::toDomain).toList();
        } catch (RuntimeException e) {
            return List.of();
        }
    }

    // Abre transaccion, ejecuta la accion y confirma solo si devuelve true; si falla, rollback y false
    private boolean escribir(Function<EntityManager, Boolean> accion) {
        try (EntityManager em = JpaUtil.abrir()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                boolean ok = accion.apply(em);
                if (ok) tx.commit(); else tx.rollback();
                return ok;
            } catch (RuntimeException e) {
                if (tx.isActive()) tx.rollback();
                return false;
            }
        } catch (RuntimeException e) {
            return false;
        }
    }

    // Cliente y equipo obligatorios, fechas AAAA-MM-DD reales y fin no anterior al inicio
    private boolean esValido(Alquiler a) {
        if (a == null || !texto(a.getClienteDni()) || !texto(a.getEquipoCodigo())) return false;
        if (!fecha(a.getFechaInicio()) || !fecha(a.getFechaFin())) return false;
        if (a.getFechaDevolucion() != null && !fecha(a.getFechaDevolucion())) return false;
        return a.getFechaFin().compareTo(a.getFechaInicio()) >= 0;
    }

    private static boolean texto(String valor) {
        return valor != null && !valor.isBlank();
    }

    private static boolean fecha(String valor) {
        if (valor == null || !valor.matches("\\d{4}-\\d{2}-\\d{2}")) return false;
        try {
            LocalDate.parse(valor);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
