package es.ejemplo.taller.dao;
import es.ejemplo.taller.modelo.Vehiculo;
import es.ejemplo.taller.orm.config.OrmFactory;
import es.ejemplo.taller.orm.entity.VehiculoEntity;
import es.ejemplo.taller.orm.mapper.VehiculoMapper;
import jakarta.persistence.EntityManagerFactory;
import java.sql.*;
import java.util.*;

/**
 * @author AlejandroDonGar
 * Clase VehiculoDao
 */
public class VehiculoDao  {
    private final String url;
    private final EntityManagerFactory managerFactory;

    public VehiculoDao(String url) {
        this.url = url;
        managerFactory = OrmFactory.crear(url);
    }

    public List<Vehiculo> listar() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select v from VehiculoEntity v order by v.marca", VehiculoEntity.class)
                    .getResultStream()
                    .map(VehiculoMapper::toDomain)
                    .toList();
        }
    }
    public Optional<Vehiculo> buscarPorId(String id) {
        if (id == null || id.isEmpty()) return Optional.empty();

        try (var em = managerFactory.createEntityManager()) {
            return Optional.ofNullable(em.find(VehiculoEntity.class, id)).map(VehiculoMapper::toDomain);
        }
    }
    public int insertar(Vehiculo vehiculo) {
        if(vehiculo == null) return 0;

        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                VehiculoEntity entidad = VehiculoMapper.toEntity(vehiculo);
                em.persist(entidad);
                vehiculo.setMatricula(entidad.getMatricula());
                tx.commit();
                return 1;
            } catch (RuntimeException ex) {
                if (tx.isActive()) tx.rollback();
                throw ex;
            }
        }
    }
    public int actualizar(Vehiculo vehiculo) {
        if (vehiculo == null) return 0;

        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                VehiculoEntity c = em.find(VehiculoEntity.class, vehiculo.getMatricula());
                if(c==null) {
                    tx.commit();
                    return 0;
                }
                //Trabajamos con un nuevo clienteEntity porque cliente no se debe de conecatar a la bbdd, solo el clienteEntity
                VehiculoEntity vehiculoEntityActualizado = VehiculoMapper.toEntity(vehiculo);
                c.setMarca(vehiculoEntityActualizado.getMarca());
                c.setModelo(vehiculoEntityActualizado.getModelo());
                c.setAnio(vehiculoEntityActualizado.getAnio());

                // No se setea el idCliente porque se entiende que el cliente es el mismo.
                // Solo se pondria si el coche cambiara de cliente. -> c.setIdCliente(vehiculoEntityActualizado.getIdCliente());

                tx.commit();
                return 1;
            } catch (RuntimeException ex) {
                if(tx.isActive()) tx.rollback();
                throw ex;
            }
        }
    }
    public int eliminar(String id) {
        if(buscarPorId(id).isEmpty()) return 0;

        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                VehiculoEntity c = em.find(VehiculoEntity.class, id);
                tx.commit();
                return 1;
            } catch (RuntimeException ex) {
                if(tx.isActive()) tx.rollback();
                throw ex;
            }
        }
    }
    public List<Vehiculo> buscarPorCliente(Integer idCliente) {
        if (idCliente == null) return new ArrayList<>();

        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select v from VehiculoEntity v where v.idCliente = :idCliente",VehiculoEntity.class)
                    .setParameter("idCliente", idCliente)
                    .getResultStream()
                    .map(VehiculoMapper::toDomain)
                    .toList();
        }
    }
    public List<Vehiculo> listarPorMarca(String marca) {
        if (marca == null) return new ArrayList<>();

        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select v from VehiculoEntity v where v.marca = :marca",VehiculoEntity.class)
                    .setParameter("marca", marca)
                    .getResultStream()
                    .map(VehiculoMapper::toDomain)
                    .toList();
        }
    }
}
