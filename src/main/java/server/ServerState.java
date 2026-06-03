package server;

import java.net.Socket;
public abstract class ServerState {
    protected Server server;

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
   
    public abstract void switchServer();
    //    public abstract void hearthbeat(DataInputStream in, DataOutputStream out);
    //    public abstract void comunicaGestor(GestorID gestorID, DataInputStream in, DataOutputStream out);
    //    public abstract void comunicaTurnoEspera(Turno turno, DataInputStream in, DataOutputStream out); // Esto es para cuando el server principal pase turnos segun van llegando
    //    public abstract void comunicaListaTurnosEspera (ListaTurnos turnos, DataInputStream in, DataOutputStream out); // Esto para cuando el servidor recien se conecta por primera vez a uno de respaldo.
}
