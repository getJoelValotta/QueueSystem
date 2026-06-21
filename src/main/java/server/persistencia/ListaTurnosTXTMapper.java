package server.persistencia;

import java.util.concurrent.ConcurrentLinkedQueue;

import shared.turno.*;
import shared.persistencia.*;

public class ListaTurnosTXTMapper extends AbstractFileMapper<ConcurrentLinkedQueue<Turno>> {
    private static ListaTurnosTXTMapper instance;
    String filePath = "turnosEsperaServer.txt";

    private ListaTurnosTXTMapper(String filePath) {
        super(filePath);
    }

    public static ListaTurnosTXTMapper getInstance(String filePath) {
        if (instance == null) {
            instance = new ListaTurnosTXTMapper(filePath);
        } else
            filePath = instance.filePath;
        return instance;
    }

    @Override
    protected String serialize(ConcurrentLinkedQueue<Turno> turnos) {
        StringBuilder sb = new StringBuilder();
        for (Turno turno : turnos) {
            sb.append(TurnoToStringUtil.getStringDelTurno(turno)).append(",");
        }
        return sb.toString();
    }

    @Override
    protected ConcurrentLinkedQueue<Turno> deserialize(String data) {
        ConcurrentLinkedQueue<Turno> turnos = new ConcurrentLinkedQueue<>();
        String[] lines = data.split(",");
        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                turnos.add(TurnoToStringUtil.getTurnoFromString(line.trim()));
            }
        }
        return turnos;
    }
}
