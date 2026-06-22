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

    public static ConcurrentLinkedQueue<Turno> cargar(String modo, String cosaACargar, boolean esPrincipal)
            throws RuntimeException {
        if (cosaACargar.startsWith("turnos")) {
            String filePath = cosaACargar;
            if (esPrincipal) {
                filePath += "_ServerPrincipal.";
            } else {
                filePath += "_ServerBackup.";
            }
            if (modo.equals("txt")) {
                filePath += "txt"; // ← solo la extensión
                return ListaTurnosTXTMapper.getInstance(filePath).load();
            } else if (modo.equals("xml")) {
                filePath += "xml"; // ← idem
                return ListaTurnosXMLMapper.getInstance(filePath).load();
            } else if (modo.equals("json")) {
                filePath += "json"; // ← idem
                return ListaTurnosJSONMapper.getInstance(filePath).load();
            } else {
                throw new IllegalArgumentException("Modo de persistencia no soportado: " + modo);
            }
        }
        throw new IllegalArgumentException("Cosa a cargar no soportada: " + cosaACargar);
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
