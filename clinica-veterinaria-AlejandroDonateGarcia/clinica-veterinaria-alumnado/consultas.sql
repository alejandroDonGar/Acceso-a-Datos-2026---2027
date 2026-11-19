-- 1. Todos los clientes alfabéticamente
SELECT id, nombre, email, telefono
FROM clientes
ORDER BY nombre;

-- 2. Mascotas de una especie (modifica 'Perro' si deseas)
SELECT id, nombre, especie, raza, fecha_nacimiento
FROM mascotas
WHERE especie = 'Perro';

-- 3. Mascotas de un cliente
-- Mascotas del cliente con id 1.
SELECT id, nombre, especie, raza
FROM mascotas
WHERE id_cliente = 1;

-- 4. Veterinarios de una especialidad
SELECT id, nombre, especialidad, tarifa_hora
FROM veterinarios
WHERE especialidad = 'Medicina general'
ORDER BY tarifa_hora;

-- 5. Consultas programadas
-- La fecha es texto ISO (AAAA-MM-DD...), asi que ordenarla como texto la ordena por fecha.
SELECT id, id_mascota, id_veterinario, fecha, horas
FROM consultas
WHERE estado = 'PROGRAMADA'
ORDER BY fecha;

-- 6a. Mascota y propietario con WHERE
SELECT m.nombre AS mascota, c.nombre AS propietario
FROM mascotas m, clientes c
WHERE m.id_cliente = c.id;

-- 6b. Mascota y propietario con INNER JOIN
-- Mismo resultado que 6a: la condicion de union va en ON y queda separada de los filtros.
SELECT m.nombre AS mascota, c.nombre AS propietario
FROM mascotas m
INNER JOIN clientes c ON m.id_cliente = c.id;

-- 7. Consultas, mascota, propietario y veterinario
SELECT co.id, co.fecha, co.estado, m.nombre AS mascota, c.nombre AS propietario, v.nombre AS veterinario
FROM consultas co
INNER JOIN mascotas m ON co.id_mascota = m.id
INNER JOIN clientes c ON m.id_cliente = c.id
INNER JOIN veterinarios v ON co.id_veterinario = v.id
ORDER BY co.fecha;

-- 8. Cantidad de mascotas por cliente, incluidos los de cero mascotas
-- LEFT JOIN conserva los clientes sin mascotas; COUNT(m.id) no cuenta los NULL y da 0.
SELECT c.id, c.nombre, COUNT(m.id) AS num_mascotas
FROM clientes c
LEFT JOIN mascotas m ON m.id_cliente = c.id
GROUP BY c.id, c.nombre
ORDER BY c.id;

-- 9. Importe tratamientos, incluyendo consultas sin tratamientos
-- SUM da NULL si la consulta no tiene tratamientos; COALESCE lo cambia por 0.
SELECT co.id, COALESCE(SUM(t.cantidad * t.precio_unitario), 0) AS importe_tratamientos
FROM consultas co
LEFT JOIN tratamientos t ON t.id_consulta = co.id
GROUP BY co.id
ORDER BY co.id;

-- 10. Consultas sin tratamientos
-- LEFT JOIN + quedarse con las filas sin tratamiento (t.id IS NULL).
SELECT co.id, co.fecha, co.estado
FROM consultas co
LEFT JOIN tratamientos t ON t.id_consulta = co.id
WHERE t.id IS NULL
ORDER BY co.id;
