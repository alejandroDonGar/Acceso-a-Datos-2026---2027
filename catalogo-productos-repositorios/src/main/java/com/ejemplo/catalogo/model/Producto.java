package com.ejemplo.catalogo.model;

public record Producto(Long id, String nombre, Double precio) implements Identifiable<Long> {}