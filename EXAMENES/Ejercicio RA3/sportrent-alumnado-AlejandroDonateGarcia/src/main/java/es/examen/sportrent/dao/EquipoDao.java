package es.examen.sportrent.dao;
import es.examen.sportrent.entity.ClienteEntity;
import es.examen.sportrent.entity.EquipoEntity;
import es.examen.sportrent.infra.OrmFactory;
import es.examen.sportrent.mapper.ClienteMapper;
import es.examen.sportrent.mapper.EquipoMapper;
import es.examen.sportrent.model.Equipo;
import es.examen.sportrent.model.EquipoPrecio;
import es.examen.sportrent.model.PrecioCategoria;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class EquipoDao {
    private final EntityManagerFactory managerFactory;

    public EquipoDao() {
        String url = "jdbc:sqlite:datos/sportrent.db";
        managerFactory = OrmFactory.crear(url);
    }

    public List<Equipo> listarTodos() {
        try (var em = managerFactory.createEntityManager()){
            return em.createQuery("select e from EquipoEntity e order by e.codigo asc", EquipoEntity.class)
                    .getResultStream()
                    .map(EquipoMapper::toDomain)
                    .toList();
        } catch (Exception e) {
            System.err.println(e);
            return List.of();
        }
    }

    public Equipo buscarPorId(String codigo) {
        if(codigo == null || codigo.isBlank()) return null;

        try (var em = managerFactory.createEntityManager()) {
            return EquipoMapper.toDomain(em.find(EquipoEntity.class, codigo));
        } catch (Exception e) {
            System.err.println(e);
            return null;
        }
    }

    public boolean insertar(Equipo m) {
        if(m==null) return false;

        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                EquipoEntity ce = EquipoMapper.toEntity(m);
                em.persist(ce);
                em.flush();
                m.setCodigo(ce.getCodigo());
                tx.commit();
                return true;
            } catch (Exception e) {
                if(tx.isActive()) tx.rollback();
                throw e;
            }
        } catch (Exception e) {
            System.err.println(e);
            return false;
        }
    }

    public boolean actualizar(Equipo m) {
        if(m==null) return false;

        try (var em = managerFactory.createEntityManager()){
            var tx = em.getTransaction();
            try {
                tx.begin();
                EquipoEntity ee = em.find(EquipoEntity.class, m.getCodigo());
                if(ee == null) {
                    tx.commit();
                    return false;
                }
                EquipoEntity eeActualizado = EquipoMapper.toEntity(m);
                ee.setCodigo(eeActualizado.getCodigo());
                ee.setNombre(eeActualizado.getNombre());
                ee.setCategoria(eeActualizado.getCategoria());
                ee.setPrecioDia(eeActualizado.getPrecioDia());
                ee.setEstado(eeActualizado.getEstado());
                tx.commit();
                return true;
            } catch (Exception e) {
                if(tx.isActive()) tx.rollback();
                throw e;
            }
        } catch (Exception e) {
            System.err.println(e);
            return false;
        }
    }

    public boolean eliminar(String id) {
        if(id==null || id.isBlank()) return false;

        try (var em = managerFactory.createEntityManager()){
            var tx = em.getTransaction();
            try {
                tx.begin();
                EquipoEntity ee = em.find(EquipoEntity.class, id);
                if(ee == null) {
                    tx.rollback();
                    return false;
                }
                em.remove(ee);
                tx.commit();
                return true;
            } catch (Exception e) {
                if(tx.isActive()) tx.rollback();
                throw e;
            }
        } catch (Exception e) {
            System.err.println(e);
            return false;
        }
    }

    public List<Equipo> cincoMasCaros() {
        throw new UnsupportedOperationException("No implementado");
    }
    public List<PrecioCategoria> maximoPrecioPorCategoria() {
        throw new UnsupportedOperationException("No implementado");
    }
    public List<EquipoPrecio> equiposSobreMediaCategoria() {
        throw new UnsupportedOperationException("No implementado");
    }
}
