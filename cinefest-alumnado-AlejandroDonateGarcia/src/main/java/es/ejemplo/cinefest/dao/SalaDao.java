package es.ejemplo.cinefest.dao;

import es.ejemplo.cinefest.modelo.Sala;
import es.ejemplo.cinefest.orm.config.OrmFactory;
import jakarta.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Optional;


public class SalaDao {

    public SalaDao(String jdbcUrl) {  }

    // Usar transacciones y rollback en operaciones de escritura.
    public List<Sala> listar() {
        // TODO: implementar conforme al contrato del README.md.
        return List.of();
    }
    public Optional<Sala> buscarPorId(int id) {
        // TODO: implementar conforme al contrato del README.md.
        return Optional.empty();
    }
    public int insertar(Sala dato) {
        // TODO: implementar conforme al contrato del README.md.
        return 0;
    }
    public int actualizar(Sala dato) {
        // TODO: implementar conforme al contrato del README.md.
        return 0;
    }
    public int eliminar(int id) {
        // TODO: implementar conforme al contrato del README.md.
        return 0;
    }
    public List<Object[]> salasConSede() {
        // TODO: implementar conforme al contrato del README.md.
        return List.of();
    }
    public List<Sala> salasSinSesiones() {
        // TODO: implementar conforme al contrato del README.md.
        return List.of();
    }
    public List<Object[]> ocupacionPorSala() {
        // TODO: implementar conforme al contrato del README.md.
        return List.of();
    }

}
