package puesto.persistencia;

import shared.turno.Turno;
import shared.persistencia.*;
import puesto.Puesto;

public class PuestoJSONMapper extends AbstractFileMapper<Puesto> {
    private static PuestoJSONMapper instance;
    String filePath = "puestos.json";

    private PuestoJSONMapper(String filePath) {
        super(filePath);
    }

    public static PuestoJSONMapper getInstance(String filePath) {
        if (instance == null) {
            instance = new PuestoJSONMapper(filePath);
        }
        return instance;
    }

    @Override
    protected String serialize(Puesto puesto) {
        // Pasa los datos de puesto a formato JSON
        // (Formato: {puesto:idPuesto,turno:(estado,cantLlamados,idPuesto,dni)})
        return "{\"puesto\":\"" + puesto.getId() + "\",\"turno\":\""
                + TurnoToStringUtil.getStringDelTurno(puesto.getTurno()) + "\"}";
    }

    @Override
    protected Puesto deserialize(String data) {
        if (!data.startsWith("{") || !data.endsWith("}")) {
            throw new IllegalArgumentException("Invalid JSON format");
        }
        // elimina los curly braces
        String content = data.substring(1, data.length() - 1);
        // divide por comas, parte 0 = puestoID y parte 1 = turno
        String[] parts = content.split(",");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid JSON format");
        }

        // trim para eliminar espacios
        // idpart = "puesto":xxxxxx
        String idPart = parts[0].trim();
        // turnopPart = "turno":(xx,xx,xx,xx)
        String turnoPart = parts[1].trim();

        // El turno es un string con formato (estado,cantLlamados,idPuesto,dni)
        if (!idPart.startsWith("\"puesto\":")) {
            throw new IllegalArgumentException("Invalid JSON format for puesto");
        }
        if (!turnoPart.startsWith("\"turno\":")) {
            throw new IllegalArgumentException("Invalid JSON format for turno");
        }
        String id = idPart.substring("\"puesto\":\"".length(), idPart.length() - 1);
        String turnoData = turnoPart.substring("\"turno\":\"".length(), turnoPart.length() - 1);
        Turno turno = TurnoToStringUtil.getTurnoFromString(turnoData);
        return new Puesto(id.trim(), turno);
    }

}
