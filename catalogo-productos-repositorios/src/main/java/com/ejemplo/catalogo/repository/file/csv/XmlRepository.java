package com.ejemplo.catalogo.repository.file.csv;

import com.ejemplo.catalogo.model.Producto;
import com.ejemplo.catalogo.model.ProductosXml;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class XmlRepository extends AbstractRepository {
    private final XmlMapper mapper;
    public XmlRepository(Path path) {
        super(path);
        productos = load();
        mapper = new XmlMapper();
        //El mapper transforma el fichero en clase. Con 2 condiciones,
        //El fichero debe de ser JSON
        //Los parametros del fichero deben de ser los mismo que nuestra clase de record
    }
    @Override
    public void saveAll(List<Producto> productos) {
        Path temporal = null;
        try {
            // Creamos una instancia de ProductosXml
            // Seteamos los productos usanmdo el setter de la clase y la variable productos.
            // Escribimos los elementos de la lista en el directorio del fichero

            ProductosXml productosXml = new ProductosXml(); // Coge la parte del producto y lo busca dentro del archivo de manera automatica
            productosXml.setProductos(productos); // Ya esta la clase etiquetada con todos los productos
            mapper.writerWithDefaultPrettyPrinter().writeValue(getPath().toFile(), productosXml);

        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo guardar " + getPath(), e);
        } finally {
        }
    }
    @Override
    public List<Producto> load() {
        try {
            // Va a la clase ProductosXml y reconoce las etiquetas.
            // productosXml -> En esta intancia va a poner lo que se encuentra dentro de ProdcutosXml usando mapper.readValue(getpath().toFile(), ProductosXml.class)
            // .clear -> Limpia la lista antigua
            // .addAll(productosXml.getProductos()) -> mete la nueva informacion

            ProductosXml productosXml = mapper.readValue(getPath().toFile(), ProductosXml.class);
            productos.clear();
            productos.addAll(productosXml.getProductos());
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo cargar " + getPath(), e);
        }
        return productos;
    }
}
