package es.ejemplo.clinica.dao;

import es.ejemplo.clinica.infra.ConexionDB;
/**
 * @author AlejandroDonGar
 * AbstractDao
 */
public abstract class AbstractDao extends ConexionDB {
    public AbstractDao(String url) {
        super(url);
    }
}
