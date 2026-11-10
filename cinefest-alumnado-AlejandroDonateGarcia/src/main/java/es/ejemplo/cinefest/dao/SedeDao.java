package es.ejemplo.cinefest.dao;

import es.ejemplo.cinefest.modelo.Sede;
import es.ejemplo.cinefest.orm.config.OrmFactory;
import es.ejemplo.cinefest.orm.entity.SedeEntity;
import es.ejemplo.cinefest.orm.mapper.SedeMapper;
import jakarta.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Optional;

public class SedeDao {

    private final EntityManagerFactory managerFactory;

    public SedeDao(String jdbcUrl) {
        managerFactory = OrmFactory.crear(jdbcUrl);
    }

    // Usar transacciones y rollback en operaciones de escritura.
    public List<Sede> listar() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select s from sedes s order by s.nombre", SedeEntity.class)
            .getResultStream()
            .map(SedeMapper::desdeEntidad)
            .toList();
        } catch (Exception e) {
            System.err.println(e);
            return List.of();
        }
    }
    public Optional<Sede> buscarPorId(int id) {
        // TODO: implementar conforme al contrato del README.md.
        return Optional.empty();
    }
    public int insertar(Sede dato) {
        // TODO: implementar conforme al contrato del README.md.
        return 0;
    }
    public int actualizar(Sede dato) {
        // TODO: implementar conforme al contrato del README.md.
        return 0;
    }
    public int eliminar(int id) {
        // TODO: implementar conforme al contrato del README.md.
        return 0;
    }
    public List<Object[]> salasPorSede(int idSede) {
        // TODO: implementar conforme al contrato del README.md.
        return List.of();
    }
    public List<Object[]> aforoTotalPorSede() {
        // TODO: implementar conforme al contrato del README.md.
        return List.of();
    }
    
}
