package es.codelearnacademy.filelab.model;

public record Vehiculo(String matricula, String marca, String modelo, int anio)  implements Identifiable<String>{
    @Override
    public String id() {
        return matricula;
    }
}