-- Consultas de la primera fase sobre datos/taller.sqlite3 (misma base que el taller JDBC)
-- Los experimentos que modifican datos (integridad referencial) se hicieron sobre una copia: ver MEMORIA.md

-- 1. Lista los clientes ordenados por nombre.
-- Todas las columnas de clientes, de la A a la Z por nombre.
SELECT id_cliente, nombre, email, telefono
FROM clientes
ORDER BY nombre;

-- 2. Consulta matrícula, marca, modelo y año de los vehículos de una marca elegida.
-- Marca elegida: Seat. Comparacion exacta (en Java se pasaria como parametro ?).
SELECT matricula, marca, modelo, anio
FROM vehiculos
WHERE marca = 'Seat';

-- 3. Obtén los vehículos de un cliente utilizando su identificador.
-- Vehiculos del cliente con id 1.
SELECT matricula, marca, modelo, anio
FROM vehiculos
WHERE id_cliente = 1;

-- 4. Lista los mecánicos de una especialidad y ordénalos por tarifa.
-- Mecanicos de Motor, del mas barato al mas caro.
SELECT id_mecanico, nombre, especialidad, tarifa_hora
FROM mecanicos
WHERE especialidad = 'Motor'
ORDER BY tarifa_hora;

-- 5. Obtén las reparaciones pendientes, ordenadas por fecha.
-- Como la fecha se guarda como texto AAAA-MM-DD, ordenarla como texto la ordena tambien por fecha.
SELECT id_reparacion, matricula, descripcion, fecha
FROM reparaciones
WHERE estado = 'PENDIENTE'
ORDER BY fecha;

-- 6. Muestra la matrícula de cada vehículo y el nombre de su propietario. Resuélvelo una vez con `WHERE` y otra con `INNER JOIN`.
-- a) Con WHERE: se cruzan las dos tablas y la condicion se queda solo con las parejas que coinciden.
SELECT v.matricula, c.nombre
FROM vehiculos v, clientes c
WHERE v.id_cliente = c.id_cliente;

-- b) Con INNER JOIN: el mismo resultado, pero la condicion de union va en ON y queda separada de los filtros.
SELECT v.matricula, c.nombre
FROM vehiculos v
INNER JOIN clientes c ON v.id_cliente = c.id_cliente;

-- 7. Muestra las reparaciones con el nombre del cliente, la matrícula y el nombre del mecánico.
-- Tres uniones: reparacion -> vehiculo -> cliente, y reparacion -> mecanico.
SELECT r.id_reparacion, c.nombre AS cliente, r.matricula, m.nombre AS mecanico, r.descripcion, r.estado
FROM reparaciones r
INNER JOIN vehiculos v ON r.matricula = v.matricula
INNER JOIN clientes c ON v.id_cliente = c.id_cliente
INNER JOIN mecanicos m ON r.id_mecanico = m.id_mecanico
ORDER BY r.id_reparacion;

-- 8. Cuenta los vehículos de cada cliente, incluyendo clientes sin vehículos.
-- LEFT JOIN conserva todos los clientes aunque no tengan vehiculo (sus columnas de vehiculo salen NULL).
-- COUNT(v.matricula) no cuenta los NULL, asi que un cliente sin vehiculos sale con 0.
SELECT c.id_cliente, c.nombre, COUNT(v.matricula) AS num_vehiculos
FROM clientes c
LEFT JOIN vehiculos v ON v.id_cliente = c.id_cliente
GROUP BY c.id_cliente, c.nombre
ORDER BY c.id_cliente;

-- 9. Calcula el coste de piezas por reparación, incluyendo las reparaciones sin piezas con coste cero.
-- GROUP BY junta las piezas de cada reparacion, SUM suma cantidad x precio
-- y COALESCE cambia por 0 el NULL que da SUM cuando la reparacion no tiene piezas.
SELECT r.id_reparacion, COALESCE(SUM(p.cantidad * p.precio_unitario), 0) AS coste_piezas
FROM reparaciones r
LEFT JOIN piezas_reparacion p ON p.id_reparacion = r.id_reparacion
GROUP BY r.id_reparacion
ORDER BY r.id_reparacion;

-- 10. Obtén las reparaciones que no tienen piezas asociadas.
-- LEFT JOIN + quedarse con las filas donde la pieza es NULL: reparaciones sin ninguna pieza.
SELECT r.id_reparacion, r.matricula, r.descripcion
FROM reparaciones r
LEFT JOIN piezas_reparacion p ON p.id_reparacion = r.id_reparacion
WHERE p.id_pieza IS NULL
ORDER BY r.id_reparacion;

-- INNER JOIN frente a LEFT JOIN:
--   INNER JOIN solo devuelve las filas que tienen pareja en las dos tablas.
--   LEFT JOIN devuelve todas las filas de la tabla de la izquierda; si no hay pareja, las columnas
--   de la derecha salen NULL. Por eso las actividades 8, 9 y 10 necesitan LEFT JOIN.
-- GROUP BY agrupa filas con el mismo valor para calcular un dato por grupo (COUNT, SUM...).
-- SUM suma una columna dentro de cada grupo; si el grupo no tiene valores devuelve NULL.
-- COALESCE(a, b) devuelve el primer valor que no sea NULL: sirve para mostrar 0 en vez de NULL.
