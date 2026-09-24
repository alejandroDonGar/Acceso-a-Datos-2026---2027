package com.docencia.herencia;

/**
 * Clase que implementa los metodos de la interfaz HerenciaService
 * @author AlejandroDonGar
 */
public class HerenciaServiceImpl implements HerenciaService {

    @Override
    public String obtenerNombreCompleto(Persona persona) {
        // Sin persona no hay nombre que devolver (y llamar a getNombre sobre null daria un error).
        if (persona == null) {
            throw new IllegalArgumentException();
        }
        // Unimos nombre + un espacio + apellidos: "Ana" + " " + "Lopez" = "Ana Lopez".
        return persona.getNombre() + " " + persona.getApellidos();
    }

    @Override
    public Boolean esAlumno(Persona persona) {
        // instanceof pregunta "¿este objeto es realmente un Alumno?". Como Alumno hereda de Persona, una variable de
        // tipo Persona puede guardar un Alumno, y asi lo distinguimos. Si persona es null, instanceof da false directamente.
        return persona instanceof Alumno;
    }

    @Override
    public String obtenerDescripcionPersona(Persona persona) {
        // Sin persona no hay nada que describir.
        if (persona == null) {
            throw new IllegalArgumentException();
        }
        // Reutilizamos los otros metodos de esta clase para no repetir logica:
        // el tipo (ALUMNO o PERSONA) y el nombre completo.
        // Resultado con el formato pedido: "ALUMNO: Ana Lopez (20)".
        return obtenerTipoPersona(persona) + ": " + obtenerNombreCompleto(persona) + " (" + persona.getEdad() + ")";
    }

    @Override
    public Boolean tieneEdadMinima(Persona persona, Integer edadMinima) {
        // Necesitamos la persona, su edad y la edad minima (si alguna es null, el >= de abajo daria error).
        if (persona == null || persona.getEdad() == null || edadMinima == null) {
            throw new IllegalArgumentException();
        }
        // Cumple si su edad es igual o mayor que el minimo exigido (17 con minimo 18 -> false).
        return persona.getEdad() >= edadMinima;
    }

    @Override
    public String obtenerTipoPersona(Persona persona) {
        // Sin persona no hay tipo.
        if (persona == null) {
            throw new IllegalArgumentException();
        }
        // Si el objeto es un Alumno devolvemos ALUMNO.
        if (persona instanceof Alumno) {
            return "ALUMNO";
        }
        // Si es cualquier otro tipo de Persona, lo llamamos PERSONA.
        return "PERSONA";
    }
}