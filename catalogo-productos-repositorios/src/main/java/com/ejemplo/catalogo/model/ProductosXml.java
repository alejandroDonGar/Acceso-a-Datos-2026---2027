package com.ejemplo.catalogo.model;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.ArrayList;
import java.util.List;

//Busca en el archivo una etiqueta raiz llamada "productos". Esto solo ocurre cuando trabajamos con XML
@JacksonXmlRootElement(localName = "productos")
public class ProductosXml {
    //Esto pregunta si vamos a declarar cada elemento de la lista a mano
    // Si en el fichero encuentra el campo ID, en la clase Producto debe de haber un ID. Lo mismo con las demas filas del archivo, nombre y precio. con == false hacemos que lo haga el solo
    @JacksonXmlElementWrapper(useWrapping = false)

    //Busca una propiedad dentro de la raiz llamada "productos". Esta será la lista real de productos
    @JacksonXmlProperty(localName = "producto")
    public List<Producto> productos;

    // Constructor donde incializamos la lista de productos
    public ProductosXml() {
        productos = new ArrayList<>();
    }

    public List<Producto> getProductos() {
        return productos;
    }
    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }
}
