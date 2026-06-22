package puesto.persistencia;

import shared.turno.Turno;
import shared.persistencia.*;
import puesto.Puesto;

public class PuestoTXTMapper extends AbstractFileMapper<Puesto> {
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
        return puesto.getId() + "," + TurnoToStringUtil.getStringDelTurno(puesto.getTurno());
    }

    @Override
    protected Puesto deserialize(String data) {
        String[] parts = data.split(",");
        if (parts.length != 2) {
            throw new IllegalArgumentException(
                    "Invalid data format, parts length: " + parts.length + ", data: " + data);

        }
        String id = parts[0];
        Turno turno = TurnoToStringUtil.getTurnoFromString(parts[1]);
        return new Puesto(id, turno);
    }

}
