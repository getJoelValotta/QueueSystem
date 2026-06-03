package server;

import java.net.Socket;
public class ServerRespaldo extends ServerState{
    private Socket socketEntreServers;
    
    public ServerRespaldo(Server server, Socket socketEntreServers) {
        super(server);
        this.socketEntreServers = socketEntreServers;
    }

    
    @Override
    public void switchServer() {
        server.abreConexion();
    }

    @Override
    public Socket getSocketEntreServers() {
        return socketEntreServers;
    }



 /* 
    @Override
    public void hearthbeat(DataInputStream in, DataOutputStream out) {}
    @Override
    public void comunicaGestor(GestorID gestorID, DataInputStream in, DataOutputStream out) {}

    @Override
    public void comunicaTurnoEspera(Turno turno, DataInputStream in, DataOutputStream out) {}

    @Override
    public void comunicaListaTurnosEspera(ListaTurnos turnos, DataInputStream in, DataOutputStream out) {}
*/   
}
