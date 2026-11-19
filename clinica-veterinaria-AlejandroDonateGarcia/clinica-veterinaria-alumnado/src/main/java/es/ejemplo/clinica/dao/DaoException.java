package es.ejemplo.clinica.dao;
/**
 * Error de acceso a datos: envuelve la SQLException original (causa) para que la API de los DAO no declare throws
 * @author AlejandroDonGar
 */
public class DaoException extends RuntimeException {

    public DaoException(String mensaje, Throwable causa) { 
        super(mensaje, causa); 
    }

}
