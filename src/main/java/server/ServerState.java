package server;

import java.net.Socket;

public abstract class ServerState {
    private Server server;

    public ServerState(Server server) {
        this.server = server;
    }

    public ServerState() {
        server = null;
    }

    public void setServer(Server server){
        this.server = server;
    }

    public boolean esPrincipal(){
        return false;
    }

    public boolean esRespaldo(){
        return false;
    }

    public Server getServer() {
        return server;
    }

    public Socket getSocketEntreServers(){
        return null;
    }

    public abstract void hearthbeat();
    public abstract void switchServer();

}
