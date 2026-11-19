package es.ejemplo.cinefest.modelo;

/**
 * Modelo de dominio: sede del festival
 * @author AlejandroDonGar
 */
public class Sede {
    private Integer idSede;
    private String nombre;
    private String municipio;
    public Sede() {}
    public Integer getIdSede() { return idSede; }
    public void setIdSede(Integer value) { this.idSede = value; }
    public String getNombre() { return nombre; }
    public void setNombre(String value) { this.nombre = value; }
    public String getMunicipio() { return municipio; }
    public void setMunicipio(String value) { this.municipio = value; }
}
