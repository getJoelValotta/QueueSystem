package server;

import java.util.concurrent.ConcurrentLinkedQueue;

import server.persistencia.*;

import shared.turno.Turno;

public class LlamaMappersServer {
    public static void persistir(String modo, ConcurrentLinkedQueue<Turno> turnos, String cosaAPersistir,
            boolean esPrincipal) {
        // Si hay que persistir listas de turnos
        if (cosaAPersistir.startsWith("turnos")) {
            String filePath = cosaAPersistir;
            if (esPrincipal) {
                filePath += "_ServerPrincipal.";
            } else {
                filePath += "_ServerBackup.";
            }
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

    public static void persistirConfig(ServerConfig config, boolean esPrincipal) {
        String filePath = null;
        if (esPrincipal) {
            filePath = "serverConfig.json";
        } else {
            filePath = "serverConfigBackup.json";
        }
        ServerConfigJSONMapper.getInstance(filePath).save(config);
    }

    public static ServerConfig cargarConfig() throws RuntimeException {
        String filePath = "serverConfig.json";
        return ServerConfigJSONMapper.getInstance(filePath).load();
    }

}
