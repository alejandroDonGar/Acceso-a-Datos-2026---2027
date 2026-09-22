package com.docencia.numeros;
/**
 * Clase que implementa los metodos de la interfaz IntegerService
 * @author AlejandroDonGar
 */
public class IntegerServiceImpl implements IntegerService {
    @Override
    public Boolean esPar(Integer numero) {
        if(numero == null) {
            return false;
        }
        return numero%2==0;
    }
    @Override
    public Integer sumarDigitos(Integer numero) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'esFechaFutura'");
    }

    @Override
    public Integer convertirTextoAEntero(String texto) {
        if(texto == null || texto.isEmpty()) {
            return 0;
        }
        return Integer.parseInt(texto.trim());
    }

    @Override
    public Boolean esNumeroPrimo(Integer numero) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'esNumeroPrimo'");
    }

    @Override
    public Integer calcularFactorial(Integer numero) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'calcularFactorial'");
    }
    
}
