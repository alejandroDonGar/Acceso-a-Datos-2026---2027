package com.docencia.herencia;

/**
 * Clase abstracta con los datos comunes de una persona (nombre, apellidos y edad).
 * No se puede instanciar: se usa a traves de sus hijas, como Alumno
 * @author AlejandroDonGar
 */
public abstract class Persona {
    private final String nombre;
    private final String apellidos;
    private final Integer edad;

    protected Persona(String nombre, String apellidos, Integer edad) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.edad = edad;
    }

    public String getNombre() { return nombre; }
    public String getApellidos() { return apellidos; }
    public Integer getEdad() { return edad; }
}
