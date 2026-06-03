package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import server.id.GestorID;
import server.id.GestorIDListener;
import server.manejadores.ManejaAdmin;
import server.manejadores.ManejaMonitor;
import server.manejadores.ManejaPuesto;
import server.manejadores.ManejaServerRespaldo;
import server.manejadores.ManejaTotem;
import shared.conexion_server.ComunicaServer;

public class ControllerServer implements GestorIDListener, SocketListener {
    private Server server;
    private ManejaServerRespaldo nodoRespaldo; // Gestor id dispara un metodo a traves de una interfaz al controller que hace que le envie la informacion necesaria a nodoRespaldo.
    private ManejaAdmin nodoAdmin;
    private ManejaTotem nodoTotem;
    private ManejaPuesto nodoPuesto;
    private ManejaMonitor nodoMonitor;
    private GestorID gestorID; // gestor ID es parte del server, pero necesita persistirse y ademas pasarle info al server de Respaldo, justo como la informacion de la lista de espera del server


    public ControllerServer(Server server, ManejaServerRespaldo nodoRespaldo, ManejaAdmin nodoAdmin,
            ManejaTotem nodosTotem, ManejaPuesto nodosPuesto, ManejaMonitor nodoMonitor) {
        this.server = server;
        this.nodoRespaldo = nodoRespaldo;
        this.nodoAdmin = nodoAdmin;
        this.nodoTotem = nodosTotem;
        this.nodoPuesto = nodosPuesto;
        this.nodoMonitor = nodoMonitor;
        this.gestorID = null;
    }

    @Override
    public void atiendeSockets(Socket socket) {
        String conectado, solicitud;
        DataOutputStream out;
        try {
            out = new DataOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(socket.getInputStream());
            conectado = in.readUTF();
            solicitud = in.readUTF();
            switch (conectado) {
                case ComunicaServer.TOTEM:
                    if (solicitud.equals(ComunicaServer.ID)) {  
                        out.writeUTF(gestorID.generarIdTotem()); // ademas de generar la id unica, le avisa al controlador que el totem cambio y debe persistirse.
                    }
                    new Thread(nodoTotem).start();
                    break;
                case ComunicaServer.PUESTO:
                    if (solicitud.equals(ComunicaServer.ID)) {
                        out.writeUTF(gestorID.generarIdPuesto());
                    }
                    new Thread(nodoPuesto).start();
                    break;
                case ComunicaServer.MONITOR:
                    new Thread(nodoMonitor).start();
                    break;
                case ComunicaServer.ADMIN:
                    new Thread(nodoAdmin).start();
                    break;
                case Server.SERVER:
                    new Thread(nodoRespaldo).start();
                    break;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void iniciaServer() {
        nodoRespaldo.setServer(server); // Lo puse primero por posible condicion de carrera (condicion critica.) 
        server.abreConexion();       
        if (server.esRespaldo()) {
            new Thread(nodoRespaldo).start();
        }
        gestorID = new GestorID(0, 0, 0, null);
        // TODO : leer (persistir) GESTORID
        server.setGestorID(gestorID);
    }

    // nota para mi: CREO QUE TENGO QUE PREGUNTAR POR EL ESTADO ya que si estoy en
    // el lado del server principal hay instanciado un manejador, pero en el del
    // respaldo solo tengo un socket
    // cuando deberia tener tambien un manejador
    @Override
    public void persisteYEnvia(GestorID gestorID) { // Esto no bloquea hilos de manejadores, porque se ejecuta desde el hilo Server que esta aceptando terminales.
        // TODO : escribir (persistir) GESTORID
        nodoRespaldo.enviaGestor(gestorID);
    }

}
