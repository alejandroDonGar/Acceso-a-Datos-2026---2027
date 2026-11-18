package es.ejemplo.taller.dao;
import es.ejemplo.taller.modelo.Reparacion;
import es.ejemplo.taller.orm.entity.ReparacionEntity;
import es.ejemplo.taller.orm.mapper.ReparacionMapper;
import java.math.BigDecimal;
import java.util.*;

/**
 * DAO de reparaciones con JPA, con las busquedas por vehiculo, por mecanico y las pendientes
 * @author AlejandroDonGar
 */
public class ReparacionDao extends AbstractDao<Reparacion, ReparacionEntity, Integer> {
    private static final Set<String> ESTADOS = Set.of("PENDIENTE", "EN_PROCESO", "FINALIZADA");

    public ReparacionDao(String url) {
        super(url, ReparacionEntity.class, ReparacionMapper::toDomain,
                "select r from ReparacionEntity r order by r.idReparacion");
    }

    public int insertar(Reparacion dato) {
        if (!esValido(dato)) return 0;
        return escribir("insertar", em -> {
            ReparacionEntity entidad = ReparacionMapper.toEntity(dato);
            em.persist(entidad);
            em.flush();
            dato.setIdReparacion(entidad.getIdReparacion());
            return entidad.getIdReparacion();
        });
    }

    public int actualizar(Reparacion dato) {
        if (!esValido(dato) || dato.getIdReparacion() == null) return 0;
        return escribir("actualizar", em -> {
            ReparacionEntity r = em.find(ReparacionEntity.class, dato.getIdReparacion());
            if (r == null) return 0;
            // Se escriben las FK planas (matricula, idMecanico): las relaciones @ManyToOne son de solo lectura
            r.setMatricula(dato.getMatricula());
            r.setIdMecanico(dato.getIdMecanico());
            r.setDescripcion(dato.getDescripcion());
            r.setFecha(dato.getFecha());
            r.setHorasTrabajo(dato.getHorasTrabajo());
            r.setEstado(dato.getEstado());
            return 1;
        });
    }

    public List<Reparacion> buscarPorVehiculo(String valor) {
        if (!texto(valor)) return new ArrayList<>();
        return consultar("select r from ReparacionEntity r where r.matricula = :matricula order by r.idReparacion",
                Map.of("matricula", valor));
    }

    public List<Reparacion> buscarPorMecanico(Integer valor) {
        if (valor == null) return new ArrayList<>();
        return consultar("select r from ReparacionEntity r where r.idMecanico = :idMecanico order by r.idReparacion",
                Map.of("idMecanico", valor));
    }

    public List<Reparacion> listarPendientes() {
        return consultar("select r from ReparacionEntity r where r.estado = :estado order by r.idReparacion",
                Map.of("estado", "PENDIENTE"));
    }

    // La fecha se valida en Java (formato AAAA-MM-DD y que exista); el esquema solo guarda texto
    private boolean esValido(Reparacion r) {
        return r != null && texto(r.getMatricula()) && r.getIdMecanico() != null
                && texto(r.getDescripcion()) && fecha(r.getFecha())
                && r.getHorasTrabajo() != null && r.getHorasTrabajo().compareTo(BigDecimal.ZERO) >= 0
                && ESTADOS.contains(r.getEstado());
    }
}
