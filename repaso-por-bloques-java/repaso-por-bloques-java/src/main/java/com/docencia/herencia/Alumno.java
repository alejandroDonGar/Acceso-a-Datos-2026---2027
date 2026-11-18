package com.docencia.herencia;

/**
 * Alumno: una Persona que ademas tiene numero de expediente
 * @author AlejandroDonGar
 */
public class Alumno extends Persona {
    private final String expediente;

    public Alumno(String nombre, String apellidos, Integer edad, String expediente) {
        super(nombre, apellidos, edad);
        this.expediente = expediente;
    }

    public String getExpediente() { return expediente; }
}
