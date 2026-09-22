package com.docencia.condicionales;

public class IfElseServiceImpl implements IfElseService {

    @Override
    public String clasificarEdad(Integer edad) {
        if(edad == null || edad < 0) {
            throw new IllegalArgumentException();
        }
        if(edad <= 17) {
            return "MENOR";
        }
        return "MAYOR";
    }
    @Override
    public String evaluarNota(Integer nota) {
        if(nota == null || nota>10|| nota < 0) {
            return null;
        }
        if(nota>=5) {
            return "APROBADO";
        } else {
            return "SUSPENSO";
        }
    }

    @Override
    public Boolean puedeAcceder(Boolean activo, Integer edad) {
        if (activo == false || edad < 0) {
            return false;
        }
        if(edad>=18) {
            return true;
        }
        return false;
    }

    @Override
    public String clasificarTemperatura(Double temperatura) {
        if(temperatura == null) {
            return null;
        }
        if(temperatura >=30.0) {
            return "CALOR";
        }
        return "NO CALOR";
    }

    @Override
    public String calcularResultadoComparacion(Integer primero, Integer segundo) {
        if(primero == null || segundo == null) {
            return null;
        }
        if(primero==segundo) {
            return "IGUALES";
        }
        return "DIFERENTES";
    }
}
