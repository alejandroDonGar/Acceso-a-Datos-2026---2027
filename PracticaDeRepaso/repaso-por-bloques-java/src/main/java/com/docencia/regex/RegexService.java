package com.docencia.regex;
/**
 * Interfaz de los metodos de regex a implementar
 * @author AlejandroDonGar
 */
public interface RegexService {
    /**
     * Valida el formato de un dni español
     * @param dni dni a validad
     * @return devuelve true o false
     */
    Boolean validarDni(String dni);
    /**
     * Valida el formado de un correo
     * @param email correo a validar
     * @return devuelte true o false
     */
    Boolean validarEmail(String email);
    /**
     * Valida el formato de un telefono
     * @param telefono telefono a validar
     * @return devuelve true o false
     */
    Boolean validarTelefono(String telefono);
    /**
     * Valida el formato de un nombre con caracteres especiales
     * @param nombre nombre a validar
     * @return devuelve true o false
     */
    Boolean validarNombre(String nombre);
    /**
     * Valida el formado de un codigo postal español
     * @param codigoPostal codigo postal a validar
     * @return devuelve true o false
     */
    Boolean validarCodigoPostal(String codigoPostal);
    /**
     * Valida el formato de una matricula de coche español
     * @param matricula matricula a validar
     * @return devuelve true o false
     */
    Boolean validarMatricula(String matricula);
}