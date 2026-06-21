package server;

import java.util.concurrent.ConcurrentLinkedQueue;

import server.persistencia.*;

import shared.turno.Turno;

public class LlamaMappersServer {
    public static void persistir(String modo, ConcurrentLinkedQueue<Turno> turnos, String cosaAPersistir) {
        // Si hay que persistir listas de turnos
        if (cosaAPersistir.startsWith("turnos")) {
            String filePath = cosaAPersistir + "Server.";
            if (modo.equals("txt")) {
                filePath += "txt";
                ListaTurnosTXTMapper.getInstance(filePath).save(turnos, filePath);
            } else if (modo.equals("xml")) {
                filePath += "xml";
                ListaTurnosXMLMapper.getInstance(filePath).save(turnos, filePath);
            } else if (modo.equals("json")) {
                filePath += "json";
                ListaTurnosJSONMapper.getInstance(filePath).save(turnos, filePath);
            } else {
                throw new IllegalArgumentException("Modo de persistencia no soportado: " + modo);
            }
        }
    }

}
