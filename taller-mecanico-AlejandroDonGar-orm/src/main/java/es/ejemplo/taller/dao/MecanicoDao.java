package es.ejemplo.taller.dao;
import es.ejemplo.taller.modelo.Mecanico;
import es.ejemplo.taller.orm.entity.MecanicoEntity;
import es.ejemplo.taller.orm.mapper.MecanicoMapper;
import java.math.BigDecimal;
import java.util.*;

/**
 * DAO de mecanicos con JPA, con la busqueda por especialidad
 * @author AlejandroDonGar
 */
public class MecanicoDao extends AbstractDao<Mecanico, MecanicoEntity, Integer> {

    public MecanicoDao(String url) {
        super(url, MecanicoEntity.class, MecanicoMapper::toDomain,
                "select m from MecanicoEntity m order by m.idMecanico");
    }

    public int insertar(Mecanico dato) {
        if (!esValido(dato)) return 0;
        return escribir("insertar", em -> {
            MecanicoEntity entidad = MecanicoMapper.toEntity(dato);
            em.persist(entidad);
            em.flush();
            dato.setIdMecanico(entidad.getIdMecanico());
            return entidad.getIdMecanico();
        });
    }

    public int actualizar(Mecanico dato) {
        if (!esValido(dato) || dato.getIdMecanico() == null) return 0;
        return escribir("actualizar", em -> {
            MecanicoEntity m = em.find(MecanicoEntity.class, dato.getIdMecanico());
            if (m == null) return 0;
            m.setNombre(dato.getNombre());
            m.setEspecialidad(dato.getEspecialidad());
            m.setTarifaHora(dato.getTarifaHora());
            return 1;
        });
    }

    // Coincidencia exacta, sin comodines ni pasar a minusculas
    public List<Mecanico> buscarPorEspecialidad(String especialidad) {
        if (!texto(especialidad)) return new ArrayList<>();
        return consultar("select m from MecanicoEntity m where m.especialidad = :especialidad order by m.idMecanico",
                Map.of("especialidad", especialidad));
    }

    private boolean esValido(Mecanico m) {
        return m != null && texto(m.getNombre()) && texto(m.getEspecialidad())
                && m.getTarifaHora() != null && m.getTarifaHora().compareTo(BigDecimal.ZERO) >= 0;
    }
}
