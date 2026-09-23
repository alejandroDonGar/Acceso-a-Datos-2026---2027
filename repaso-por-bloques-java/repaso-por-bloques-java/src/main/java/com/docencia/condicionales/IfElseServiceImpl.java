package com.docencia.condicionales;

/**
 * Clase que implementa los metodos de la interfaz IfElseService
 * @author AlejandroDonGar
 */
public class IfElseServiceImpl implements IfElseService {

    @Override
    public String clasificarEdad(Integer edad) {
        // Una edad null o negativa no existe: dato invalido.
        if (edad == null || edad < 0) {
            throw new IllegalArgumentException();
        }
        // Hasta 17 anios (inclusive) es menor de edad.
        if (edad <= 17) {
            return "MENOR";
        }
        // Si no ha entrado en el if anterior, tiene 18 o mas.
        return "ADULTO";
    }

    @Override
    public String evaluarNota(Integer nota) {
        // Una nota null o fuera del rango 0-10 no es valida: devolvemos null como dice la interfaz.
        if (nota == null || nota > 10 || nota < 0) {
            return null;
        }
        // 5 o mas es aprobado.
        if (nota >= 5) {
            return "APROBADO";
        } else {
            // Si es menor de 5 (y ya sabemos que no es negativa) es suspenso.
            return "SUSPENSO";
        }
    }

    @Override
    public Boolean puedeAcceder(Boolean activo, Integer edad) {
        // Con datos null (o edad negativa) no se puede decidir: no dejamos entrar.
        // Ponemos las comprobaciones de null primero porque, si activo fuese null, hacer "!activo" daria un error.
        if (activo == null || edad == null || edad < 0) {
            return false;
        }
        // Si la persona no esta activa no entra, tenga la edad que tenga.
        if (!activo) {
            return false;
        }
        // Activa y con 18 o mas: entra. Devolvemos directamente el resultado de la comparacion.
        return edad >= 18;
    }

    @Override
    public String clasificarTemperatura(Double temperatura) {
        // Sin temperatura no se puede clasificar.
        if (temperatura == null) {
            return null;
        }
        // 30 grados o mas se considera calor.
        if (temperatura >= 30.0) {
            return "CALOR";
        }
        // Menos de 30 grados.
        return "NO CALOR";
    }

    @Override
    public String calcularResultadoComparacion(Integer primero, Integer segundo) {
        // Sin los dos numeros no se puede comparar.
        if (primero == null || segundo == null) {
            return null;
        }
        // IMPORTANTE: con objetos Integer se usa equals, no ==. El == compara si son el MISMO objeto y solo
        // "funciona" por casualidad con numeros pequeños (Java reutiliza los objetos entre -128 y 127).
        if (primero.equals(segundo)) {
            return "IGUALES";
        }
        return "DIFERENTES";
    }
}
