package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

import server.id.GestorID;
import server.id.GestorIDListener;
import server.manejadores.IControllerObserver;
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
import server.manejadores.IManejaServidores;

public class ControllerServer implements GestorIDListener, SocketListener, ManejadorEventListener{ //SocketListener es para escuchar al Server que delega atencion, ManejadorEventListener dispara eventos de interes desde los manejadores para ser atendido por el controller
    private Server server;
    private GestorID gestorID; // gestor ID es parte del server, pero necesita persistirse y ademas pasarle info al server de Respaldo, justo como la informacion de la lista de espera del server
    private CopyOnWriteArrayList<IControllerObserver> observadoresServers;
    private CopyOnWriteArrayList<IControllerObserver> observadoresPuestos;
    private ManejaPuesto nodoPuesto;

    public ControllerServer(Server server) {
        this.server = server;
        this.gestorID = null;
        observadoresServers = new CopyOnWriteArrayList<>();
        observadoresPuestos = new CopyOnWriteArrayList<>();
    }

    @Override
    public void atiendeSockets(Socket socket) {
        String conectado, solicitud;
        DataOutputStream out;
        DataInputStream in;
        String id;
        try {
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());
            conectado = in.readUTF();
            switch (conectado) { 
                case ComunicaServer.TOTEM:
                    solicitud = in.readUTF(); //Solo los que solicitan ID piden solicitud, si solicitud no es lo que tiene el string ID es porque ya tiene una id.
                    if (solicitud.equals(ComunicaServer.ID)) { 
                        id = gestorID.generarIdTotem();
                        out.writeUTF(id); // ademas de generar la id unica, le avisa al controlador que el totem cambio y debe persistirse.
                    }
                    else 
                        id = solicitud;
                    ManejaTotem nodoTotem = new ManejaTotem(this, id);
                    nodoTotem.setSocket(socket);
                    new Thread(nodoTotem).start();
                    break;


                case ComunicaServer.PUESTO:
                    solicitud = in.readUTF();
                    if (solicitud.equals(ComunicaServer.PUESTO_COLA)) {
                        nodoPuesto = new ManejaPuesto(this, "-1");
                        puestoObservaControlador(nodoPuesto);
                        nodoPuesto.setSocket(socket);
                    }
                    else if (solicitud.equals(ComunicaServer.PUESTO_LLAMADOS)){
                        solicitud = in.readUTF();
                        if (solicitud.equals(ComunicaServer.ID)) {
                            id = gestorID.generarIdPuesto();
                            out.writeUTF(id);
                        }
                        else 
                            id = solicitud;
                        nodoPuesto.setId(id);
                        nodoPuesto.setSocketSimple(socket);
                        nodoPuesto.enviaCantidadEnEspera(server.getEnEspera().getCantidadTurnos());
                        new Thread(nodoPuesto).start();
                    }
                    
                    break;


                case ComunicaServer.MONITOR:
                    ManejaMonitor nodoMonitor = new ManejaMonitor(this, "unico");
                    nodoMonitor.setSocket(socket);
                    new Thread(nodoMonitor).start();
                    break;


                case ComunicaServer.ADMIN:
                    ManejaAdmin nodoAdmin = new ManejaAdmin(this, "unico");
                    nodoAdmin.setSocket(socket);
                    new Thread(nodoAdmin).start();
                    break;

                    
                case Server.SERVER:
                    ManejaServerPrincipal nodoServer = new ManejaServerPrincipal(this, "unico");
                    nodoServer.setSocket(socket);
                    new Thread(nodoServer).start();
                    serverObservaControlador(nodoServer);
                    sincronizacionDeEstado(nodoServer);
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
            IManejaServidores nodoServer = new ManejaServerPrincipal(this, "unico");
            nodoServer.setSocket(server.getSocketEntreServers());
            new Thread(nodoServer).start(); //Recordar que si el Server es respaldo tiene el socket para comunicarse con el serverprincipal como atributo de state.
        }
        gestorID = new GestorID(0, 0, 0, this);
        // TODO : leer (persistir) GESTORID
        server.setGestorID(gestorID);
    }

    // Transfiere todo lo que necesita el nuevo servidor cuando recien se conecta
    public void sincronizacionDeEstado(IManejaServidores nodoServer){
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
        ManejaServerPrincipal nodoServer;
        for (IControllerObserver obs : observadoresServers){
            nodoServer = (ManejaServerPrincipal) obs;
            nodoServer.comunicaGestor(gestorID);
        }
    }

    //@Override
    public void recibeYPersisteGestor(String totem, String puesto, String monitor){
        int cantTotem = Integer.valueOf(totem);
        int cantPuesto = Integer.valueOf(puesto);
        int cantMonitor = Integer.valueOf(monitor);
        this.gestorID.setContadorTotem(cantTotem);
        this.gestorID.setContadorPuesto(cantPuesto);
        this.gestorID.setContadorMonitor(cantMonitor);
        // ESCRIBE LOCALMENTE GESTORID...
    }

    //@Override
    public boolean recibeYPersisteTurno(String dni, String estado){
        boolean validacion = false;
        Cliente cliente;
        try {
            cliente = new Cliente(dni);
            Turno turno = new Turno();
            turno.setCliente(cliente);
            if (estado.equals(IManejaServidores.TURNO_ATENCION)){
                turno.atender("1"); // TODO BORRAR
                server.getEnAtencion().agregaTurno(turno);
            }
            else if (estado.equals(IManejaServidores.TURNO_ESPERA)){
                validacion = server.getEnEspera().contieneA(turno) | server.getEnAtencion().contieneA(turno);
                if (!validacion){
                    server.getEnEspera().agregaTurno(turno);
                    System.out.println(server.getEnEspera());
                    System.out.println(server.getEnAtencion());
                    notificaPuestos();
                }
            }
        } catch (ClienteDniVacioException | ClienteDniInvalidoException e) {}
        // TODO : Escribir turno en el archivo que corresponda.
        return validacion;
    }

    public Turno llamaSiguienteTurno(String id) { //TODO: Tiene la id porque se la tiene que pasar al monitor...
        Turno turno = server.getEnEspera().llamaTurno();
        if (turno != null) {
            turno.atender(id);
            System.out.println("======Agregando turno a la Lista de Atencion======");
            notificaPuestos();
            // TODO : Persistir cambios en ambas listas.
            Iterator<Turno> enAtencion = server.getEnAtencion().devuelveIterator();
            while (enAtencion.hasNext()){
                Turno turnoEnAtencionEnPuestoActual = enAtencion.next();
                if (turnoEnAtencionEnPuestoActual.getIdPuesto().equals(id)){
                    if (turnoEnAtencionEnPuestoActual.getCantLlamados() < 4){
                        server.getEnAtencion().eliminaTurno(turnoEnAtencionEnPuestoActual);
                        server.getAtendidos().agregaTurno(turnoEnAtencionEnPuestoActual);
                        // TODO : Persistir turnos Atendidos por x puesto a x hora.
                    }
                    else{
                        server.getEnAtencion().eliminaTurno(turnoEnAtencionEnPuestoActual);
                        server.getAbandonados().agregaTurno(turnoEnAtencionEnPuestoActual);
                    }
                }
            }
            server.getEnAtencion().agregaTurno(turno);
        }
        return turno;
    }

    @Override
    public boolean actualizaTurnoRenotificado(String idPuesto) {
        boolean valido = false;
        Iterator<Turno> enAtencion = server.getEnAtencion().devuelveIterator();
        while (enAtencion.hasNext()){
            Turno turnoEnAtencionEnPuestoActual = enAtencion.next();
            if (turnoEnAtencionEnPuestoActual.getIdPuesto().equals(idPuesto)){
                turnoEnAtencionEnPuestoActual.llamar();
                valido = true;
            }
        }
        return valido;
    }
    
    @Override
    public void cambiaEstadoServer() {
        server.switchServer(); // Nombre no representativo la verdad, porque no cambia de server, porque el server en si es el mismo, solo transiciona de estado y crea un socket server
        
    }

    public void serverObservaControlador(IControllerObserver nodoServerRespaldo){
        observadoresServers.add(nodoServerRespaldo);
    }

    public void puestoObservaControlador(IControllerObserver nodoPuesto){
        observadoresPuestos.add(nodoPuesto);
    }

    public void serverDejaDeObservar(IControllerObserver suscriptor) {
        observadoresServers.remove(suscriptor);
    }

    public void puestoDejaDeObservar(IControllerObserver suscriptor) {
        observadoresPuestos.remove(suscriptor);
    }

    public void notificaPuestos(){
        int cantTurnos = server.getEnEspera().getCantidadTurnos();
        Iterator<IControllerObserver> nodosPuesto = observadoresPuestos.iterator();
        while (nodosPuesto.hasNext()){
            ManejaPuesto puestoActual = (ManejaPuesto) nodosPuesto.next();
            puestoActual.enviaCantidadEnEspera(cantTurnos);
        }
    }

}