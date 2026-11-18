# Memoria — Taller mecánico (JDBC + SQLite)

## 1. Reparto de responsabilidades

| Clase | Qué hace | Qué no hace |
| --- | --- | --- |
| `ConexionBD` | Abre las conexiones: valida la URL, comprueba el archivo, activa las claves foráneas y verifica el esquema | No contiene SQL de ninguna tabla |
| `AbstractDao<T, ID>` | Lo común a los cinco DAO: `listar`, `buscarPorId` y `eliminar` (solo cambian tabla y columna clave), ejecutar sentencias con parámetros, recuperar el id generado y registrar errores | No sabe cómo se valida ni cómo se inserta cada entidad |
| Cada DAO | Su SQL de `insertar`/`actualizar`, sus validaciones, sus consultas propias y `mapear` (fila → objeto) | No abre conexiones a mano ni repite el recorrido del `ResultSet` |

**Por qué `AbstractDao<T, ID>`:** `T` es la entidad y `ID` el tipo de su clave. Cuatro tablas usan `Integer` autogenerado y `vehiculos` usa la matrícula (`String`), así que con un solo genérico `ID` sirve la misma clase base para todas.

**Lo que se queda en cada DAO:** `insertar` y `actualizar`. Las columnas y reglas cambian por tabla, y meterlas en la base habría obligado a llenar `AbstractDao` de excepciones. Además, `VehiculoDao.insertar` devuelve `1` (la matrícula la aporta el objeto) y el resto devuelven el id generado.

## 2. Decisiones sobre errores

- La API pública de los DAO no declara `throws SQLException`: el error se captura en `AbstractDao` y se devuelve el valor de respaldo del contrato (lista vacía, `Optional.empty()` o `0`).
- Se registra con `java.util.logging` solo la operación y el motivo (por ejemplo, `Fallo al insertar en clientes: UNIQUE constraint failed`), sin los datos personales del registro.
- **Limitación:** estos retornos no distinguen «no hay datos» de «ha fallado la base». Una lista vacía puede significar las dos cosas; solo el log lo aclara.
- Las entradas inválidas se rechazan en Java antes de lanzar el SQL. Eso incluye objetos o ids `null`, textos vacíos o con solo espacios, importes y horas negativos, cantidad ≤ 0, estado fuera de `PENDIENTE`/`EN_PROCESO`/`FINALIZADA` y fechas que no tienen formato `AAAA-MM-DD` o no existen (se comprueba con `LocalDate.parse`).
- `calcularCostePiezas` suma en Java con `BigDecimal` para no pasar por `double`, y devuelve `0` si no hay piezas.

## 3. Comprobación del archivo antes de conectar

SQLite crea una base vacía si se conecta a una ruta que no existe, así que conectar sin error no demuestra nada. `ConexionBD.abrir`:

1. Exige una URL que empiece por `jdbc:sqlite:`.
2. Con `Files.isRegularFile` y `Files.isReadable` comprueba, **antes de llamar a JDBC**, que la ruta es un archivo que existe y se puede leer. Una carpeta o una ruta inexistente lanzan `SQLException`, y no se crea nada.
3. Conecta, ejecuta `PRAGMA foreign_keys = ON` y cuenta en `sqlite_master` las cinco tablas del taller. Si el archivo no es SQLite, la consulta falla; si faltan tablas, se lanza `SQLException`.
4. Si algo falla después de conectar, cierra la conexión antes de lanzar el error, para no dejar el archivo bloqueado (en Windows impediría borrarlo).

## 4. Integridad referencial

Experimento hecho sobre una **copia** de `datos/taller.sqlite3`:

| Operación | `foreign_keys = 0` | `foreign_keys = 1` |
| --- | --- | --- |
| Insertar vehículo con `id_cliente = 999` (no existe) | Aceptado | `FOREIGN KEY constraint failed` |
| Asociar la reparación 1 al mecánico 999 (no existe) | Aceptado | `FOREIGN KEY constraint failed` |
| Borrar el cliente 1, que tiene vehículos | Aceptado | `FOREIGN KEY constraint failed` (`ON DELETE RESTRICT`) |

- Con las claves foráneas desactivadas, el vehículo huérfano `X999` **no aparece** en un `INNER JOIN` con clientes (no tiene pareja). En un `LEFT JOIN` desde vehículos sí sale, con el nombre del cliente a `NULL`.
- Relacionar tablas (`JOIN ... ON` o `WHERE`) y proteger la relación (clave foránea) son cosas distintas. La consulta une por la condición que escribes, haya o no `FOREIGN KEY`.
- `PRAGMA foreign_keys` vale para **cada conexión**. Por eso se activa en `ConexionBD.abrir`, el único sitio por donde pasan todas las conexiones de los DAO.

## 5. Resultado de las pruebas

Suite ejecutada: las 90 pruebas obligatorias (50 de los cinco DAO y 40 de `AmpliacionTest`), sin añadir ni desactivar ninguna.

**Comando**
```bash
mvn clean test
```
**Resultado**
```
[INFO] Tests run: 40, Failures: 0, Errors: 0, Skipped: 0 -- in es.ejemplo.taller.AmpliacionTest
[INFO] Tests run: 10, Failures: 0, Errors: 0, Skipped: 0 -- in es.ejemplo.taller.ClienteDaoTest
[INFO] Tests run: 10, Failures: 0, Errors: 0, Skipped: 0 -- in es.ejemplo.taller.MecanicoDaoTest
[INFO] Tests run: 10, Failures: 0, Errors: 0, Skipped: 0 -- in es.ejemplo.taller.PiezaReparacionDaoTest
[INFO] Tests run: 10, Failures: 0, Errors: 0, Skipped: 0 -- in es.ejemplo.taller.ReparacionDaoTest
[INFO] Tests run: 10, Failures: 0, Errors: 0, Skipped: 0 -- in es.ejemplo.taller.VehiculoDaoTest
[INFO] Tests run: 90, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```
