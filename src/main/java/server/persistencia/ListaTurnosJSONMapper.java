package server.persistencia;

import java.util.concurrent.ConcurrentLinkedQueue;

import shared.turno.*;
import shared.persistencia.*;

//Jackson
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ListaTurnosJSONMapper extends AbstractFileMapper<ConcurrentLinkedQueue<Turno>> {
    private static ListaTurnosJSONMapper instance;
    String filePath = "turnosEsperaServer.json";

    private ListaTurnosJSONMapper(String filePath) {
        super(filePath);
    }

    public static ListaTurnosJSONMapper getInstance(String filePath) {
        if (instance == null) {
            instance = new ListaTurnosJSONMapper(filePath);
        } else
            filePath = instance.filePath;
        return instance;
    }

    @Override
    protected String serialize(ConcurrentLinkedQueue<Turno> turnos) {
        // Pasamos a lista de strings formateados de turnos
        ConcurrentLinkedQueue<String> turnosStr = new ConcurrentLinkedQueue<>();
        for (Turno turno : turnos) {
            turnosStr.add(TurnoToStringUtil.getStringDelTurno(turno));
        }
        // Convertimos la lista de strings a JSON
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(turnosStr);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    protected ConcurrentLinkedQueue<Turno> deserialize(String data) {
        ConcurrentLinkedQueue<Turno> turnos = new ConcurrentLinkedQueue<>();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ConcurrentLinkedQueue<String> turnosStr = objectMapper.readValue(data,
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
