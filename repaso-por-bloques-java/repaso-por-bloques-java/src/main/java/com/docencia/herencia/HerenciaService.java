package com.docencia.herencia;

/**
 * Servicio para practicar herencia.
 * @author AlejandroDonGar
 */
public interface HerenciaService {
    /**
     * Obtiene el nombre completo de una persona uniendo nombre y apellidos
     * @param persona persona de la que se quiere el nombre completo
     * @return devuelve el nombre seguido de los apellidos
     * @throws IllegalArgumentException si la persona es null
     */
    String obtenerNombreCompleto(Persona persona);

    /**
     * Comprueba si una persona es en realidad un alumno usando instanceof
     * Si la persona es null no se lanza excepcion y se devuelve false
     * @param persona persona a comprobar
     * @return devuelve true si es un Alumno, false en caso contrario
     */
    Boolean esAlumno(Persona persona);

    /**
     * Genera una descripcion con el tipo, el nombre completo y la edad, por ejemplo ALUMNO: Ana Lopez (20)
     * @param persona persona a describir
     * @return devuelve el texto de la descripcion
     * @throws IllegalArgumentException si la persona es null
     */
    String obtenerDescripcionPersona(Persona persona);

    /**
     * Comprueba si una persona alcanza una edad minima
     * @param persona persona a comprobar
     * @param edadMinima edad minima exigida
     * @return devuelve true si la edad de la persona es igual o mayor que la minima
     * @throws IllegalArgumentException si la persona o la edad minima son null
     */
    Boolean tieneEdadMinima(Persona persona, Integer edadMinima);

    /**
     * Obtiene el tipo de una persona segun su clase (ALUMNO o PERSONA)
     * @param persona persona de la que se quiere el tipo
     * @return devuelve ALUMNO si es un Alumno o PERSONA en otro caso
     * @throws IllegalArgumentException si la persona es null
     */
    String obtenerTipoPersona(Persona persona);
}
