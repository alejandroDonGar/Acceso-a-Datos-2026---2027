package es.examen.sportrent.dao;
import es.examen.sportrent.entity.EquipoEntity;
import es.examen.sportrent.mapper.EquipoMapper;
import es.examen.sportrent.model.Equipo;
import es.examen.sportrent.model.EquipoPrecio;
import es.examen.sportrent.model.PrecioCategoria;
import es.examen.sportrent.test.config.JpaUtil;
import jakarta.persistence.EntityManager;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;

/**
 * DAO de equipos con JPA y JPQL. Ningun metodo publico lanza excepciones ni escribe por consola:
 * ante un error devuelve null, false o una lista vacia segun el contrato del enunciado
 * @author AlejandroDonGar
 */
public class EquipoDao {

    /**
     * Crea el DAO; cada metodo pide su EntityManager a JpaUtil, la base que configuran los tests
     */
    public EquipoDao() {
        // No guarda estado: la conexion se abre en cada operacion
    }

    /**
     * Lista todos los equipos ordenados por codigo
     * @return lista de equipos, vacia si no hay o si falla la consulta
     */
    public List<Equipo> listarTodos() {
        try (EntityManager em = JpaUtil.abrir()) {
            return em.createQuery("select e from EquipoEntity e order by e.codigo", EquipoEntity.class)
                    .getResultList().stream().map(EquipoMapper::toDomain).toList();
        } catch (RuntimeException e) {
            return List.of();
        }
    }

    /**
     * Busca un equipo por su codigo
     * @param codigo clave del equipo
     * @return el equipo, o null si el codigo es nulo, vacio o no existe
     */
    public Equipo buscarPorId(String codigo) {
        if (codigo == null || codigo.isBlank()) return null;
        try (EntityManager em = JpaUtil.abrir()) {
            EquipoEntity entidad = em.find(EquipoEntity.class, codigo);
            return entidad == null ? null : EquipoMapper.toDomain(entidad);
        } catch (RuntimeException e) {
            return null;
        }
    }

    /**
     * Inserta un equipo nuevo, el codigo lo pone quien llama
     * @param m equipo a guardar
     * @return true si se ha guardado; false si es nulo, tiene datos invalidos o el codigo ya existe
     */
    public boolean insertar(Equipo m) {
        if (!esValido(m)) return false;
        return escribir(em -> {
            if (em.find(EquipoEntity.class, m.getCodigo()) != null) return false;
            em.persist(EquipoMapper.toEntity(m));
            return true;
        });
    }

    /**
     * Actualiza los datos de un equipo existente; el codigo no cambia
     * @param m equipo con los datos nuevos
     * @return true si existia y se ha guardado; false en otro caso
     */
    public boolean actualizar(Equipo m) {
        if (!esValido(m)) return false;
        return escribir(em -> {
            EquipoEntity ee = em.find(EquipoEntity.class, m.getCodigo());
            if (ee == null) return false;
            ee.setNombre(m.getNombre());
            ee.setCategoria(m.getCategoria());
            ee.setPrecioDia(m.getPrecioDia());
            ee.setEstado(m.getEstado());
            return true;
        });
    }

    /**
     * Borra un equipo que no tenga alquileres
     * @param id codigo del equipo
     * @return true si se ha borrado; false si no existe, tiene alquileres o falla
     */
    public boolean eliminar(String id) {
        if (id == null || id.isBlank()) return false;
        return escribir(em -> {
            EquipoEntity ee = em.find(EquipoEntity.class, id);
            // Un equipo con alquileres no se puede borrar
            if (ee == null || contarAlquileres(em, id) > 0) return false;
            em.remove(ee);
            return true;
        });
    }

    /**
     * E1: los cinco equipos mas caros, empate por codigo
     * @return hasta cinco equipos, vacia si no hay o si falla
     */
    public List<Equipo> cincoMasCaros() {
        try (EntityManager em = JpaUtil.abrir()) {
            return em.createQuery("select e from EquipoEntity e order by e.precioDia desc, e.codigo", EquipoEntity.class)
                    .setMaxResults(5) // genera el LIMIT 5
                    .getResultList().stream().map(EquipoMapper::toDomain).toList();
        } catch (RuntimeException e) {
            return List.of();
        }
    }

    /**
     * E2: precio maximo de cada categoria que tenga al menos dos equipos, por orden alfabetico
     * @return categorias con su precio maximo, vacia si no hay o si falla
     */
    public List<PrecioCategoria> maximoPrecioPorCategoria() {
        try (EntityManager em = JpaUtil.abrir()) {
            return em.createQuery("select new es.examen.sportrent.model.PrecioCategoria(e.categoria, max(e.precioDia)) "
                    + "from EquipoEntity e group by e.categoria having count(e) >= 2 order by e.categoria",
                    PrecioCategoria.class).getResultList();
        } catch (RuntimeException e) {
            return List.of();
        }
    }

    /**
     * E3: equipos cuyo precio supera la media de su categoria, con esa media; orden por categoria y codigo
     * @return equipos con la media de su categoria, vacia si no hay o si falla
     */
    public List<EquipoPrecio> equiposSobreMediaCategoria() {
        String media = "(select avg(e2.precioDia) from EquipoEntity e2 where e2.categoria = e.categoria)";
        try (EntityManager em = JpaUtil.abrir()) {
            return em.createQuery("select new es.examen.sportrent.model.EquipoPrecio(e.codigo, e.nombre, e.categoria, e.precioDia, "
                    + media + ") from EquipoEntity e where e.precioDia > " + media + " order by e.categoria, e.codigo",
                    EquipoPrecio.class).getResultList();
        } catch (RuntimeException e) {
            return List.of();
        }
    }

    private long contarAlquileres(EntityManager em, String codigo) {
        return em.createQuery("select count(a) from AlquilerEntity a where a.equipo.codigo = :codigo", Long.class)
                .setParameter("codigo", codigo).getSingleResult();
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

    // Textos obligatorios y precio por dia mayor que cero
    private boolean esValido(Equipo m) {
        return m != null && texto(m.getCodigo()) && texto(m.getNombre()) && texto(m.getCategoria())
                && texto(m.getEstado()) && m.getPrecioDia() != null && m.getPrecioDia().compareTo(BigDecimal.ZERO) > 0;
    }

    private static boolean texto(String valor) {
        return valor != null && !valor.isBlank();
    }
}
