package es.ejemplo.cinefest.dao;

import es.ejemplo.cinefest.modelo.Entrada;
import es.ejemplo.cinefest.orm.config.OrmFactory;
import jakarta.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Optional;


public class EntradaDao  {

    private final EntityManagerFactory managerFactory;
    private final boolean ownFactory;


    public EntradaDao(String jdbcUrl) {
        managerFactory = OrmFactory.crear(jdbcUrl);
        ownFactory = true;
    }

    // Usar transacciones y rollback en operaciones de escritura.
    public List<Entrada> listar() {
        try (var em = managerFactory.createEntityManager()){
            return  listar();
        } catch (Exception e) {
        return List.of();
        }
    }
    public Optional<Entrada> buscarPorId(int id) {
        // TODO: implementar conforme al contrato del README.md.
        return Optional.empty();
    }
    public int insertar(Entrada dato) {
        // TODO: implementar conforme al contrato del README.md.
        return 0;
    }
    public int actualizar(Entrada dato) {
        // TODO: implementar conforme al contrato del README.md.
        return 0;
    }
    public int eliminar(int id) {
        // TODO: implementar conforme al contrato del README.md.
        return 0;
    }
    public List<Object[]> entradasConDetalles() {
        // TODO: implementar conforme al contrato del README.md.
        return List.of();
    }
    public List<Object[]> entradasPorEspectador(int idEspectador) {
        // TODO: implementar conforme al contrato del README.md.
        return List.of();
    }
    public List<Object[]> ventasPorDia() {
        // TODO: implementar conforme al contrato del README.md.
        return List.of();
    }
    public int cancelarEntrada(int idEntrada) {
        // TODO: implementar conforme al contrato del README.md.
        return 0;
    }

}
