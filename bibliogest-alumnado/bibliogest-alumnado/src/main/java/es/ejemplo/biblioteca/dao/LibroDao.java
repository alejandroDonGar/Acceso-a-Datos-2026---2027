package es.ejemplo.biblioteca.dao;
import es.ejemplo.biblioteca.modelo.Libro;

import es.ejemplo.biblioteca.orm.mapper.LibroMapper;
import es.ejemplo.biblioteca.orm.entity.LibroEntity;
import es.ejemplo.biblioteca.orm.config.OrmFactory;
import jakarta.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * @author AlejandroDonGar
 * DAO de acceso a la tabla libros mediante JPA (EntityManager y JPQL).
 * Los errores de persistencia se registran en el log y nunca se lanzan:
 * se devuelve 0, Optional.empty() o una lista vacia segun el metodo.
 */
public class LibroDao implements AutoCloseable {
    private static final Logger LOG=Logger.getLogger(LibroDao.class.getName());
    private final EntityManagerFactory managerFactory;
    private final boolean ownFactory;
    
    /**
     * Crea el DAO con su propia EntityManagerFactory, que se cierra en close()
     *
     * @param url URL JDBC de la base de datos SQLite
     */
    public LibroDao(String url) {managerFactory=OrmFactory.crear(url);ownFactory=true;}
    
    /**
     * Crea el DAO usando una EntityManagerFactory compartida (no la cierra en close())
     *
     * @param factory Factoria de EntityManager ya creada
     */
    public LibroDao(EntityManagerFactory factory) {managerFactory=factory;ownFactory=false;}
    private void registrar(String metodo,Exception ex) {LOG.log(Level.WARNING,metodo+" no se pudo completar: "+ex.getMessage());}
    /**
     * Busca todos los libros de la bbdd
     *
     * @return Devuelve una lista con todos los libros, o una lista vacia si no hay o hay un error
     */
    public List<Libro> listar() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select l from LibroEntity l order by l.titulo", LibroEntity.class)
            .getResultStream()
            .map(LibroMapper::toDomain)
            .toList();
        } catch (Exception e) {
            registrar("listar", e);
            return List.of();
        }
    }
    /**
     * Busca un libro por su identificador
     *
     * @param id Identificador con el que buscar al libro
     * @return Devuelve el libro encontrado o un Optional.empty()
     */
    public Optional<Libro> buscarPorId(Integer id) {
        if(id==null) return Optional.empty();

        try (var em = managerFactory.createEntityManager()){
            return Optional.ofNullable(em.find(LibroEntity.class, id)).map(LibroMapper::toDomain);
        } catch (Exception e) {
            registrar("buscarPorId", e);
            return Optional.empty();
        }
    }
    /**
     * Crea un nuevo libro en la bbdd
     *
     * @param dato Datos del libro a insertar en la bbdd
     * @return Devuelve el id del libro
     */
    public int insertar(Libro dato) {
        if (!validar(dato)) return 0;

        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                validar(dato);
                LibroEntity entidad = LibroMapper.toEntity(dato);
                em.persist(entidad);
                em.flush();
                dato.setIdLibro(entidad.getIdLibro());;
                tx.commit();
                return entidad.getIdLibro();
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
     * Actualiza la informacion de un libro
     *
     * @param dato Informacion a actualizar del libro
     * @return Devuelve 1 o 0 dependiendo de si se ha actualizado el libro
     */
    public int actualizar(Libro dato) {
        if (!validar(dato) || dato.getIdLibro() == null) return 0;

        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                LibroEntity a = em.find(LibroEntity.class, dato.getIdLibro());
                if(a==null) {
                    tx.commit();
                    return 0;
                }
                //Trabajamos con un nuevo clienteEntity porque cliente no se debe de conecatar a la bbdd, solo el clienteEntity
                LibroEntity LibroEntityActualizado = LibroMapper.toEntity(dato);
                a.setTitulo(LibroEntityActualizado.getTitulo());
                a.setIsbn(LibroEntityActualizado.getIsbn());
                a.setGenero(LibroEntityActualizado.getGenero());
                a.setAnioPublicacion(LibroEntityActualizado.getAnioPublicacion());
                a.setEjemplares(LibroEntityActualizado.getEjemplares());
                a.setIdAutor(LibroEntityActualizado.getIdAutor());
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
     * Borra un libro de la bbdd
     *
     * @param id Identificador del libro
     * @return Devuelve 1 si se ha borrado, o 0 si no existe, tiene prestamos o reservas, o hay un error
     */
    public int eliminar(Integer id) {
        if(id==null||id<1) return 0;

        try (var em = managerFactory.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                LibroEntity a = em.find(LibroEntity.class, id);
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
    private boolean validar(Libro dato) {return dato != null && dato.getTitulo()!=null && !dato.getTitulo().isBlank() && dato.getIsbn()!=null && !dato.getIsbn().isBlank() && dato.getGenero()!=null && dato.getIdAutor()!=null && dato.getEjemplares()!=null && dato.getEjemplares()>=0;}
    
    /**
     * Lista los libros de un genero
     *
     * @param genero Genero que se busca
     * @return Devuelve los libros de ese genero, o una lista vacia si no hay, el genero esta vacio o hay un error
     */
    public List<Libro> buscarPorGenero(String genero) {
        if(genero==null || genero.isBlank()) return List.of();

        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select l from LibroEntity l where genero = :genero", LibroEntity.class)
            .setParameter("genero", genero)
            .getResultStream()
            .map(LibroMapper::toDomain)
            .toList();
        } catch (Exception e) {
            registrar("buscarPorGenero", e);
            return List.of();
        }
    }
    
    /**
     * Lista los libros de un autor
     *
     * @param idAutor Identificador del autor
     * @return Devuelve los libros del autor, o una lista vacia si no tiene o hay un error
     */
    public List<Libro> buscarPorAutor(int idAutor) {
        if(idAutor<0) return List.of();

        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select l from LibroEntity l where l.idAutor = :idAutor", LibroEntity.class)
            .setParameter("idAutor", idAutor)
            .getResultStream()
            .map(LibroMapper::toDomain)
            .toList();
        } catch (Exception e) {
            registrar("buscarPorAutor", e);
            return List.of();
        }
    }
    
    /**
     * Lista los libros cuyo titulo contiene un texto
     *
     * @param texto Texto que debe aparecer en el titulo
     * @return Devuelve los libros encontrados, o una lista vacia si no hay, el texto esta vacio o hay un error
     */
    public List<Libro> buscarPorTitulo(String texto) {
        if(texto==null || texto.isBlank()) return List.of();

        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select l from LibroEntity l where titulo like concat('%', :textoLike, '%')", LibroEntity.class)
                    .setParameter("textoLike", texto)
                    .getResultStream()
                    .map(LibroMapper::toDomain)
                    .toList();
        } catch (Exception e) {
            registrar("buscarPorTitulo", e);
            return List.of();
        }
    }
    
    /**
     * Lista los libros que tienen al menos un ejemplar disponible
     * (disponibles = ejemplares - prestamos activos)
     *
     * @return Devuelve los libros disponibles ordenados por titulo, o una lista vacia si no hay o hay un error
     */
    public List<Libro> listarDisponibles() {
        try (var em = managerFactory.createEntityManager()) {
            // La subconsulta cuenta los prestamos sin devolver de cada libro
            return em.createQuery("select l from LibroEntity l where l.ejemplares > "
                    + "(select count(p) from PrestamoEntity p where p.idLibro = l.idLibro and p.fechaDevolucion is null) "
                    + "order by l.titulo", LibroEntity.class)
                    .getResultStream()
                    .map(LibroMapper::toDomain)
                    .toList();
        } catch (Exception e) {
            registrar("listarDisponibles", e);
            return List.of();
        }
    }
    
    /**
     * Lista los libros de un genero publicados desde un anio
     *
     * @param anio Anio minimo de publicacion (incluido)
     * @param genero Genero de los libros
     * @return Devuelve los libros que cumplen las dos condiciones, o una lista vacia si no hay o hay un error
     */
    public List<Libro> librosDesdeAnio(int anio, String genero) {
        if(genero==null || genero.isBlank()) return List.of();

        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select l from LibroEntity l where anioPublicacion >= :anio and genero = :genero", LibroEntity.class)
                    .setParameter("anio", anio)
                    .setParameter("genero", genero)
                    .getResultStream()
                    .map(LibroMapper::toDomain)
                    .toList();
        } catch (Exception e) {
            registrar("librosDesdeAnio", e);
            return List.of();
        }
    }
    
    /**
     * Lista los generos distintos que hay en el catalogo
     *
     * @return Devuelve los generos sin repetir, o una lista vacia si no hay libros o hay un error
     */
    public List<String> generos() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select distinct(l.genero) from LibroEntity l", String.class)
                    .getResultStream()
                    .toList();
        } catch (Exception e) {
            registrar("generos", e);
            return List.of();
        }
    }
    
    /**
     * Lista los libros que no se han prestado nunca
     *
     * @return Devuelve los libros sin ningun prestamo, o una lista vacia si no hay o hay un error
     */
    public List<Libro> nuncaPrestados() {
        try (var em = managerFactory.createEntityManager()) {
            // Libros cuyo id no aparece en ningun prestamo
            return em.createQuery("select l from LibroEntity l where l.idLibro not in (select p.idLibro from PrestamoEntity p) order by l.titulo", LibroEntity.class)
                    .getResultStream()
                    .map(LibroMapper::toDomain)
                    .toList();
        } catch (Exception e) {
            registrar("nuncaPrestados", e);
            return List.of();
        }
    }

    /**
     * Lista los libros con mas prestamos historicos que la media del catalogo.
     * La media incluye tambien los libros sin prestamos (cuentan como 0).
     *
     * @return Devuelve los libros por encima de la media, o una lista vacia si no hay libros o hay un error
     */
    public List<Libro> masPrestadosQueMedia() {
        try (var em = managerFactory.createEntityManager()) {
            // Media = total de prestamos / total de libros (asi los libros sin prestamos cuentan como 0)
            long totalLibros = em.createQuery("select count(l) from LibroEntity l", Long.class).getSingleResult();
            if(totalLibros==0) return List.of();
            long totalPrestamos = em.createQuery("select count(p) from PrestamoEntity p", Long.class).getSingleResult();
            double media = (double) totalPrestamos / totalLibros;

            return em.createQuery("select l from LibroEntity l where "
                    + "(select count(p) from PrestamoEntity p where p.idLibro = l.idLibro) > :media order by l.titulo", LibroEntity.class)
                    .setParameter("media", media)
                    .getResultStream()
                    .map(LibroMapper::toDomain)
                    .toList();
        } catch (Exception e) {
            registrar("masPrestadosQueMedia", e);
            return List.of();
        }
    }

    /**
     * Lista los libros que tienen reservas pendientes pero ningun ejemplar disponible
     *
     * @return Devuelve los libros sin disponibilidad y con reservas pendientes, o una lista vacia si no hay o hay un error
     */
    public List<Libro> reservadosSinDisponibilidad() {
        try (var em = managerFactory.createEntityManager()) {
            // Sin disponibilidad = los prestamos activos ya ocupan todos los ejemplares
            return em.createQuery("select distinct l from ReservaEntity r join r.libro l where r.estado = 'PENDIENTE' and l.ejemplares <= "
                    + "(select count(p) from PrestamoEntity p where p.idLibro = l.idLibro and p.fechaDevolucion is null) "
                    + "order by l.titulo", LibroEntity.class)
                    .getResultStream()
                    .map(LibroMapper::toDomain)
                    .toList();
        } catch (Exception e) {
            registrar("reservadosSinDisponibilidad", e);
            return List.of();
        }
    }

    /**
     * Lista los libros junto al nombre y la nacionalidad de su autor
     *
     * @return Devuelve filas con [0] titulo, [1] nombre del autor y [2] nacionalidad, o una lista vacia si hay un error
     */
    public List<Object[]> librosConAutor() {
        try (var em = managerFactory.createEntityManager()) {
            // join l.autor usa la relacion @ManyToOne de LibroEntity
            return em.createQuery("select l.titulo, a.nombre, a.nacionalidad from LibroEntity l join l.autor a order by l.titulo", Object[].class)
                    .getResultList();
        } catch (Exception e) {
            registrar("librosConAutor", e);
            return List.of();
        }
    }
    
    /**
     * Cuenta cuantos libros hay de cada genero
     *
     * @return Devuelve filas con [0] genero y [1] numero de libros (Long), o una lista vacia si hay un error
     */
    public List<Object[]> contarPorGenero() {
        try (var em = managerFactory.createEntityManager()) {
            return em.createQuery("select l.genero, count(l) from LibroEntity l group by l.genero order by l.genero", Object[].class)
                    .getResultList();
        } catch (Exception e) {
            registrar("contarPorGenero", e);
            return List.of();
        }
    }
    
    /**
     * Lista los generos que superan un numero minimo de prestamos historicos
     *
     * @param minimo Numero de prestamos que hay que superar
     * @return Devuelve filas con [0] genero y [1] numero de prestamos (Long), de mas a menos, o una lista vacia si hay un error
     */
    public List<Object[]> prestamosPorGeneroMinimo(long minimo) {
        try (var em = managerFactory.createEntityManager()) {
            // having filtra los grupos (where no puede usar count)
            return em.createQuery("select l.genero, count(p) from PrestamoEntity p join p.libro l group by l.genero having count(p) > :minimo order by count(p) desc", Object[].class)
                    .setParameter("minimo", minimo)
                    .getResultList();
        } catch (Exception e) {
            registrar("prestamosPorGeneroMinimo", e);
            return List.of();
        }
    }

    /**
     * Muestra para cada libro sus ejemplares totales, los prestados ahora mismo y los disponibles
     *
     * @return Devuelve filas con [0] titulo, [1] ejemplares, [2] prestados activos (Long) y [3] disponibles,
     *         incluidos los libros sin prestamos, o una lista vacia si hay un error
     */
    public List<Object[]> disponibilidadPorLibro() {
        try (var em = managerFactory.createEntityManager()) {
            // left join para que tambien salgan los libros sin prestamos activos (con 0)
            return em.createQuery("select l.titulo, l.ejemplares, count(p), l.ejemplares - count(p) from LibroEntity l "
                    + "left join PrestamoEntity p on p.idLibro = l.idLibro and p.fechaDevolucion is null "
                    + "group by l.idLibro, l.titulo, l.ejemplares order by l.titulo", Object[].class)
                    .getResultList();
        } catch (Exception e) {
            registrar("disponibilidadPorLibro", e);
            return List.of();
        }
    }

    /**
     * Estadisticas por genero: titulos, ejemplares, prestamos historicos y prestamos activos
     *
     * @return Devuelve filas con [0] genero, [1] numero de titulos, [2] total de ejemplares,
     *         [3] prestamos historicos y [4] prestamos activos, o una lista vacia si hay un error
     */
    public List<Object[]> estadisticasGenero() {
        try (var em = managerFactory.createEntityManager()) {
            // Los prestamos se cuentan con subconsultas: si se hiciera join con prestamos,
            // cada libro se repetiria una vez por prestamo y sum(ejemplares) saldria inflado
            return em.createQuery("select l.genero, count(l), sum(l.ejemplares), "
                    + "(select count(p) from PrestamoEntity p join p.libro l2 where l2.genero = l.genero), "
                    + "(select count(p) from PrestamoEntity p join p.libro l2 where l2.genero = l.genero and p.fechaDevolucion is null) "
                    + "from LibroEntity l group by l.genero order by l.genero", Object[].class)
                    .getResultList();
        } catch (Exception e) {
            registrar("estadisticasGenero", e);
            return List.of();
        }
    }

    /**
     * Cierra la EntityManagerFactory solo si la ha creado este DAO
     */
    @Override public void close() {if(ownFactory && managerFactory.isOpen())managerFactory.close();}
}
