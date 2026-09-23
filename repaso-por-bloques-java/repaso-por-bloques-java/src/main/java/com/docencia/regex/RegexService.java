package com.docencia.regex;

/**
 * Interfaz de los metodos de regex a implementar.
 * En todos los metodos, si el dato es invalido (null) no se lanza excepcion y se devuelve false.
 * @author AlejandroDonGar
 */
public interface RegexService {
    /**
     * Valida el formato de un dni espaniol: ocho numeros seguidos de una letra mayuscula
     * Si el texto es invalido (null) no se lanza excepcion y se devuelve false
     * @param dni dni a validar
     * @return devuelve true si el formato es correcto, false si no lo es
     */
    Boolean validarDni(String dni);

    /**
     * Valida el formato de un correo electronico: usuario, arroba, dominio y extension
     * Si el texto es invalido (null) no se lanza excepcion y se devuelve false
     * @param email correo a validar
     * @return devuelve true si el formato es correcto, false si no lo es
     */
    Boolean validarEmail(String email);

    /**
     * Valida el formato de un telefono espaniol de nueve digitos que empieza por 6, 7, 8 o 9, con prefijo opcional
     * Si el texto es invalido (null) no se lanza excepcion y se devuelve false
     * @param telefono telefono a validar
     * @return devuelve true si el formato es correcto, false si no lo es
     */
    Boolean validarTelefono(String telefono);

    /**
     * Valida el formato de un nombre formado solo por letras (con tildes y enie) y espacios, de al menos dos caracteres
     * Si el texto es invalido (null) no se lanza excepcion y se devuelve false
     * @param nombre nombre a validar
     * @return devuelve true si el formato es correcto, false si no lo es
     */
    Boolean validarNombre(String nombre);

    /**
     * Valida el formato de un codigo postal espaniol: cinco digitos cuyos dos primeros van de 01 a 52
     * Si el texto es invalido (null) no se lanza excepcion y se devuelve false
     * @param codigoPostal codigo postal a validar
     * @return devuelve true si el formato es correcto, false si no lo es
     */
    Boolean validarCodigoPostal(String codigoPostal);

    /**
     * Valida el formato de una matricula de coche espaniola: cuatro numeros seguidos de tres letras mayusculas
     * Si el texto es invalido (null) no se lanza excepcion y se devuelve false
     * @param matricula matricula a validar
     * @return devuelve true si el formato es correcto, false si no lo es
     */
    Boolean validarMatricula(String matricula);
}
