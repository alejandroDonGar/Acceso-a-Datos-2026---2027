package es.ejemplo.taller.dao;
import es.ejemplo.taller.modelo.PiezaReparacion;
import es.ejemplo.taller.orm.entity.PiezaReparacionEntity;
import es.ejemplo.taller.orm.mapper.PiezaReparacionMapper;
import java.math.BigDecimal;
import java.util.*;

/**
 * DAO de piezas de reparacion con JPA, con las piezas de una reparacion y su coste
 * @author AlejandroDonGar
 */
public class PiezaReparacionDao extends AbstractDao<PiezaReparacion, PiezaReparacionEntity, Integer> {

    public PiezaReparacionDao(String url) {
        super(url, PiezaReparacionEntity.class, PiezaReparacionMapper::toDomain,
                "select p from PiezaReparacionEntity p order by p.idPieza");
    }

    public int insertar(PiezaReparacion dato) {
        if (!esValido(dato)) return 0;
        return escribir("insertar", em -> {
            PiezaReparacionEntity entidad = PiezaReparacionMapper.toEntity(dato);
            em.persist(entidad);
            em.flush();
            dato.setIdPieza(entidad.getIdPieza());
            return entidad.getIdPieza();
        });
    }

    public int actualizar(PiezaReparacion dato) {
        if (!esValido(dato) || dato.getIdPieza() == null) return 0;
        return escribir("actualizar", em -> {
            PiezaReparacionEntity p = em.find(PiezaReparacionEntity.class, dato.getIdPieza());
            if (p == null) return 0;
            p.setIdReparacion(dato.getIdReparacion());
            p.setNombre(dato.getNombre());
            p.setCantidad(dato.getCantidad());
            p.setPrecioUnitario(dato.getPrecioUnitario());
            return 1;
        });
    }

    public List<PiezaReparacion> listarPorReparacion(Integer id) {
        if (id == null) return new ArrayList<>();
        return consultar("select p from PiezaReparacionEntity p where p.idReparacion = :idReparacion order by p.idPieza",
                Map.of("idReparacion", id));
    }

    // Como en la guia: se suma en Java con BigDecimal para no pasar por double
    // ponytail: si falla la consulta tambien devuelve 0, igual que una reparacion sin piezas
    public BigDecimal calcularCostePiezas(Integer id) {
        BigDecimal total = BigDecimal.ZERO;
        for (PiezaReparacion p : listarPorReparacion(id)) {
            total = total.add(p.getPrecioUnitario().multiply(BigDecimal.valueOf(p.getCantidad())));
        }
        return total;
    }

    private boolean esValido(PiezaReparacion p) {
        return p != null && p.getIdReparacion() != null && texto(p.getNombre())
                && p.getCantidad() != null && p.getCantidad() > 0
                && p.getPrecioUnitario() != null && p.getPrecioUnitario().compareTo(BigDecimal.ZERO) >= 0;
    }
}
