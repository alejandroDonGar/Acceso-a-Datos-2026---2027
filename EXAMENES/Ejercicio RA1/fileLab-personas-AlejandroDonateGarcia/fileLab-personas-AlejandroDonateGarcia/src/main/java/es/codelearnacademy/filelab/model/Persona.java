package es.codelearnacademy.filelab.model;
/**
 * Modelo de una persona que guardan los repositorios CSV, JSON y XML
 * @author AlejandroDonGar
 */
public record Persona(String dni, String nombre, String email, int edad, boolean activo) {
}
