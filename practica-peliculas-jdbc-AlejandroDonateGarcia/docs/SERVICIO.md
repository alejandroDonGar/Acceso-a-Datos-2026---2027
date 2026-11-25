# Recorrido de una petición por el servicio

En este documento sigo dos peticiones de la API de películas desde que llegan al controlador hasta que tocan la tabla `peliculas` de SQLite, y de vuelta: un POST que crea una película y un GET que la busca por su código. La ruta base es `/api/v1/jdbc/peliculas` y la aplicación arranca con el perfil `jdbc`.

Las dos usan la misma película de ejemplo:

```json
{ "codigo": "PEL003", "titulo": "Arrival", "anioEstreno": 2016, "duracionMinutos": 116 }
```

## POST: crear una película

### 1. Clases por las que pasa

Ida, de la petición a la base de datos:

1. `PeliculaJdbcController.crear` recibe el JSON. Spring lo convierte en un `PeliculaRequest` y lo valida con `@Valid`.
2. `PeliculaApiMapper.aDatosCreacion` lo pasa a `DatosCreacionPelicula`.
3. `PeliculaService.crear` recibe esos datos y construye la `Pelicula` con `PeliculaDominioMapper.aPelicula`.
4. El servicio llama a `PeliculaRepository.insertarSiAusente`. Detrás de esa interfaz está `PeliculaJdbcRepository`.
5. `PeliculaJdbcRepository` convierte la `Pelicula` en `PeliculaEntity` con `PeliculaEntityMapper.aEntity` y ejecuta el `INSERT ... ON CONFLICT(pelicula_codigo) DO NOTHING` con `JdbcTemplate.update`.

Vuelta, de la base de datos a la respuesta:

1. `update` devuelve cuántas filas ha insertado. El repositorio devuelve `true` solo si es exactamente 1.
2. Si es `true`, el servicio devuelve un `Optional` con la misma `Pelicula` que construyó en el paso 3, sin volver a leerla de la tabla. Si es `false`, devuelve un `Optional` vacío.
3. El controlador convierte la `Pelicula` en `PeliculaResponse` con `PeliculaApiMapper.aRespuesta`, y Spring la escribe como JSON.

### 2. Mapper de cada frontera

| Frontera | Mapper | Método |
|---|---|---|
| API → servicio | `PeliculaApiMapper` | `aDatosCreacion(PeliculaRequest)` |
| servicio → dominio | `PeliculaDominioMapper` | `aPelicula(DatosCreacionPelicula)` |
| dominio → fila | `PeliculaEntityMapper` | `aEntity(Pelicula)` |
| fila → tabla | ninguno: `PeliculaJdbcRepository` pasa los getters de la entidad a los `?` del INSERT en orden |
| dominio → API (respuesta) | `PeliculaApiMapper` | `aRespuesta(Pelicula)` |

En el POST no interviene `PeliculaRowMapper`, porque no se lee ninguna fila.

### 3. Nombre de cada dato en cada capa

| JSON (`PeliculaRequest`) | `DatosCreacionPelicula` | `Pelicula` | `PeliculaEntity` | Columna SQLite |
|---|---|---|---|---|
| `codigo` | `referenciaSolicitada` | `identificador` | `codigoFila` | `pelicula_codigo` |
| `titulo` | `nombreSolicitado` | `nombre` | `tituloFila` | `titulo_texto` |
| `anioEstreno` | `anioSolicitado` | `anioLanzamiento` | `anioFila` | `estreno_anio` |
| `duracionMinutos` | `minutosSolicitados` | `minutosProyeccion` | `minutosFila` | `duracion_minutos` |

En la respuesta, `PeliculaResponse` vuelve a usar los nombres del JSON (`codigo`, `titulo`, `anioEstreno`, `duracionMinutos`).

### 4. Cómo se decide el código HTTP

- 400: Spring lo devuelve antes de entrar en el método si el cuerpo no cumple las reglas de `PeliculaRequest`. Por ejemplo, un código que no sigue `PELnnn`, un título vacío o de más de 120 caracteres, o un año fuera de 1888–2100. El servicio no llega a ejecutarse.
- 201: si el `INSERT` ha añadido una fila, el servicio devuelve la película y el controlador responde `ResponseEntity.status(HttpStatus.CREATED)` con el `PeliculaResponse`.
- 409: si ya había una película con ese código, `ON CONFLICT ... DO NOTHING` no inserta nada y `update` devuelve 0. El repositorio devuelve `false`, el servicio un `Optional` vacío, y el controlador responde con un `ProblemDetail` de estado `CONFLICT` y el detalle «Ya existe una película con el código PEL003».

El servicio no lanza excepciones para el duplicado, así que no hace falta un manejador global. El controlador decide el código a partir de si el `Optional` trae valor o no.

## GET por código: obtener una película

### 1. Clases por las que pasa

Ida:

1. `PeliculaJdbcController.obtener` recibe `GET /api/v1/jdbc/peliculas/PEL003`. `@PathVariable` saca `PEL003` de la URL y `@Pattern` comprueba su formato.
2. `PeliculaService.obtener` recibe el código como `String` y llama a `PeliculaRepository.buscarPorCodigo`.
3. `PeliculaJdbcRepository.buscarPorCodigo` ejecuta `SELECT ... FROM peliculas WHERE pelicula_codigo = ?` con `JdbcTemplate.query`.

Vuelta:

1. `JdbcTemplate` llama a `PeliculaRowMapper` por cada fila del `ResultSet` y obtiene una lista de `PeliculaEntity` (como el código es clave primaria, como mucho tiene una).
2. El repositorio toma la primera con `stream().findFirst()` y la convierte en `Pelicula` con `PeliculaEntityMapper.aPelicula`. Si la lista está vacía, devuelve un `Optional` vacío. Uso `query` con `findFirst` en lugar de `queryForObject` porque este último lanza una excepción cuando no hay fila.
3. El servicio devuelve ese `Optional` tal cual.
4. El controlador convierte la `Pelicula` en `PeliculaResponse` con `PeliculaApiMapper.aRespuesta`.

### 2. Mapper de cada frontera

| Frontera | Mapper | Método |
|---|---|---|
| API → servicio | ninguno: solo viaja el código como `String` |
| tabla → fila | `PeliculaRowMapper` | `mapRow(ResultSet, int)` |
| fila → dominio | `PeliculaEntityMapper` | `aPelicula(PeliculaEntity)` |
| dominio → API | `PeliculaApiMapper` | `aRespuesta(Pelicula)` |

### 3. Nombre de cada dato en cada capa

El código de entrada se llama `codigo` en el controlador y en el servicio, `identificador` en el repositorio y llega al `?` que se compara con `pelicula_codigo`.

Los datos de vuelta recorren el camino contrario al del POST:

| Columna SQLite | `PeliculaEntity` | `Pelicula` | JSON (`PeliculaResponse`) |
|---|---|---|---|
| `pelicula_codigo` | `codigoFila` | `identificador` | `codigo` |
| `titulo_texto` | `tituloFila` | `nombre` | `titulo` |
| `estreno_anio` | `anioFila` | `anioLanzamiento` | `anioEstreno` |
| `duracion_minutos` | `minutosFila` | `minutosProyeccion` | `duracionMinutos` |

`PeliculaRowMapper` lee cada columna por su nombre (`rs.getString("pelicula_codigo")`, `rs.getInt("estreno_anio")`...), así que no depende del orden del `SELECT`.

### 4. Cómo se decide el código HTTP

- 400: si el código de la URL no cumple `^PEL[0-9]{3}$` (por ejemplo `PEL3` o `XYZ001`), Spring lo rechaza por el `@Pattern` y el servicio no se ejecuta. La clase no lleva `@Validated`. Con esa anotación, el fallo saldría como una excepción que, sin manejador global, acabaría en 500.
- 200: si el `Optional` trae una película, el controlador responde `ResponseEntity.ok(...)` con el `PeliculaResponse`.
- 404: si el `Optional` está vacío, el controlador llama a `noExiste(codigo)`, que devuelve un `ProblemDetail` con estado `NOT_FOUND` y el detalle «No existe ninguna película con el código PEL003». PUT y DELETE usan el mismo método.
