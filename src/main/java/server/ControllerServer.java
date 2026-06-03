package server;

import server.id.GestorID;
import server.id.GestorIDListener;
import server.manejadores.ManejaAdmin;
import server.manejadores.ManejaMonitor;
import server.manejadores.ManejaPuesto;
import server.manejadores.ManejaServerRespaldo;
import server.manejadores.ManejaTotem;

public class ControllerServer implements GestorIDListener{
    private Server server;
    private ManejaServerRespaldo nodoRespaldo; //Gestor id dispara un metodo a traves de una interfaz al controller que hace que le envie la informacion necesaria a nodoRespaldo.
    private ManejaAdmin nodoAdmin;
    private ManejaTotem nodosTotem;
    private ManejaPuesto nodosPuesto;
    private ManejaMonitor nodoMonitor;
    private GestorID gestorID; //gestor ID es parte del server, pero necesita persistirse y ademas pasarle info al server de Respaldo, justo como la informacion de la lista de espera del server

    public ControllerServer(Server server, ManejaServerRespaldo nodoRespaldo, ManejaAdmin nodoAdmin,
            ManejaTotem nodosTotem, ManejaPuesto nodosPuesto, ManejaMonitor nodoMonitor) {
        this.server = server;
        this.nodoRespaldo = nodoRespaldo;
        this.nodoAdmin = nodoAdmin;
        this.nodosTotem = nodosTotem;
        this.nodosPuesto = nodosPuesto;
        this.nodoMonitor = nodoMonitor;
        this.gestorID = null;
    }

    public void iniciaServer(){
        server.abreConexion();
        gestorID = new GestorID(0, 0, 0, null);
        // TODO : leer (persistir) GESTORID
        server.setGestorID(gestorID);
    }
    @Override
    public void persisteYEnvia(GestorID gestorID) {
        // TODO : escribir (persistir) GESTORID

    }

    
}
