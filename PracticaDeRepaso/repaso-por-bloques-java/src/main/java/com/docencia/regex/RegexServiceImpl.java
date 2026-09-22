package com.docencia.regex;
/**
 * Clase que implementa los metodos de regex de la interfaz RegexService
 * @author AlejandroDonGar
 */
import java.util.regex.Pattern;

public class RegexServiceImpl implements RegexService {
    @Override
    public Boolean validarDni(String dni) {
        if(dni==null) {
            throw new IllegalArgumentException();
        }
        return Pattern.matches(dni, "^[0-9]{8}[A-Z]$");
    }
    @Override
    public Boolean validarEmail(String email) {
        if(email==null) {
            throw new IllegalArgumentException();
        }
        return Pattern.matches(email, "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    }
    @Override
    public Boolean validarTelefono(String telefono) {
        if(telefono==null) {
            throw new IllegalArgumentException();
        }
        return Pattern.matches(telefono, "^(+[0-9]{1,3})?[0-9]{9}$");
    }
    @Override
    public Boolean validarNombre(String nombre) {
        if(nombre==null){
            throw new IllegalArgumentException();
        }
        return Pattern.matches(nombre, "^[a-záéíóúA-ZÁÉÍÓÚÑñ\\s]+$");
    }
    @Override
    public Boolean validarCodigoPostal(String codigoPostal) {
        if(codigoPostal==null) {
            throw new IllegalArgumentException();
        }
        return Pattern.matches(codigoPostal, "^([0][1-9]|[1-4][0-9]|[5][0-2])[0-9]{3}$");
    }
    @Override
    public Boolean validarMatricula(String matricula) {
        if(matricula==null) {
            throw new IllegalArgumentException();
        }
        return Pattern.matches(matricula, "^[0-9]{4}[A-Z]{3}$");
    }
}