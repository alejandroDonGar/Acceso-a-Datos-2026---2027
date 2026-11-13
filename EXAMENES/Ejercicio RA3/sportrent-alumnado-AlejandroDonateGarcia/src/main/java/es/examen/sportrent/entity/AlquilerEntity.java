package es.examen.sportrent.entity;
import es.examen.sportrent.model.Alquiler;
import jakarta.persistence.*;

import java.math.BigDecimal;
@Entity
@Table(name = "alquileres")
public class AlquilerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "fecha_inicio")
    private String fechaInicio;
    @Column(name = "fecha_fin")
    private String fechaFin;
    @Column(name = "fecha_devolucion")
    private String fechaDevolucion;
    @Column(name = "importe_total")
    private java.math.BigDecimal importeTotal;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dni", insertable = false, updatable = false)
    private ClienteEntity cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo", insertable = false, updatable = false)
    private EquipoEntity equipo;

    protected AlquilerEntity() {
    }

    public AlquilerEntity(Long id, String fechaInicio, String fechaFin, String fechaDevolucion, BigDecimal importeTotal) {
        this.id = id;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.fechaDevolucion = fechaDevolucion;
        this.importeTotal = importeTotal;
    }

    public Long getId() {
        return id;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(String fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public BigDecimal getImporteTotal() {
        return importeTotal;
    }

    public void setImporteTotal(BigDecimal importeTotal) {
        this.importeTotal = importeTotal;
    }

    public ClienteEntity getCliente() {
        return cliente;
    }

    public void setCliente(ClienteEntity cliente) {
        this.cliente = cliente;
    }

    public EquipoEntity getEquipo() {
        return equipo;
    }

    public void setEquipo(EquipoEntity equipo) {
        this.equipo = equipo;
    }
}
