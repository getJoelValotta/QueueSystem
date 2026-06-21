package server.persistencia;

import java.util.concurrent.ConcurrentLinkedQueue;

import shared.turno.*;
import shared.persistencia.*;

//Jackson
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;;

public class TurnosEsperaXMLMapper extends AbstractFileMapper<ConcurrentLinkedQueue<Turno>> {
    private static TurnosEsperaXMLMapper instance;
    String filePath = "turnosEsperaServer.xml";

    private TurnosEsperaXMLMapper(String filePath) {
        super(filePath);
    }

    public static TurnosEsperaXMLMapper getInstance(String filePath) {
        if (instance == null) {
            instance = new TurnosEsperaXMLMapper(filePath);
        }
        return instance;
    }

    @Override
    protected String serialize(ConcurrentLinkedQueue<Turno> turnos) {
        // Pasamos a lista de strings formateados de turnos
        ConcurrentLinkedQueue<String> turnosStr = new ConcurrentLinkedQueue<>();
        for (Turno turno : turnos) {
            turnosStr.add(TurnoToStringUtil.getStringDelTurno(turno));
        }
        // Convertimos la lista de strings a XML
        XmlMapper xmlMapper = new XmlMapper();
        try {
            return xmlMapper.writeValueAsString(turnosStr);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    protected ConcurrentLinkedQueue<Turno> deserialize(String data) {
        ConcurrentLinkedQueue<Turno> turnos = new ConcurrentLinkedQueue<>();
        XmlMapper xmlMapper = new XmlMapper();
        try {
            ConcurrentLinkedQueue<String> turnosStr = xmlMapper.readValue(data,
                    new TypeReference<ConcurrentLinkedQueue<String>>() {
                    });
            for (String turnoStr : turnosStr) {
                turnos.add(TurnoToStringUtil.getTurnoFromString(turnoStr));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return turnos;
    }
}
