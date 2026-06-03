package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import server.id.GestorID;
import server.id.GestorIDListener;
import server.manejadores.IManejaServidores;
import server.manejadores.ManejaAdmin;
import server.manejadores.ManejaMonitor;
import server.manejadores.ManejaPuesto;
import server.manejadores.ManejaServerPrincipal;
import server.manejadores.ManejaServerRespaldo;
import server.manejadores.ManejaTotem;
import server.manejadores.ManejadorEventListener;
import shared.cliente.Cliente;
import shared.cliente.ClienteDniInvalidoException;
import shared.cliente.ClienteDniVacioException;
import shared.conexion_server.ComunicaServer;
import shared.turno.Turno;

public class ControllerServer implements GestorIDListener, SocketListener, ManejadorEventListener{ //SocketListener es para escuchar al Server que delega atencion, ManejadorEventListener dispara eventos de interes desde los manejadores para ser atendido por el controller
    private Server server;
    private IManejaServidores nodoServer; // Gestor id dispara un metodo a traves de una interfaz al controller que hace que le envie la informacion necesaria a nodoRespaldo.
    private ManejaAdmin nodoAdmin;
    private ManejaTotem nodoTotem;
    private ManejaPuesto nodoPuesto;
    private ManejaMonitor nodoMonitor;
    private GestorID gestorID; // gestor ID es parte del server, pero necesita persistirse y ademas pasarle info al server de Respaldo, justo como la informacion de la lista de espera del server


    public ControllerServer(Server server, ManejaAdmin nodoAdmin,
            ManejaTotem nodosTotem, ManejaPuesto nodosPuesto, ManejaMonitor nodoMonitor) {
        this.server = server;
        this.nodoAdmin = nodoAdmin;
        this.nodoTotem = nodosTotem;
        this.nodoPuesto = nodosPuesto;
        this.nodoMonitor = nodoMonitor;
        this.nodoServer = null;
        this.gestorID = null;
    }

    @Override
    public void atiendeSockets(Socket socket) {
        String conectado, solicitud;
        DataOutputStream out;
        DataInputStream in;
        try {
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());
            conectado = in.readUTF();
            switch (conectado) { 
                case ComunicaServer.TOTEM:
                    solicitud = in.readUTF(); //Solo los que solicitan ID piden solicitud
                    if (solicitud.equals(ComunicaServer.ID)) {  
                        out.writeUTF(gestorID.generarIdTotem()); // ademas de generar la id unica, le avisa al controlador que el totem cambio y debe persistirse.
                    }
                    nodoTotem.setSocket(socket);
                    new Thread(nodoTotem).start();
                    break;
                case ComunicaServer.PUESTO:
                    solicitud = in.readUTF();
                    if (solicitud.equals(ComunicaServer.ID)) {
                        out.writeUTF(gestorID.generarIdPuesto());
                    }
                    nodoPuesto.setSocket(socket);
                    new Thread(nodoPuesto).start();
                    break;
                case ComunicaServer.MONITOR:
                    nodoMonitor.setSocket(socket);
                    new Thread(nodoMonitor).start();
                    break;
                case ComunicaServer.ADMIN:
                    nodoAdmin.setSocket(socket);
                    new Thread(nodoAdmin).start();
                    break;
                case Server.SERVER:
                    nodoServer = new ManejaServerRespaldo();
                    nodoServer.setSocket(socket);
                    new Thread(nodoServer).start();
                    sincronizacionDeEstado();
                    break;
            }
        } catch (IOException e) {  
            e.printStackTrace();
        }
    }

    public void iniciaServer() {
        // TODO : PERSISTIR LISTAS ESPERA Y ATENCION y volcar al metodo inicializaListas
        server.inicializaListas();
        server.abreConexion();       
        if (server.esRespaldo()) { //Si no es principal, nunca abre conexion de ServerSocket
            nodoServer = new ManejaServerPrincipal(this);
            nodoServer.setSocket(server.getSocketEntreServers());
            new Thread(nodoServer).start(); //Recordar que si el Server es respaldo tiene el socket para comunicarse con el serverprincipal como atributo de state.
        }
        gestorID = new GestorID(0, 0, 0, this);
        // TODO : leer (persistir) GESTORID
        server.setGestorID(gestorID);
    }

    // Transfiere todo lo que necesita el nuevo servidor cuando recien se conecta
    public void sincronizacionDeEstado(){
        ListaTurnos enEspera, enAtencion;
        enEspera = server.getEnEspera();
        enAtencion = server.getEnAtencion();
        nodoServer.comunicaGestor(gestorID);
        nodoServer.comunicaListaTurnos(enEspera, IManejaServidores.TURNO_ESPERA);
        nodoServer.comunicaListaTurnos(enAtencion, IManejaServidores.TURNO_ATENCION);
    }

    @Override
    public void persisteYEnvia(GestorID gestorID) { // Esto no bloquea hilos de manejadores, porque se ejecuta desde el hilo Server que esta aceptando terminales.
        // TODO : escribir (persistir) GESTORID
        nodoServer.comunicaGestor(gestorID);
    }

    public void persisteYEnvia(Turno turno){
        // 
    }

    //@Override
    public void recibeYPersiste(String totem, String puesto, String monitor){
        int cantTotem = Integer.valueOf(totem);
        int cantPuesto = Integer.valueOf(puesto);
        int cantMonitor = Integer.valueOf(monitor);
        this.gestorID.setContadorTotem(cantTotem);
        this.gestorID.setContadorPuesto(cantPuesto);
        this.gestorID.setContadorMonitor(cantMonitor);
        // ESCRIBE LOCALMENTE GESTORID...
    }

    //@Override
    public void recibeYPersisteTurno(String dni, String estado){
        Cliente cliente;
        try {
            cliente = new Cliente(dni);
            Turno turno = new Turno();
            turno.setCliente(cliente);
            if (estado.equals(IManejaServidores.TURNO_ATENCION)){
                turno.llamar();
                server.getEnAtencion().agregaTurno(turno);
            }
            else{
                server.getEnEspera().agregaTurno(turno);
            }
        } catch (ClienteDniVacioException | ClienteDniInvalidoException e) {}
        // TODO : Escribir turno en el archivo que corresponda.
    }


}
