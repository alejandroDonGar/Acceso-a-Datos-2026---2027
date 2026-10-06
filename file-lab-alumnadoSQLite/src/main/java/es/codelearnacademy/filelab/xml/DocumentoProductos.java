package es.codelearnacademy.filelab.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import es.codelearnacademy.filelab.model.Producto;
import java.util.ArrayList;
import java.util.List;

@JacksonXmlRootElement(localName = "productos")
public class DocumentoProductos {

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "producto")
    private List<Producto> productos;

    public DocumentoProductos() {
    }

    public DocumentoProductos(List<Producto> productos) {
        this.productos = new ArrayList<>();
    }

    public List<Producto> getProductos() {
        DocumentoProductos documentoProductos = new DocumentoProductos();
        documentoProductos.setProductos(productos);
        return documentoProductos.getProductos();
    }

    public void setProductos(List<Producto> productos) {
        throw new UnsupportedOperationException("Función no implementada");
    }
}
