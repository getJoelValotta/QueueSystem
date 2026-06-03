package server.manejadores;

import server.Server;
import server.id.GestorID;
public class ManejaServerRespaldo extends ManejadorDeNodos{
    private Server server;

    public void setServer(Server server){
        this.server = server;
    }
    
    @Override
    public void comunicacion() {
        server.hearthbeat();
    }

    public void enviaGestor(GestorID gestorID){
        
    }

    public Server getServer() {
        return server;
    }
}
