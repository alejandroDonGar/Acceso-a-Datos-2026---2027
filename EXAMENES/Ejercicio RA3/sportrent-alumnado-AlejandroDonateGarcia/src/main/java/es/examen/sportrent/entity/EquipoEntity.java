package es.examen.sportrent.entity;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;
@Entity
@Table(name = "equipos")
public class EquipoEntity {
    @Id
    @Column(name = "codigo")
    private String codigo;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "categoria")
    private String categoria;
    @Column(name = "precio_dia")
    private java.math.BigDecimal precioDia;
    @Column(name = "estado")
    private String estado;
    @OneToMany(mappedBy = "equipo", fetch=FetchType.LAZY)
    private List<AlquilerEntity> alquileres;

    protected EquipoEntity(){}

    public EquipoEntity(String codigo, String nombre, String categoria, BigDecimal precioDia, String estado) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.categoria = categoria;
        this.precioDia = precioDia;
        this.estado = estado;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public BigDecimal getPrecioDia() {
        return precioDia;
    }

    public void setPrecioDia(BigDecimal precioDia) {
        this.precioDia = precioDia;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<AlquilerEntity> getAlquileres() {
        return alquileres;
    }

    public void setEquipos(List<AlquilerEntity> alquileres) {
        this.alquileres = alquileres;
    }
}
