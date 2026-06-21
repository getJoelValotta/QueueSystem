package server;

import java.util.concurrent.ConcurrentLinkedQueue;

import server.persistencia.*;

import shared.turno.Turno;

public class LlamaMappersServer {
    public static void persistir(String modo, ConcurrentLinkedQueue<Turno> turnos) {
        if (modo.equals("txt")) {
            TurnosEsperaTXTMapper.getInstance("puestos.txt").save(turnos);
            // TODO: AGREGAR LOS DEMAS
        } else if (modo.equals("xml")) {
            TurnosEsperaXMLMapper.getInstance("puestos.xml").save(turnos);
        } else if (modo.equals("json")) {
            TurnosEsperaJSONMapper.getInstance("puestos.json").save(turnos);
        } else {
            throw new IllegalArgumentException("Modo de persistencia no soportado: " + modo);
        }
    }

}
