package com.docencia.condicionales;

/**
 * Clase que implementa los metodos de la interfaz SwitchService
 * @author AlejandroDonGar
 */
public class SwitchServiceImpl implements SwitchService {

    @Override
    public String obtenerNombreDia(Integer numeroDia) {
        // Un switch sobre un Integer null daria error, asi que lo comprobamos antes.
        if (numeroDia == null) {
            throw new IllegalArgumentException();
        }
        // switch: salta al "case" cuyo valor coincide con numeroDia.
        switch (numeroDia) {
            case 1:
                // El return sale del metodo, por eso aqui no hace falta "break".
                return "LUNES";
            case 2:
                return "MARTES";
            case 3:
                return "MIERCOLES";
            case 4:
                return "JUEVES";
            case 5:
                return "VIERNES";
            case 6:
                return "SABADO";
            case 7:
                return "DOMINGO";
            default:
                // default recoge cualquier otro numero (0, 9, negativos...): no es un dia valido.
                throw new IllegalArgumentException();
        }
    }

    @Override
    public Double calcularDescuentoPorTipo(String tipoCliente, Double importe) {
        // Un switch sobre un String null daria error, asi que validamos antes.
        if (tipoCliente == null || importe == null) {
            throw new IllegalArgumentException();
        }
        // switch sobre texto: compara el String con cada case (distingue mayusculas de minusculas).
        switch (tipoCliente) {
            case "VIP":
                // El VIP tiene un 20% de descuento: importe * 0.20 (100.0 -> 20.0).
                return importe * 0.20;
            case "NORMAL":
                // El cliente normal no tiene descuento.
                return 0.0;
            default:
                // Cualquier otro tipo no lo conocemos.
                throw new IllegalArgumentException();
        }
    }

    @Override
    public String obtenerMensajeEstado(String estado) {
        // Validamos null antes del switch.
        if (estado == null) {
            throw new IllegalArgumentException();
        }
        switch (estado) {
            case "ACTIVO":
                return "Elemento activo";
            case "INACTIVO":
                return "Elemento inactivo";
            case "PENDIENTE":
                return "Elemento pendiente";
            default:
                // Estado que no esta en la lista.
                throw new IllegalArgumentException();
        }
    }

    @Override
    public Integer obtenerDiasDelMes(Integer mes) {
        // Validamos null antes del switch.
        if (mes == null) {
            throw new IllegalArgumentException();
        }
        switch (mes) {
            // Varios case seguidos SIN break ni return "caen" al siguiente: asi agrupamos los meses que dan lo mismo.
            case 1:  // enero
            case 3:  // marzo
            case 5:  // mayo
            case 7:  // julio
            case 8:  // agosto
            case 10: // octubre
            case 12: // diciembre
                return 31;
            case 4:  // abril
            case 6:  // junio
            case 9:  // septiembre
            case 11: // noviembre
                return 30;
            case 2:
                // Febrero: 28 dias (no tenemos en cuenta los bisiestos).
                return 28;
            default:
                // Un mes fuera de 1-12 no existe.
                throw new IllegalArgumentException();
        }
    }

    @Override
    public String obtenerCategoriaProducto(String codigo) {
        // Sin codigo (o vacio) no hay primera letra que mirar.
        if (codigo == null || codigo.isEmpty()) {
            throw new IllegalArgumentException();
        }
        // charAt(0) es la primera letra del codigo; toUpperCase para que "t001" funcione igual que "T001".
        char letra = Character.toUpperCase(codigo.charAt(0));
        // switch tambien funciona con char.
        switch (letra) {
            case 'T':
                return "TECNOLOGIA";
            case 'A':
                return "ALIMENTACION";
            case 'R':
                return "ROPA";
            default:
                // Letra que no corresponde a ninguna categoria conocida.
                throw new IllegalArgumentException();
        }
    }
}
