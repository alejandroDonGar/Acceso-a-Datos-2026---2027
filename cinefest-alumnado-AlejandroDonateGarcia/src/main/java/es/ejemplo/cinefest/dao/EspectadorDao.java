package es.ejemplo.cinefest.dao;

import es.ejemplo.cinefest.modelo.Espectador;
import es.ejemplo.cinefest.orm.config.OrmFactory;
import jakarta.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Optional;


public class EspectadorDao {

    public EspectadorDao(String jdbcUrl) { }

    // Usar transacciones y rollback en operaciones de escritura.

    public List<Espectador> listar() {
        // TODO: implementar conforme al contrato del README.md.
        return List.of();
    }
    public Optional<Espectador> buscarPorId(int id) {
        // TODO: implementar conforme al contrato del README.md.
        return Optional.empty();
    }
    public int insertar(Espectador dato) {
        // TODO: implementar conforme al contrato del README.md.
        return 0;
    }
    public int actualizar(Espectador dato) {
        // TODO: implementar conforme al contrato del README.md.
        return 0;
    }
    public int eliminar(int id) {
        // TODO: implementar conforme al contrato del README.md.
        return 0;
    }
    public Optional<Espectador> buscarPorEmail(String email) {
        // TODO: implementar conforme al contrato del README.md.
        return Optional.empty();
    }
    public List<Espectador> espectadoresSinEntradas() {
        // TODO: implementar conforme al contrato del README.md.
        return List.of();
    }
    public List<Object[]> rankingEspectadores() {
        // TODO: implementar conforme al contrato del README.md.
        return List.of();
    }
    public List<Object[]> espectadoresVariasSedes(int minimo) {
        // TODO: implementar conforme al contrato del README.md.
        return List.of();
    }
}
