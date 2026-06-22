package puesto.persistencia;

import puesto.Puesto;
import shared.turno.Turno;
import shared.persistencia.*;

public class PuestoXMLMapper extends AbstractFileMapper<Puesto> {
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
        return "<puesto><id>" + puesto.getId() + "</id><turno>" + TurnoToStringUtil.getStringDelTurno(puesto.getTurno())
                + "</turno></puesto>";
    }

    @Override
    protected Puesto deserialize(String data) {
        String idStr = data.replaceAll(".*<id>(.*)</id>.*", "$1");
        String turnoData = data.replaceAll(".*<turno>(.*)</turno>.*", "$1");
        Turno turno = TurnoToStringUtil.getTurnoFromString(turnoData);
        return new Puesto(idStr.trim(), turno);
    }
}
