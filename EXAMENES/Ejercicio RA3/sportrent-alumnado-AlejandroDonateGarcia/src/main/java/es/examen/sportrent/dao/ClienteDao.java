package es.examen.sportrent.dao;
import es.examen.sportrent.entity.ClienteEntity;
import es.examen.sportrent.mapper.ClienteMapper;
import es.examen.sportrent.model.Cliente;
import es.examen.sportrent.model.ClienteAlquileres;
import es.examen.sportrent.test.config.JpaUtil;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.function.Function;

/**
 * DAO de clientes con JPA y JPQL. Ningun metodo publico lanza excepciones ni escribe por consola:
 * ante un error devuelve null, false o una lista vacia segun el contrato del enunciado
 * @author AlejandroDonGar
 */
public class ClienteDao {

    /**
     * Crea el DAO; cada metodo pide su EntityManager a JpaUtil, la base que configuran los tests
     */
    public ClienteDao() {
        // No guarda estado: la conexion se abre en cada operacion
    }

    /**
     * Lista todos los clientes ordenados por DNI
     * @return lista de clientes, vacia si no hay o si falla la consulta
     */
    public List<Cliente> listarTodos() {
        try (EntityManager em = JpaUtil.abrir()) {
            return em.createQuery("select c from ClienteEntity c order by c.dni", ClienteEntity.class)
                    .getResultList().stream().map(ClienteMapper::toDomain).toList();
        } catch (RuntimeException e) {
            return List.of();
        }
    }

    /**
     * Busca un cliente por su DNI
     * @param dni clave del cliente
     * @return el cliente, o null si el DNI es nulo, vacio o no existe
     */
    public Cliente buscarPorId(String dni) {
        if (dni == null || dni.isBlank()) return null;
        try (EntityManager em = JpaUtil.abrir()) {
            ClienteEntity entidad = em.find(ClienteEntity.class, dni);
            return entidad == null ? null : ClienteMapper.toDomain(entidad);
        } catch (RuntimeException e) {
            return null;
        }
    }

    /**
     * Inserta un cliente nuevo, el DNI lo pone quien llama
     * @param m cliente a guardar
     * @return true si se ha guardado; false si es nulo, tiene datos vacios o el DNI ya existe
     */
    public boolean insertar(Cliente m) {
        if (!esValido(m)) return false;
        return escribir(em -> {
            if (em.find(ClienteEntity.class, m.getDni()) != null) return false;
            em.persist(ClienteMapper.toEntity(m));
            return true;
        });
    }

    /**
     * Actualiza los datos de un cliente existente; el DNI no cambia
     * @param m cliente con los datos nuevos
     * @return true si existia y se ha guardado; false en otro caso
     */
    public boolean actualizar(Cliente m) {
        if (!esValido(m)) return false;
        return escribir(em -> {
            ClienteEntity ce = em.find(ClienteEntity.class, m.getDni());
            if (ce == null) return false;
            ce.setNombre(m.getNombre());
            ce.setEmail(m.getEmail());
            ce.setTelefono(m.getTelefono());
            return true;
        });
    }

    /**
     * Borra un cliente que no tenga alquileres
     * @param id DNI del cliente
     * @return true si se ha borrado; false si no existe, tiene alquileres o falla
     */
    public boolean eliminar(String id) {
        if (id == null || id.isBlank()) return false;
        return escribir(em -> {
            ClienteEntity ce = em.find(ClienteEntity.class, id);
            // Un cliente con alquileres no se puede borrar
            if (ce == null || contarAlquileres(em, id) > 0) return false;
            em.remove(ce);
            return true;
        });
    }

    /**
     * C1: clientes con al menos un alquiler y cuantos tiene, de mas a menos, empate por DNI
     * @return lista de clientes con su total, vacia si no hay o si falla
     */
    public List<ClienteAlquileres> alquileresPorCliente() {
        return consultarTotales("");
    }

    /**
     * C2: igual que C1 pero solo los clientes con mas de dos alquileres
     * @return lista de clientes con su total, vacia si no hay o si falla
     */
    public List<ClienteAlquileres> clientesConMasDeDosAlquileres() {
        return consultarTotales(" having count(a) > 2");
    }

    /**
     * C3: clientes con mas alquileres que el cliente de referencia, cuentan tambien los de cero
     * @param dniReferencia DNI del cliente con el que se compara
     * @return lista de clientes con su total, vacia si la referencia es nula, no existe o falla
     */
    public List<ClienteAlquileres> clientesSuperanReferencia(String dniReferencia) {
        if (dniReferencia == null || dniReferencia.isBlank()) return List.of();
        try (EntityManager em = JpaUtil.abrir()) {
            if (em.find(ClienteEntity.class, dniReferencia) == null) return List.of();
            // left join para que los clientes sin alquileres tambien entren en la comparacion
            return em.createQuery("select new es.examen.sportrent.model.ClienteAlquileres(c.dni, c.nombre, count(a)) "
                    + "from ClienteEntity c left join c.alquileres a group by c.dni, c.nombre "
                    + "having count(a) > (select count(a2) from AlquilerEntity a2 where a2.cliente.dni = :ref) "
                    + "order by count(a) desc, c.dni", ClienteAlquileres.class)
                    .setParameter("ref", dniReferencia)
                    .getResultList();
        } catch (RuntimeException e) {
            return List.of();
        }
    }

    // C1 y C2 solo se diferencian en el having
    private List<ClienteAlquileres> consultarTotales(String having) {
        try (EntityManager em = JpaUtil.abrir()) {
            return em.createQuery("select new es.examen.sportrent.model.ClienteAlquileres(c.dni, c.nombre, count(a)) "
                    + "from AlquilerEntity a join a.cliente c group by c.dni, c.nombre" + having
                    + " order by count(a) desc, c.dni", ClienteAlquileres.class)
                    .getResultList();
        } catch (RuntimeException e) {
            return List.of();
        }
    }

    private long contarAlquileres(EntityManager em, String dni) {
        return em.createQuery("select count(a) from AlquilerEntity a where a.cliente.dni = :dni", Long.class)
                .setParameter("dni", dni).getSingleResult();
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

    private boolean esValido(Cliente m) {
        return m != null && texto(m.getDni()) && texto(m.getNombre()) && texto(m.getEmail()) && texto(m.getTelefono());
    }

    private static boolean texto(String valor) {
        return valor != null && !valor.isBlank();
    }
}
