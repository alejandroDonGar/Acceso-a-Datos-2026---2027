# Memoria — Taller mecánico con JPA / Hibernate

Mismo taller y mismas 90 pruebas que la versión JDBC, pero los DAO usan `EntityManager` y JPQL en lugar de SQL a mano.

## 1. Reparto de responsabilidades

| Clase | Qué hace |
| --- | --- |
| `ConexionBD` | Comprueba la base antes de usarla: URL `jdbc:sqlite:`, archivo existente y legible, que sea SQLite y que tenga las cinco tablas |
| `OrmFactory` | Crea la `EntityManagerFactory` de la unidad `tallerPU` con la URL recibida |
| Entidades (`orm/entity`) | Una clase por tabla, con `@Entity`, `@Table` y `@Column` con los nombres reales. Las FK se escriben como columna plana (`idCliente`, `matricula`…) y además hay una relación `@ManyToOne(LAZY)` de solo lectura (`insertable = false, updatable = false`) |
| Mappers (`orm/mapper`) | Pasan de dominio a entidad y al revés, para que la aplicación no reciba objetos administrados por Hibernate |
| `AbstractDao<T, E, ID>` | Lo común: `listar`, `buscarPorId`, `eliminar`, `consultar` (JPQL con parámetros) y `escribir` (transacción con `commit`/`rollback`) |
| Cada DAO | Su JPQL, sus validaciones y qué campos copia en la entidad al insertar o actualizar |

**Por qué `AbstractDao<T, E, ID>`:**
- `T` es el modelo de dominio, `E` la entidad JPA e `ID` el tipo de la clave: `Integer`, o `String` para la matrícula.
- Con genéricos no se puede escribir `E.class`, así que el constructor recibe el `Class<E>` y el mapper `toDomain`. Sin ellos no se podían escribir `em.find` ni `createQuery` de forma genérica.

**Lo que no va a la clase base:** `insertar` y `actualizar`. Cada tabla copia campos distintos, y `VehiculoDao.insertar` devuelve `1` en vez del id generado.

## 2. Decisiones sobre errores y transacciones

- `escribir` abre el `EntityManager` y la transacción, ejecuta la acción y hace `commit`. Si salta una `PersistenceException` (email repetido, matrícula duplicada…), hace `rollback`, lo registra y devuelve `0`. No se captura `RuntimeException` en general, para no tapar errores de programación.
- Las lecturas sin transacción devuelven lista vacía u `Optional.empty()` si fallan. Como en JDBC, eso no distingue «no hay datos» de «ha fallado»; solo el log lo aclara.
- Al insertar con id autogenerado se hace `persist` y luego `flush`, que fuerza el `INSERT` y deja el id disponible antes del `commit`.
- Al actualizar se hace `find` y se cambian los atributos de la entidad. Como está administrada, el `commit` guarda los cambios sin llamar a `merge`.
- Antes de lanzar nada se rechazan en Java:
  - objetos o ids `null`;
  - textos vacíos o con solo espacios;
  - importes y horas negativos, y cantidades ≤ 0;
  - estados fuera de los tres permitidos;
  - fechas que no son `AAAA-MM-DD` reales.
- `calcularCostePiezas` suma en Java con `BigDecimal`, como propone la guía, en lugar de usar un `SUM` que pasaría por `double`.

## 3. Comprobación del archivo e integridad referencial

- Cada DAO llama a `ConexionBD.abrir(url)` en su constructor, antes de crear la `EntityManagerFactory`. Si la base no existe o le faltan tablas, el DAO no se crea. Así no se crea una base vacía sin querer.
- La investigación de integridad referencial es la misma que en la versión JDBC: misma base, mismos resultados. Con `foreign_keys = 0` se aceptan un vehículo con un cliente inexistente, una reparación con un mecánico inexistente y el borrado de un cliente con vehículos. Con `= 1` las tres fallan con `FOREIGN KEY constraint failed`.
- **Limitación:** las conexiones que abre Hibernate no pasan por `ConexionBD`, así que en esta versión no se activa `PRAGMA foreign_keys`. El README no lo exige; para activarlo habría que configurarlo en la conexión que usa Hibernate.

## 4. Comparación de una operación: insertar un cliente

| | JDBC (`taller-mecanico-alejandroDonateGarcia`) | JPA (este proyecto) |
| --- | --- | --- |
| SQL | Escrito a mano: `INSERT INTO clientes (nombre, email, telefono) VALUES (?,?,?)` | Ninguno: `em.persist(entidad)` y Hibernate genera el `INSERT` |
| Copia de columnas | `ps.setString(1, …)` columna a columna y, al leer, `rs.getString("nombre")` | La entidad declara las columnas con `@Column` una sola vez; el mapper convierte dominio ↔ entidad |
| Id generado | `prepareStatement(sql, RETURN_GENERATED_KEYS)` + `getGeneratedKeys()` | `persist` + `flush` y `entidad.getIdCliente()` |
| Transacción | Autocommit de JDBC: cada sentencia se confirma sola | `tx.begin()` / `commit()` / `rollback()` explícitos en `escribir` |
| Código del DAO | `ClienteDao` con `insertar` y `actualizar`; el resto lo hereda de `AbstractDao` | Igual de pequeño: la repetición está en `AbstractDao` |
| Resultado | id generado, o `0` si se rechaza | El mismo: las 90 pruebas son las mismas y pasan en las dos versiones |

## 5. Resultado de las pruebas

Suite: las 90 pruebas obligatorias (50 de los DAO y 40 de `AmpliacionTest`), sin cambios.

**En Windows** las pruebas dan error al borrar la base temporal en `@AfterEach`: «El proceso no tiene acceso al archivo porque está siendo utilizado por otro proceso». Cada DAO crea su `EntityManagerFactory`, las pruebas nunca la cierran y el pool de conexiones de Hibernate deja el archivo abierto. Windows no deja borrar un archivo abierto y Linux sí. Por eso se ejecutan en WSL.

**Comando** (en WSL, desde la carpeta del proyecto)
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
