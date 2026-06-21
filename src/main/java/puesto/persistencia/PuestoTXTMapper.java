package puesto.persistencia;

import shared.turno.Turno;
import puesto.Puesto;

public class PuestoTXTMapper extends AbstractPuestoMapper {
    private static PuestoTXTMapper instance;
    String filePath = "puestos.txt";

    private PuestoTXTMapper(String filePath) {
        super(filePath);
    }

    public static PuestoTXTMapper getInstance(String filePath) {
        if (instance == null) {
            instance = new PuestoTXTMapper(filePath);
        }
        return instance;
    }

    @Override
    protected String serialize(Puesto puesto) {
        // Pasa los datos de puesto a formato txt
        // (Formato: idPuesto,(estado,cantLlamados,idPuesto,dni))
        return puesto.getId() + "," + getStringDelTurno(puesto.getTurno());
    }

    @Override
    protected Puesto deserialize(String data) {
        String[] parts = data.split(",");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid data format");
        }
        String id = parts[0];
        Turno turno = getTurnoFromString(parts[1]);
        return new Puesto(id, turno);
    }

}
