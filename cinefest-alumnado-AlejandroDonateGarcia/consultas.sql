-- CINEFEST. Consultas C01 a C30.

-- C01: Películas ordenadas alfabéticamente
SELECT titulo, genero, anio
FROM peliculas
ORDER BY titulo;

-- C02: Espectadores activos por antigüedad
SELECT nombre, fecha_alta
FROM espectadores
WHERE activo = 1
ORDER BY fecha_alta, nombre;

-- C03: Películas de un género
SELECT titulo, pais, anio
FROM peliculas
WHERE genero = 'Drama'
ORDER BY titulo;

-- C04: Sesiones entre dos fechas
SELECT fecha, hora, id_sesion, printf('%.2f', precio) AS precio
FROM sesiones
WHERE fecha BETWEEN '2026-11-13' AND '2026-11-14'
ORDER BY fecha, hora, id_sesion;

-- C05: Géneros distintos
SELECT DISTINCT genero
FROM peliculas
ORDER BY genero;

-- C06: Salas con su sede
SELECT se.nombre AS sede, sa.nombre AS sala, sa.aforo
FROM salas sa
JOIN sedes se ON se.id_sede = sa.id_sede
ORDER BY sede, sala;

-- C07: Sesiones con película, sala y sede
SELECT s.id_sesion, p.titulo AS pelicula, se.nombre AS sede, sa.nombre AS sala, s.fecha, s.hora
FROM sesiones s
JOIN peliculas p ON p.id_pelicula = s.id_pelicula
JOIN salas sa ON sa.id_sala = s.id_sala
JOIN sedes se ON se.id_sede = sa.id_sede
ORDER BY s.id_sesion;

-- C08: Entradas con espectador y película
SELECT e.id_entrada, es.nombre AS espectador, p.titulo AS pelicula, e.estado
FROM entradas e
JOIN espectadores es ON es.id_espectador = e.id_espectador
JOIN sesiones s ON s.id_sesion = e.id_sesion
JOIN peliculas p ON p.id_pelicula = s.id_pelicula
ORDER BY e.id_entrada;

-- C09: Espectadores que nunca compraron entradas
SELECT nombre, email
FROM espectadores es
WHERE NOT EXISTS (SELECT 1 FROM entradas e WHERE e.id_espectador = es.id_espectador)
ORDER BY nombre;

-- C10: Valoraciones con datos de película y espectador
SELECT p.titulo AS pelicula, es.nombre AS espectador, v.puntuacion
FROM valoraciones v
JOIN peliculas p ON p.id_pelicula = v.id_pelicula
JOIN espectadores es ON es.id_espectador = v.id_espectador
ORDER BY pelicula, espectador;

-- C11: Número de películas por género
SELECT genero, COUNT(*) AS total_peliculas
FROM peliculas
GROUP BY genero
ORDER BY genero;

-- C12: Número de salas y aforo total por sede, incluso sin salas
SELECT se.nombre AS sede, COUNT(sa.id_sala) AS total_salas, COALESCE(SUM(sa.aforo), 0) AS aforo_total
FROM sedes se
LEFT JOIN salas sa ON sa.id_sede = se.id_sede
GROUP BY se.id_sede, se.nombre
ORDER BY sede;

-- C13: Ranking de espectadores por entradas no canceladas
SELECT es.nombre AS espectador, COUNT(e.id_entrada) AS total_entradas
FROM espectadores es
LEFT JOIN entradas e ON e.id_espectador = es.id_espectador AND e.estado IN ('PAGADA', 'USADA')
GROUP BY es.id_espectador, es.nombre
ORDER BY total_entradas DESC, espectador;

-- C14: Películas con al menos dos valoraciones y su media
SELECT p.titulo AS pelicula, COUNT(*) AS numero_valoraciones, printf('%.2f', AVG(v.puntuacion)) AS media
FROM peliculas p
JOIN valoraciones v ON v.id_pelicula = p.id_pelicula
GROUP BY p.id_pelicula, p.titulo
HAVING COUNT(*) >= 2
ORDER BY AVG(v.puntuacion) DESC, pelicula;

-- C15: Plazas vendidas y libres por sesión
SELECT s.id_sesion, sa.aforo, COUNT(e.id_entrada) AS vendidas, sa.aforo - COUNT(e.id_entrada) AS libres
FROM sesiones s
JOIN salas sa ON sa.id_sala = s.id_sala
LEFT JOIN entradas e ON e.id_sesion = s.id_sesion AND e.estado IN ('PAGADA', 'USADA')
GROUP BY s.id_sesion, sa.aforo
ORDER BY s.id_sesion;

-- C16: Películas sin sesiones
SELECT titulo
FROM peliculas p
WHERE NOT EXISTS (SELECT 1 FROM sesiones s WHERE s.id_pelicula = p.id_pelicula)
ORDER BY titulo;

-- C17: Sesiones programadas sin ventas válidas
SELECT s.id_sesion, p.titulo AS pelicula, s.fecha
FROM sesiones s
JOIN peliculas p ON p.id_pelicula = s.id_pelicula
WHERE s.estado = 'PROGRAMADA'
  AND NOT EXISTS (SELECT 1 FROM entradas e
                  WHERE e.id_sesion = s.id_sesion AND e.estado IN ('PAGADA', 'USADA'))
ORDER BY s.id_sesion;

-- C18: Sesiones agotadas, sin contar cancelaciones
SELECT s.id_sesion, p.titulo AS pelicula, sa.aforo
FROM sesiones s
JOIN peliculas p ON p.id_pelicula = s.id_pelicula
JOIN salas sa ON sa.id_sala = s.id_sala
WHERE (SELECT COUNT(*) FROM entradas e
       WHERE e.id_sesion = s.id_sesion AND e.estado IN ('PAGADA', 'USADA')) >= sa.aforo
ORDER BY s.id_sesion;

-- C19: Películas con más ventas válidas que la media de las nueve películas
SELECT p.titulo AS pelicula, COUNT(*) AS entradas_validas
FROM peliculas p
JOIN sesiones s ON s.id_pelicula = p.id_pelicula
JOIN entradas e ON e.id_sesion = s.id_sesion
WHERE e.estado IN ('PAGADA', 'USADA')
GROUP BY p.id_pelicula, p.titulo
HAVING COUNT(*) > (SELECT COUNT(*) FROM entradas WHERE estado IN ('PAGADA', 'USADA')) * 1.0
                  / (SELECT COUNT(*) FROM peliculas)
ORDER BY entradas_validas DESC, pelicula;

-- C20: Espectadores con entradas válidas en al menos dos sedes
SELECT es.nombre AS espectador, COUNT(DISTINCT sa.id_sede) AS sedes_distintas
FROM entradas e
JOIN espectadores es ON es.id_espectador = e.id_espectador
JOIN sesiones s ON s.id_sesion = e.id_sesion
JOIN salas sa ON sa.id_sala = s.id_sala
WHERE e.estado IN ('PAGADA', 'USADA')
GROUP BY es.id_espectador, es.nombre
HAVING COUNT(DISTINCT sa.id_sede) >= 2
ORDER BY sedes_distintas DESC, espectador;

-- C21: Top 3 de películas por recaudación de entradas válidas
SELECT p.titulo AS pelicula, COUNT(*) AS entradas_validas, printf('%.2f', SUM(s.precio)) AS recaudacion
FROM peliculas p
JOIN sesiones s ON s.id_pelicula = p.id_pelicula
JOIN entradas e ON e.id_sesion = s.id_sesion
WHERE e.estado IN ('PAGADA', 'USADA')
GROUP BY p.id_pelicula, p.titulo
ORDER BY SUM(s.precio) DESC, pelicula
LIMIT 3;

-- C22: Recaudación por sede, incluyendo sedes sin ventas
SELECT se.nombre AS sede,
       printf('%.2f', COALESCE(SUM(CASE WHEN e.id_entrada IS NULL THEN 0 ELSE s.precio END), 0)) AS recaudacion
FROM sedes se
LEFT JOIN salas sa ON sa.id_sede = se.id_sede
LEFT JOIN sesiones s ON s.id_sala = sa.id_sala
LEFT JOIN entradas e ON e.id_sesion = s.id_sesion AND e.estado IN ('PAGADA', 'USADA')
GROUP BY se.id_sede, se.nombre
ORDER BY COALESCE(SUM(CASE WHEN e.id_entrada IS NULL THEN 0 ELSE s.precio END), 0) DESC, sede;

-- C23: Ventas diarias, excluyendo entradas canceladas
SELECT e.fecha_compra, COUNT(*) AS entradas_validas, printf('%.2f', SUM(s.precio)) AS total
FROM entradas e
JOIN sesiones s ON s.id_sesion = e.id_sesion
WHERE e.estado IN ('PAGADA', 'USADA')
GROUP BY e.fecha_compra
ORDER BY e.fecha_compra;

-- C24: Historial de entradas de un espectador
SELECT p.titulo AS pelicula, s.fecha AS fecha_sesion, e.estado AS estado_entrada, printf('%.2f', s.precio) AS precio
FROM entradas e
JOIN sesiones s ON s.id_sesion = e.id_sesion
JOIN peliculas p ON p.id_pelicula = s.id_pelicula
WHERE e.id_espectador = 1
ORDER BY fecha_sesion, pelicula;

-- C25: Informe por fecha de sesión, incluidos días sin ventas
SELECT s.fecha,
       COUNT(DISTINCT CASE WHEN s.estado = 'PROGRAMADA' THEN s.id_sesion END) AS sesiones_programadas,
       COUNT(e.id_entrada) AS entradas_validas,
       printf('%.2f', COALESCE(SUM(CASE WHEN e.id_entrada IS NULL THEN 0 ELSE s.precio END), 0)) AS recaudacion
FROM sesiones s
LEFT JOIN entradas e ON e.id_sesion = s.id_sesion AND e.estado IN ('PAGADA', 'USADA')
GROUP BY s.fecha
ORDER BY s.fecha;

-- C26: Media y cantidad de valoraciones por película, incluyendo películas sin valorar
SELECT p.titulo AS pelicula, COUNT(v.id_valoracion) AS numero_valoraciones,
       CASE WHEN COUNT(v.id_valoracion) = 0 THEN NULL ELSE printf('%.2f', AVG(v.puntuacion)) END AS media
FROM peliculas p
LEFT JOIN valoraciones v ON v.id_pelicula = p.id_pelicula
GROUP BY p.id_pelicula, p.titulo
ORDER BY pelicula;

-- C27: Títulos, sesiones y ventas válidas por género
SELECT p.genero, COUNT(DISTINCT p.id_pelicula) AS titulos, COUNT(DISTINCT s.id_sesion) AS sesiones,
       COUNT(e.id_entrada) AS entradas_validas
FROM peliculas p
LEFT JOIN sesiones s ON s.id_pelicula = p.id_pelicula
LEFT JOIN entradas e ON e.id_sesion = s.id_sesion AND e.estado IN ('PAGADA', 'USADA')
GROUP BY p.genero
ORDER BY p.genero;

-- C28: Salas sin sesiones programadas
SELECT se.nombre AS sede, sa.nombre AS sala
FROM salas sa
JOIN sedes se ON se.id_sede = sa.id_sede
WHERE NOT EXISTS (SELECT 1 FROM sesiones s WHERE s.id_sala = sa.id_sala AND s.estado = 'PROGRAMADA')
ORDER BY sede, sala;

-- C29: Registro atómico de entrada con control de aforo (id_sesion = 1, id_espectador = 8)
-- Todo dentro de una sola transacción. BEGIN IMMEDIATE bloquea la escritura desde el principio
-- para que nadie pueda vender la misma plaza entre la comprobación y el INSERT.
BEGIN IMMEDIATE;
-- 1) La sesión existe y está PROGRAMADA (si no devuelve fila -> ROLLBACK)
SELECT id_sesion FROM sesiones WHERE id_sesion = 1 AND estado = 'PROGRAMADA';
-- 2) El espectador existe y está activo (si no devuelve fila -> ROLLBACK)
SELECT id_espectador FROM espectadores WHERE id_espectador = 8 AND activo = 1;
-- 3) No tiene ya una entrada para esa sesión, aunque esté cancelada (si cuenta > 0 -> ROLLBACK)
SELECT COUNT(*) FROM entradas WHERE id_sesion = 1 AND id_espectador = 8;
-- 4) Quedan plazas: aforo - entradas válidas > 0 (si es 0 -> ROLLBACK)
SELECT sa.aforo - (SELECT COUNT(*) FROM entradas
                   WHERE id_sesion = 1 AND estado IN ('PAGADA', 'USADA')) AS libres
FROM sesiones s JOIN salas sa ON sa.id_sala = s.id_sala
WHERE s.id_sesion = 1;
-- 5) Todo correcto: se inserta la entrada PAGADA y se confirma -> resultado 1
INSERT INTO entradas (id_sesion, id_espectador, fecha_compra, estado)
VALUES (1, 8, '2026-11-10', 'PAGADA');
COMMIT;
-- Si falla cualquier paso: ROLLBACK; -> resultado 0

-- C30: Cancelación de entrada y liberación de plaza (id_entrada = 1)
-- Una sola transacción. No se borra la entrada: se cambia su estado para que quede registro.
BEGIN IMMEDIATE;
-- 1) La entrada existe y no está ya cancelada (si no devuelve fila -> ROLLBACK)
SELECT id_entrada FROM entradas WHERE id_entrada = 1 AND estado <> 'CANCELADA';
-- 2) Se cancela y se confirma -> resultado 1
UPDATE entradas SET estado = 'CANCELADA' WHERE id_entrada = 1 AND estado <> 'CANCELADA';
COMMIT;
-- Si falla o no existe: ROLLBACK; -> resultado 0
-- Comprobación: plazas libres de la sesión 1 (pasa de 1 a 2)
SELECT sa.aforo - COUNT(e.id_entrada) AS libres
FROM sesiones s
JOIN salas sa ON sa.id_sala = s.id_sala
LEFT JOIN entradas e ON e.id_sesion = s.id_sesion AND e.estado IN ('PAGADA', 'USADA')
WHERE s.id_sesion = 1
GROUP BY sa.aforo;
