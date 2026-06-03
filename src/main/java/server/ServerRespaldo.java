package server;

import java.net.Socket;

public class ServerRespaldo extends ServerState{
    private Socket socketEntreServers;
    
    public ServerRespaldo(Server server, Socket socketEntreServers) {
        super(server);
        this.socketEntreServers = socketEntreServers;
    }

    @Override
    public void hearthbeat() {
    }

    @Override
    public void switchServer() {
    }

    @Override
    public Socket getSocketEntreServers() {
        return socketEntreServers;
    }

}
