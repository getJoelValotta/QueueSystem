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
        ControllerServer controllerServer = new ControllerServer(server);
        server.setSocketListener(controllerServer);
        controllerServer.iniciaServer();
    }
}
