package puesto.persistencia;

import puesto.Puesto;
import shared.turno.Turno;

public class PuestoXMLMapper extends AbstractPuestoMapper {
    private static PuestoXMLMapper instance;
    String filePath = "puestos.xml";

    private PuestoXMLMapper(String filePath) {
        super(filePath);
    }

    public static PuestoXMLMapper getInstance(String filePath) {
        if (instance == null) {
            instance = new PuestoXMLMapper(filePath);
        }
        return instance;
    }

    @Override
    protected String serialize(Puesto puesto) {
        return "<puesto><id>" + puesto.getId() + "</id><turno>" + getStringDelTurno(puesto.getTurno())
                + "</turno></puesto>";
    }

    @Override
    protected Puesto deserialize(String data) {
        String idStr = data.replaceAll(".*<id>(.*)</id>.*", "$1");
        String turnoData = data.replaceAll(".*<turno>(.*)</turno>.*", "$1");

        int id = Integer.parseInt(idStr);
        Turno turno = getTurnoFromString(turnoData);
        return new Puesto(String.valueOf(id), turno);
    }
}
