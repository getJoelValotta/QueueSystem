package app;

import server.ControllerServer;
import server.Server;

public class ServerApp {
    
    public static void main(String[] args) {
        Server server = new Server();
        ControllerServer controllerServer = new ControllerServer(server);
        server.setSocketListener(controllerServer);
        controllerServer.iniciaServer();
    }
}
