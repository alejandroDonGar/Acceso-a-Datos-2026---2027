package es.codelearnacademy.filelab.persona;
import es.codelearnacademy.filelab.model.Persona;
import es.codelearnacademy.filelab.repository.IRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * @author AlejandroDonGar
 *
 * Clase repositorio que maneja objetos persona
 */
public interface IPersonaRepository extends IRepository<Persona,String> {

    /**
     * Crea una lista de personas por su edad minima
     * @param edad Parametro edad por el que buscar
     * @return Lista de personas con edad mayor o igual a la minima; lista vacia si no hay coincidencias
     */
    default List<Persona> findByEdadMinima(int edad) {
        List<Persona> list = findAll();
        List<Persona> resulado = new ArrayList<>();
        for (Persona persona : list) {
            if(persona.edad()>=edad)
                resulado.add(persona);
        }
        return resulado;
    }

    /**
     * Crea una lista de personas que esten activa
     * @param activo Parametro a buscar activo
     * @return Lista de personas activas o inactivas segun el parametro; lista vacia si no hay coincidencias
     */
    default List<Persona> findByActivo(boolean activo) {
        List<Persona> list = findAll();
        List<Persona> resulado = new ArrayList<>();
        for (Persona persona : list) {
            if(persona.activo()==activo)
                resulado.add(persona);
        }
        return resulado;
    }
}
