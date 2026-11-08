package es.ejemplo.biblioteca.dao;
import es.ejemplo.biblioteca.modelo.Autor;
import es.ejemplo.biblioteca.orm.mapper.AutorMapper;
import es.ejemplo.biblioteca.orm.config.OrmFactory;
import es.ejemplo.biblioteca.orm.entity.AutorEntity;
import jakarta.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.logging.Level;
/**
 * @author AlejandroDonGar
 * AutorDao
 */
public class AutorDao implements AutoCloseable {
    private static final Logger LOG=Logger.getLogger(AutorDao.class.getName());
    private final EntityManagerFactory managerFactory;
    private final boolean ownFactory;
    
    public AutorDao(String url) {
        managerFactory=OrmFactory.crear(url);
        ownFactory=true;
    }
    
    public AutorDao(EntityManagerFactory factory) {
        managerFactory=factory;ownFactory=false;
    }
    private void registrar(String metodo,Exception ex) {LOG.log(Level.WARNING,metodo+" no se pudo completar: "+ex.getMessage());}
    /**
     * Busca todos los autores de la bbdd
     * 
     * @return Devuelve una lista con todos los autores encontrados
     */
    public List<Autor> listar() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select a from AutorEntity a order by a.nombre", AutorEntity.class)
            .getResultStream()
            .map(AutorMapper::toDomain)
            .toList();
        } catch (Exception e) {
            registrar("listar", e);
            return List.of();
        }
    }
    /**
     * Busca un autor por su identificador
     * 
     * @param id Identificador con el que buscar al autor
     * @return Devuelve el autor encontrado o un Optional.empty()
     */
    public Optional<Autor> buscarPorId(Integer id) {
        if(id==null) return Optional.empty();

        try (var em = managerFactory.createEntityManager()){
            return Optional.ofNullable(em.find(AutorEntity.class, id)).map(AutorMapper::toDomain);
        } catch (Exception e) {
            registrar("buscarPorId", e);
            return Optional.empty();
        }
    }
    /**
     * Crea un nuevo autor en la bbdd
     * 
     * @param dato Datos del autor a insertar en la bbdd
     * @return Devuelve el id del autor
     */
    public int insertar(Autor dato) {
        if (!validar(dato)) return 0;

        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                validar(dato);
                AutorEntity entidad = AutorMapper.toEntity(dato);
                em.persist(entidad);
                em.flush();
                dato.setIdAutor(entidad.getIdAutor());;
                tx.commit();
                return entidad.getIdAutor();
            } catch (Exception e) {
                if(tx.isActive()) tx.rollback();
                throw e;
            }
        } catch (Exception e) {
            registrar("insertar", e);
            return 0;
        }
    }
    /**
     * Actualiza la informacion de un autor
     * 
     * @param dato  Informacion a actualizar del autor
     * @return Devuelve 1 o 0 dependiendo de si se ha actualizado el autor
     */
    public int actualizar(Autor dato) {
        if (!validar(dato) || dato.getIdAutor() == null) return 0;

        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                AutorEntity a = em.find(AutorEntity.class, dato.getIdAutor());
                if(a==null) {
                    tx.commit();
                    return 0;
                }
                //Trabajamos con un nuevo clienteEntity porque cliente no se debe de conecatar a la bbdd, solo el clienteEntity
                AutorEntity AutorEntityActualizado = AutorMapper.toEntity(dato);
                a.setNombre(AutorEntityActualizado.getNombre());
                a.setNacionalidad(AutorEntityActualizado.getNacionalidad());
                a.setAnioNacimiento(AutorEntityActualizado.getAnioNacimiento());
                tx.commit();
                return 1;
            } catch (Exception e) {
                if(tx.isActive()) tx.rollback();
                throw e;
            }
        } catch (Exception e) {
            registrar("actualizar", e);
            return 0;
        }
    }
    /**
     * Borra un autor de la bbdd
     * 
     * @param id Identificador del autor para encontrado en la bbdd
     * @return Devuelve 1 o 0 dependiendo de si se ha borrado el autor
     */
    public int eliminar(Integer id) {
        if(id==null||id<1) return 0;
        
        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                AutorEntity a = em.find(AutorEntity.class, id);
                if (a == null) {
                    tx.rollback();
                    return 0;
                }
                em.remove(a);
                tx.commit();
                return 1;
            } catch (Exception e) {
                if(tx.isActive()) tx.rollback();
                throw e;
            }
        } catch (Exception e) {
            registrar("eliminar", e);
            return 0;
        }
    }
    private boolean validar(Autor dato) {return dato != null && dato.getNombre()!=null && !dato.getNombre().isBlank();}
    /**
     * Lista los autores buscando por su nacionalidad
     * @param nacionalidad Nacionalidad del autor por el que buscarlo
     * @return Devuelve una lista de autores con la nacionalidad deseada
     */
    public List<Autor> buscarPorNacionalidad(String nacionalidad) {
        if(nacionalidad==null) return new ArrayList<>();

        try (var em = managerFactory.createEntityManager()){
            return em.createQuery("select a from AutorEntity a where a.nacionalidad = :nacionalidad", AutorEntity.class)
            .setParameter("nacionalidad", nacionalidad)
            .getResultStream()
            .map(AutorMapper::toDomain)
            .toList();
        } catch (Exception e) {
            registrar("buscarPorNacionalidad", e);
            return List.of();
        }
    }
    /**
     * Cuanta los libros que pertenecen a un autor en concreto
     * 
     * @param idAutor Identificador del autor cuyos libros queremos encontrar
     * @return Devuelve el total de libros que pertenecen a ese autor
     */
    public long contarLibrosPorAutor(int idAutor) {
        try (var em = managerFactory.createEntityManager()){
            long totalLibros = em.createQuery("select count(l) from LibroEntity l where idAutor = :idAutor", Long.class)
            .setParameter("idAutor", idAutor)
            .getFirstResult();
            return totalLibros;
        } catch (Exception e) {
            registrar("contarLibrosPorAutor", e);
            return 0;
        }
    }
    /**
     * Lista todos los autores junto al numero de libros que tiene cada uno, incluyendo los autores que no tienen ningun libro (aparecen con 0)
     * 
     * @return Devuelve una lista de filas donde [0] es el nombre del autor (String) y [1] el total de libros (Long), o una lista vacia si ocurre un error
     */
    public List<Object[]> autoresConRecuento() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select a.nombre, count(l) from AutorEntity a left join LibroEntity l on l.idAutor = a.idAutor group by a.idAutor, a.nombre order by a.nombre", Object[].class)
                .getResultList();
        } catch (Exception e) {
            registrar("autoresConRecuento", e);
            return List.of();
        }
    }
    /**
     * Lista los autores cuyos libros han sido prestados por al menos tres socios distintos
     * 
     * @return Devuelve una lista con los autores que cumplen la condicion, o una lista vacia si no hay ninguno o ocurre un error
     */
    public List<Autor> autoresLeidosPorTresSocios() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select a from PrestamoEntity p join p.libro l join l.autor a group by a having count(distinct p.idSocio) >= 3 order by a.nombre", AutorEntity.class)
                .getResultStream()
                .map(AutorMapper::toDomain)
                .toList();
        } catch (Exception e) {
            registrar("autoresLeidosPorTresSocios", e);
            return List.of();
        }
    }

    @Override public void close() {if(ownFactory && managerFactory.isOpen())managerFactory.close();}
}