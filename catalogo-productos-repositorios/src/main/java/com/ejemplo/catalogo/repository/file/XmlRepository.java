package com.ejemplo.catalogo.repository.file;

import com.ejemplo.catalogo.model.Producto;
import com.ejemplo.catalogo.model.ProductosXml;
import com.ejemplo.catalogo.repository.IProductoRepository;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Repositorio de productos guardados en XML con Jackson (XmlMapper),
 * usando la clase envoltorio ProductosXml para la etiqueta raiz productos
 * @author AlejandroDonGar
 */
public class XmlRepository extends AbstractFileRepository<Producto, Long> implements IProductoRepository {
    //El mapper transforma el fichero en clase. Con 2 condiciones,
    //El fichero debe de ser XML
    //Los parametros del fichero deben de ser los mismo que nuestra clase de record
    private final XmlMapper mapper = new XmlMapper();

    public XmlRepository(Path path) {
        super(path);
    }

    @Override
    protected void writeAll(List<Producto> productos) {
        try {
            // Creamos una instancia de ProductosXml
            // Seteamos los productos usanmdo el setter de la clase y la variable productos.
            // Escribimos los elementos de la lista en el directorio del fichero

            ProductosXml productosXml = new ProductosXml(); // Coge la parte del producto y lo busca dentro del archivo de manera automatica
            productosXml.setProductos(productos); // Ya esta la clase etiquetada con todos los productos
            mapper.writerWithDefaultPrettyPrinter().writeValue(getPath().toFile(), productosXml);

        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo guardar " + getPath(), e);
        }
    }

    @Override
    protected List<Producto> readAll() {
        try {
            // Va a la clase ProductosXml y reconoce las etiquetas.
            // productosXml -> En esta intancia va a poner lo que se encuentra dentro de ProdcutosXml usando mapper.readValue(getpath().toFile(), ProductosXml.class)
            // getProductos() -> devuelve la lista que ha leido; AbstractFileRepository la guarda en memoria

            ProductosXml productosXml = mapper.readValue(getPath().toFile(), ProductosXml.class);
            return productosXml.getProductos();
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo cargar " + getPath(), e);
        }
    }
}
