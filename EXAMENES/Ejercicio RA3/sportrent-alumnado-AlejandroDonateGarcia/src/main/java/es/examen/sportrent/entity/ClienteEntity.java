package es.examen.sportrent.entity;
import jakarta.persistence.*;
import org.hibernate.annotations.OptimisticLock;

import java.util.List;
@Entity
@Table(name = "clientes")
public class ClienteEntity {
    @Id
    @Column(name = "dni")
    private String dni;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "email", unique = true)
    private String email;
    @Column(name = "telefono")
    private String telefono;
    @OneToMany(mappedBy = "cliente", fetch=FetchType.LAZY)
    private List<AlquilerEntity> alquileres;

    protected ClienteEntity() {
    }

    public ClienteEntity(String dni, String nombre, String email, String telefono) {
        this.dni = dni;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

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

    public List<AlquilerEntity> getAlquileres() {
        return alquileres;
    }

    public void setAlquileres(List<AlquilerEntity> alquileres) {
        this.alquileres = alquileres;
    }
}
