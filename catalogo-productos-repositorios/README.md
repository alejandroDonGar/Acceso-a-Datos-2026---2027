# Catálogo de productos con repositorios

Proyecto Maven limpio para realizar el ejercicio.

## Paquetes preparados

- `com.ejemplo.catalogo.model`
- `com.ejemplo.catalogo.repository`
- `com.ejemplo.catalogo.repository.file`
- `com.ejemplo.catalogo.repository.file.csv`

No se incluye ninguna clase Java resuelta.

---

## Solución

Un único contrato de repositorio genérico con tres formas de guardar los productos (CSV, JSON y XML). La que se usa se elige en `data/app.properties`.

```
model/        Identifiable<ID>, Producto(Long id, String nombre, Double precio), ProductosXml
repository/   IRepository<T, ID>  ←  IProductoRepository
repository/file/
              AbstractFileRepository<T extends Identifiable<ID>, ID>   (CRUD común en memoria)
              JsonRepository, XmlRepository                            (solo readAll / writeAll)
repository/file/csv/
              CsvProductoRepository
configuration/AppConfiguration   (lee app.properties y crea el repositorio)
CsvCrudDemo   (primera versión, sin genéricos)
```

- `AbstractFileRepository` carga la lista la primera vez que se usa (en su constructor los atributos de la hija, como el `CSVFormat` o el `mapper`, todavía no existen) y compara los id con `equals`, no con `==`.
- `create` devuelve `false` si la entidad es nula, no tiene id o el id ya existe.
- JSON escribe en un fichero temporal y lo mueve con `ATOMIC_MOVE`; XML usa la clase envoltorio `ProductosXml` para la etiqueta raíz `<productos>`.
- El esbozo de SQLite se quitó: el repositorio con JDBC se hizo después en `gestion-java-jdbc-dao`.

## Pruebas

`ProductoRepositoryTest` pasa el mismo CRUD por las tres implementaciones sobre ficheros temporales (`@TempDir`), con un id mayor que 127 para comprobar el `equals`, y vuelve a abrir el fichero para ver que se guardó.

**Comando**
```bash
mvn clean test
```
**Resultado**
```
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.420 s -- in com.ejemplo.catalogo.repository.ProductoRepositoryTest
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

## Ejecutar

Con `storage.format=json` y `storage.path=data/productos.json` en `data/app.properties`:

**Comando**
```bash
mvn -q compile exec:java -Dexec.mainClass=com.ejemplo.catalogo.configuration.AppConfiguration
```
**Resultado**
```
Formato fichero: json
Ruta fichero: data/productos.json
Productos: [Producto[id=1, nombre=Teclado, precio=29.99], Producto[id=2, nombre=Raton, precio=15.5], Producto[id=3, nombre=Monitor, precio=189.99]]
create: true
create repetido: false
findById(99): Producto[id=99, nombre=Alfombrilla, precio=9.95]
update: true
findById(99): Producto[id=99, nombre=Alfombrilla XL, precio=14.95]
delete: true
Productos: [Producto[id=1, nombre=Teclado, precio=29.99], Producto[id=2, nombre=Raton, precio=15.5], Producto[id=3, nombre=Monitor, precio=189.99]]
```

Para usar CSV o XML basta con cambiar `storage.format` (`csv`, `xml`) y `storage.path` en el `.properties`; el resto del programa solo conoce `IProductoRepository`.
