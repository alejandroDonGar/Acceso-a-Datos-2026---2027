package es.codelearnacademy.filelab.csv;

import es.codelearnacademy.filelab.model.Vehiculo;
import es.codelearnacademy.filelab.repository.AbstractFileRepository;
import es.codelearnacademy.filelab.repository.IVehiculoRepository;
import org.apache.commons.csv.CSVFormat;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class VehiculoCsvRepository extends AbstractFileRepository<Vehiculo, String> implements IVehiculoRepository {

    private final CSVFormat inputFormat =  CSVFormat.DEFAULT.builder()
            .setHeader()
            .setSkipHeaderRecord(true)
            .get();
    private final CSVFormat outputFormat = CSVFormat.DEFAULT.builder()
            .setHeader("matricula", "marca", "modelo", "anio")
            .get();

    public VehiculoCsvRepository(Path path) {
        super(path);
    }

    @Override
    protected String getId(Vehiculo vehiculo) {
        return vehiculo.matricula();
    }

    @Override
    protected List<Vehiculo> readAll() {
        throw new UnsupportedOperationException("Función no implementada");
    }

    @Override
    protected void writeAll(List<Vehiculo> vehiculos){
        throw new UnsupportedOperationException("Función no implementada");
    }
}
