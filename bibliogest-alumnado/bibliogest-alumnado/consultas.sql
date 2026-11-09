-- 01. Libros ordenados por titulo                       (LibroDao.listar)
SELECT *
FROM libros
ORDER BY titulo;

-- 02. Socios activos, fecha de alta descendente        (SocioDao.listarActivos)
SELECT *
FROM socios
WHERE activo = 1
ORDER BY fecha_alta DESC;

-- 03. Libros cuyo titulo contiene un texto             (LibroDao.buscarPorTitulo)
SELECT *
FROM libros
WHERE titulo LIKE '%' || :texto || '%';

-- 04. Libros desde un anio y de un genero              (LibroDao.librosDesdeAnio)
SELECT *
FROM libros
WHERE anio_publicacion >= :anio
  AND genero = :genero;

-- 05. Generos distintos del catalogo                   (LibroDao.generos)
SELECT DISTINCT genero
FROM libros;

-- 06. Libros con nombre y nacionalidad del autor       (LibroDao.librosConAutor)
SELECT l.titulo, a.nombre, a.nacionalidad
FROM libros l
JOIN autores a ON a.id_autor = l.id_autor
ORDER BY l.titulo;

-- 07. Prestamos con socio, libro y fechas              (PrestamoDao.prestamosConSocioYLibro)
SELECT s.nombre, l.titulo, p.fecha_prestamo, p.fecha_limite, p.fecha_devolucion
FROM prestamos p
JOIN socios s ON s.id_socio = p.id_socio
JOIN libros l ON l.id_libro = p.id_libro
ORDER BY p.fecha_prestamo;

-- 08. Prestamos activos de un socio                    (PrestamoDao.listarPorSocio)
SELECT *
FROM prestamos
WHERE id_socio = :idSocio
  AND fecha_devolucion IS NULL
ORDER BY fecha_prestamo;

-- 09. Socios que nunca han hecho prestamos             (SocioDao.sociosSinPrestamos)
SELECT *
FROM socios
WHERE id_socio NOT IN (SELECT id_socio FROM prestamos)
ORDER BY nombre;

-- 10. Reservas pendientes con socio, titulo y autor    (ReservaDao.reservasConDetalles)
SELECT s.nombre, l.titulo, a.nombre AS autor, r.fecha_reserva
FROM reservas r
JOIN socios  s ON s.id_socio = r.id_socio
JOIN libros  l ON l.id_libro = r.id_libro
JOIN autores a ON a.id_autor = l.id_autor
WHERE r.estado = 'PENDIENTE'
ORDER BY r.fecha_reserva;

-- 11. Cantidad de libros por genero                    (LibroDao.contarPorGenero)
SELECT genero, COUNT(*) AS total_libros
FROM libros
GROUP BY genero
ORDER BY genero;

-- 12. Libros por autor, incluidos autores sin libros   (AutorDao.autoresConRecuento)
--     LEFT JOIN para no perder a los autores sin libros; COUNT(l.id_libro) da 0 en ese caso
SELECT a.nombre, COUNT(l.id_libro) AS total_libros
FROM autores a
LEFT JOIN libros l ON l.id_autor = a.id_autor
GROUP BY a.id_autor, a.nombre
ORDER BY a.nombre;

-- 13. Cinco socios con mas prestamos                   (SocioDao.rankingSocios(5))
SELECT s.nombre, COUNT(*) AS total_prestamos
FROM prestamos p
JOIN socios s ON s.id_socio = p.id_socio
GROUP BY s.id_socio, s.nombre
ORDER BY total_prestamos DESC
LIMIT 5;

-- 14. Generos con mas de cinco prestamos historicos    (LibroDao.prestamosPorGeneroMinimo(5))
SELECT l.genero, COUNT(*) AS total_prestamos
FROM prestamos p
JOIN libros l ON l.id_libro = p.id_libro
GROUP BY l.genero
HAVING COUNT(*) > 5
ORDER BY total_prestamos DESC;

-- 15. Por libro: ejemplares, prestados ahora y disponibles   (LibroDao.disponibilidadPorLibro)
--     La condicion de "activo" va en el ON para que el LEFT JOIN conserve los libros sin prestamos
SELECT l.titulo,
       l.ejemplares,
       COUNT(p.id_prestamo)                AS prestados,
       l.ejemplares - COUNT(p.id_prestamo) AS disponibles
FROM libros l
LEFT JOIN prestamos p ON p.id_libro = l.id_libro AND p.fecha_devolucion IS NULL
GROUP BY l.id_libro, l.titulo, l.ejemplares
ORDER BY l.titulo;

-- 16. Libros que nunca se han prestado                 (LibroDao.nuncaPrestados)
SELECT *
FROM libros
WHERE id_libro NOT IN (SELECT id_libro FROM prestamos)
ORDER BY titulo;

-- 17. Socios con prestamos vencidos en una fecha       (SocioDao.listarConPrestamosVencidos)
SELECT DISTINCT s.*
FROM prestamos p
JOIN socios s ON s.id_socio = p.id_socio
WHERE p.fecha_devolucion IS NULL
  AND p.fecha_limite < :fecha
ORDER BY s.nombre;

-- 18. Libros con mas prestamos que la media            (LibroDao.masPrestadosQueMedia)
--     Media = total de prestamos / total de libros, asi los libros sin prestamos cuentan como 0
--     (en el DAO la media se calcula antes y se pasa como parametro :media)
SELECT *
FROM libros l
WHERE (SELECT COUNT(*) FROM prestamos p WHERE p.id_libro = l.id_libro)
      > (SELECT COUNT(*) FROM prestamos) * 1.0 / (SELECT COUNT(*) FROM libros)
ORDER BY l.titulo;

-- 19. Autores leidos por al menos tres socios distintos   (AutorDao.autoresLeidosPorTresSocios)
SELECT a.*
FROM prestamos p
JOIN libros  l ON l.id_libro = p.id_libro
JOIN autores a ON a.id_autor = l.id_autor
GROUP BY a.id_autor
HAVING COUNT(DISTINCT p.id_socio) >= 3
ORDER BY a.nombre;

-- 20. Libros con reservas pendientes y sin ejemplares disponibles   (LibroDao.reservadosSinDisponibilidad)
SELECT DISTINCT l.*
FROM reservas r
JOIN libros l ON l.id_libro = r.id_libro
WHERE r.estado = 'PENDIENTE'
  AND l.ejemplares <= (SELECT COUNT(*) FROM prestamos p
                       WHERE p.id_libro = l.id_libro AND p.fecha_devolucion IS NULL)
ORDER BY l.titulo;

-- 21. Top 3 libros mas prestados de un mes, con autor  (PrestamoDao.rankingMensual('2025-01', 3))
SELECT l.titulo, a.nombre AS autor, COUNT(*) AS total_prestamos
FROM prestamos p
JOIN libros  l ON l.id_libro = p.id_libro
JOIN autores a ON a.id_autor = l.id_autor
WHERE p.fecha_prestamo LIKE :anioMes || '%'
GROUP BY l.id_libro, l.titulo, a.nombre
ORDER BY total_prestamos DESC
LIMIT 3;

-- 22. Historial de un socio con devolucion y retraso   (SocioDao.historialSocio)
SELECT l.titulo, p.fecha_prestamo, p.fecha_limite, p.fecha_devolucion,
       CASE WHEN p.fecha_devolucion IS NULL THEN 'PENDIENTE' ELSE 'DEVUELTO' END AS estado,
       CASE WHEN p.fecha_devolucion > p.fecha_limite THEN 'SI' ELSE 'NO' END     AS retraso
FROM prestamos p
JOIN libros l ON l.id_libro = p.id_libro
WHERE p.id_socio = :idSocio
ORDER BY p.fecha_prestamo;

-- 23. Reserva pendiente mas antigua de cada libro      (ReservaDao.prioridadReservas)
SELECT l.titulo, s.nombre, r.fecha_reserva
FROM reservas r
JOIN libros l ON l.id_libro = r.id_libro
JOIN socios s ON s.id_socio = r.id_socio
WHERE r.estado = 'PENDIENTE'
  AND r.fecha_reserva = (SELECT MIN(r2.fecha_reserva) FROM reservas r2
                         WHERE r2.id_libro = r.id_libro AND r2.estado = 'PENDIENTE')
ORDER BY l.titulo;

-- 24. Estadisticas por genero                          (LibroDao.estadisticasGenero)
--     Los prestamos se cuentan con subconsultas: con un JOIN cada libro se repetiria
--     una vez por prestamo y SUM(ejemplares) saldria multiplicado
SELECT l.genero,
       COUNT(*)          AS titulos,
       SUM(l.ejemplares) AS ejemplares,
       (SELECT COUNT(*) FROM prestamos p JOIN libros l2 ON l2.id_libro = p.id_libro
        WHERE l2.genero = l.genero)                                AS prestamos_historicos,
       (SELECT COUNT(*) FROM prestamos p JOIN libros l2 ON l2.id_libro = p.id_libro
        WHERE l2.genero = l.genero AND p.fecha_devolucion IS NULL) AS prestamos_activos
FROM libros l
GROUP BY l.genero
ORDER BY l.genero;

-- 25. Registro atomico de un prestamo                  (PrestamoDao.registrarPrestamo)
--     Todo ocurre dentro de UNA transaccion: si falla cualquier paso se hace ROLLBACK
--     y no queda nada a medias. En Java el metodo es synchronized para que dos hilos
--     no comprueben a la vez que queda un ejemplar y lo presten los dos.
BEGIN TRANSACTION;                                           -- tx.begin()

--   Paso 1: el socio existe y esta activo (si no devuelve fila o activo = 0 -> ROLLBACK)
SELECT activo FROM socios WHERE id_socio = :idSocio;         -- em.find(SocioEntity)

--   Paso 2: el libro existe y cuantos ejemplares tiene (si no hay fila -> ROLLBACK)
SELECT ejemplares FROM libros WHERE id_libro = :idLibro;     -- em.find(LibroEntity)

--   Paso 3: prestamos activos del libro; si activos >= ejemplares -> ROLLBACK
SELECT COUNT(*) FROM prestamos
WHERE id_libro = :idLibro AND fecha_devolucion IS NULL;

--   Paso 4: todo correcto, se inserta el prestamo (los CHECK validan las fechas)
INSERT INTO prestamos (id_socio, id_libro, fecha_prestamo, fecha_limite, fecha_devolucion)
VALUES (:idSocio, :idLibro, :inicio, :limite, NULL);          -- em.persist() + flush()

COMMIT;                                                      -- tx.commit()
-- Si algun paso falla:  ROLLBACK;                             -- tx.rollback()
