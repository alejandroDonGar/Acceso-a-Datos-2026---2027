package es.ejemplo.taller.orm.entity;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author AlejandroDonGar
 * Clase VehiculoEntity
 */
@Entity
@Table(name = "vehiculos")
public class VehiculoEntity {
    @Id
    @Column(name = "matricula")
    private String matricula;

    @Column(name = "marca")
    private String marca;

    @Column(name = "modelo")
    private String modelo;

    @Column(name = "anio")
    private Integer anio;

    @Column(name = "id_cliente")
    private Integer idCliente;

    // Esto es importante para conectar una clave foranea a su clase de origen.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente", insertable = false, updatable = false)
    private ClienteEntity cliente; // Este nombre 'cliente' es el que esta en 'mappedBy = "cliente"' en ClienteEntity.

    @OneToMany(mappedBy = "vehiculo", fetch = FetchType.LAZY)
    private List<ReparacionEntity> reparaciones = new ArrayList<>();

    protected VehiculoEntity() {} // Necesario para JPA

    public VehiculoEntity(String matricula, String marca, String modelo, Integer anio, Integer idCliente) {
        this.matricula = matricula;
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
        this.idCliente = idCliente;
    }

    public String getMatricula() { return matricula; }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public ClienteEntity getCliente() {
        return cliente;
    }

    public void setCliente(ClienteEntity cliente) {
        this.cliente = cliente;
    }
}
