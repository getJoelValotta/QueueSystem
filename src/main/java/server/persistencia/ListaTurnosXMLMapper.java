package server.persistencia;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import shared.turno.*;
import shared.persistencia.*;

//Jackson
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;;

public class ListaTurnosXMLMapper extends AbstractFileMapper<ConcurrentLinkedQueue<Turno>> {
    private static ListaTurnosXMLMapper instance;
    String filePath = "turnosEsperaServer.xml";

    private ListaTurnosXMLMapper(String filePath) {
        super(filePath);
    }

    public static ListaTurnosXMLMapper getInstance(String filePath) {
        if (instance == null) {
            instance = new ListaTurnosXMLMapper(filePath);
        }
        return instance;
    }

    @Override
    protected String serialize(ConcurrentLinkedQueue<Turno> turnos) {
        List<String> turnosStr = new ArrayList<>();
        for (Turno turno : turnos) {
            turnosStr.add(TurnoToStringUtil.getStringDelTurno(turno));
        }

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
            List<String> turnosStr = xmlMapper.readValue(data,
                    new TypeReference<List<String>>() {
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
