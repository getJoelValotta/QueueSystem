package app;

import server.ControllerServer;
import server.Server;
import server.manejadores.ManejaAdmin;
import server.manejadores.ManejaMonitor;
import server.manejadores.ManejaPuesto;
import server.manejadores.ManejaTotem;

public class ServerApp {
    
    public static void main(String[] args) {
        Server server = new Server();
        ManejaAdmin manejaAdmin = new ManejaAdmin();
        ManejaMonitor manejaMonitor = new ManejaMonitor();
        ManejaPuesto manejaPuesto = new ManejaPuesto();
        ManejaTotem manejaTotem = new ManejaTotem();
        ControllerServer controllerServer = new ControllerServer(server, manejaAdmin, manejaTotem, manejaPuesto, manejaMonitor);
        server.setSocketListener(controllerServer);
        controllerServer.iniciaServer();
    }
}
