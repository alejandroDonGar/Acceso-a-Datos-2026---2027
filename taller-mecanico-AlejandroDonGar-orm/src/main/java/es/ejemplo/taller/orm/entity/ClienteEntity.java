package es.ejemplo.taller.orm.entity;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
/**
 * @author AlejandroDonGar
 * Clase ClienteEntity
 */
@Entity
@Table(name = "clientes")
public class ClienteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private Integer idCliente;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "telefono")
    private String telefono;

    //En la clase de donde proviene el id, tenemos que hacer estas lineas por cada clase que dependa de la principla
    @OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY)
    private List<VehiculoEntity> vehiculos = new ArrayList<>();

    protected ClienteEntity() {} // Necesario para JPA

    public ClienteEntity(Integer idCliente, String nombre, String email, String telefono) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
    }

    public Integer getIdCliente() {
        return idCliente;
    }
    // No hace falta setter del id porque se genera automaticamente.

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public List<VehiculoEntity> getVehiculos() {
        return vehiculos;
    }
}