package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import admin.AdminComunicaServerP;

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
    private ManejaMonitor nodoMonitor;
    private ManejaAdmin nodoAdmin;

    public ControllerServer(Server server) {
        this.server = server;
        this.gestorID = null;
        observadoresServers = new CopyOnWriteArrayList<>();
        observadoresPuestos = new CopyOnWriteArrayList<>();
        nodoMonitor = null;
        nodoAdmin = null;
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
                    avisarAdmin("Nodo conectado: Totem con ID " + id, AdminComunicaServerP.EVENTO_PRINCIPAL);
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
                        nodoPuesto = devuelvePrimerManejador();
                        nodoPuesto.setId(id);
                        nodoPuesto.setSocketSimple(socket);
                        nodoPuesto.enviaCantidadEnEspera(server.getEnEspera().getCantidadTurnos());
                        new Thread(nodoPuesto).start();
                        avisarAdmin("Nodo conectado: Puesto con ID " + id, AdminComunicaServerP.EVENTO_PRINCIPAL);
                    }
                    break;


                case ComunicaServer.MONITOR:
                    nodoMonitor = new ManejaMonitor(this, "unico"); //podria ser observer, pero el requerimiento es que haya 1 solo.
                    nodoMonitor.setSocket(socket);
                    //new Thread(nodoMonitor).start();
                    avisarAdmin("Nodo conectado: Monitor.", AdminComunicaServerP.EVENTO_PRINCIPAL);
                    break;


                case ComunicaServer.ADMIN:
                    nodoAdmin = new ManejaAdmin(this, "unico");
                    nodoAdmin.setSocket(socket);
                    if (server.esPrincipal())
                        new Thread(nodoAdmin).start();
                    else
                        avisarAdmin("Sincronizado y escuchando.", AdminComunicaServerP.EVENTO_RESPALDO);
                    break;

                    
                case Server.SERVER:
                    System.out.println("LLEGUE HASTA ACA\n");
                    ManejaServerRespaldo nodoServer = new ManejaServerRespaldo(this, "unico");
                    nodoServer.setSocket(socket);
                    serverObservaControlador(nodoServer);
                    System.out.println("NO MORI??????\n");
                    sincronizacionDeEstado(nodoServer);
                    new Thread(nodoServer).start();
                    System.out.println("THREAD INICIADO CORRECTAMENTE\n");
                    avisarAdmin("Nodo conectado: Server de Respaldo.", AdminComunicaServerP.EVENTO_PRINCIPAL);
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
        // TODO : leer (persistir) GESTORID
        gestorID = new GestorID(0, 0, 0, this);
        server.setGestorID(gestorID);
        if (server.esRespaldo()) { //Si no es principal, nunca abre conexion de ServerSocket (Solo la del admin)
            IManejaServidores nodoServer = new ManejaServerPrincipal(this, "unico");
            nodoServer.setSocket(server.getSocketEntreServers());
            new Thread(nodoServer).start(); //Recordar que si el Server es respaldo tiene el socket para comunicarse con el serverprincipal como atributo de state.
        }
    }

    // Transfiere todo lo que necesita el nuevo servidor cuando recien se conecta
    public void sincronizacionDeEstado(IManejaServidores nodoServer){
        ListaTurnos enEspera, enAtencion, abandonados, atendidos;
        enEspera = server.getEnEspera();
        enAtencion = server.getEnAtencion();
        abandonados = server.getAbandonados();
        atendidos = server.getAtendidos();
        nodoServer.comunicaGestor(gestorID);
        nodoServer.comunicaListaTurnos(enEspera, IManejaServidores.TURNO_ESPERA);
        nodoServer.comunicaListaTurnos(enAtencion, IManejaServidores.TURNO_ATENCION);
        nodoServer.comunicaListaTurnos(abandonados, IManejaServidores.TURNO_ABANDONADO);
        nodoServer.comunicaListaTurnos(atendidos, IManejaServidores.TURNO_ATENDIDO);
    }

    @Override
    public void persisteYEnvia(GestorID gestorID) { // Esto no bloquea hilos de manejadores, porque se ejecuta desde el hilo Server que esta aceptando terminales.
        // TODO : escribir (persistir) GESTORID
        ManejaServerRespaldo nodoServer;
        for (IControllerObserver obs : observadoresServers){
            nodoServer = (ManejaServerRespaldo) obs;
            nodoServer.comunicaGestor(gestorID);
        }
    }

    //@Override
    public void recibeYPersisteGestor(String totem, String puesto, String monitor){
        System.out.println("Recibi el mensaje de admin para setear gestorID");
        int cantTotem = Integer.valueOf(totem);
        int cantPuesto = Integer.valueOf(puesto);
        int cantMonitor = Integer.valueOf(monitor);
        System.out.println("Preparandome para Setear cantTotem");
        this.gestorID.setContadorTotem(cantTotem);
        System.out.println("CantTotem SETEADO");
        this.gestorID.setContadorPuesto(cantPuesto);
        this.gestorID.setContadorMonitor(cantMonitor);
        System.out.println("Todo seteado");
        // TODO : ESCRIBE LOCALMENTE GESTORID...
    }

    //@Override
    public boolean recibeYPersisteTurno(String dni, String estado){
        boolean validacion = false;
        Cliente cliente;
        try {
            cliente = new Cliente(dni);
            Turno turno = new Turno();
            turno.setCliente(cliente);
            if (estado.equals(IManejaServidores.TURNO_ESPERA)){
                validacion = server.getEnEspera().contieneA(turno) | server.getEnAtencion().contieneA(turno);
                if (!validacion){
                    server.getEnEspera().agregaTurno(turno);
                    System.out.println(server.getEnEspera());
                    System.out.println(server.getEnAtencion());
                    notificaPuestos();
                    notificaTurnosAServers(turno);
                }
            }
        } catch (ClienteDniVacioException | ClienteDniInvalidoException e) {}
        // TODO : Escribir turno en el archivo que corresponda.
        return validacion;
    }

    public void recibeTurnoEnRespaldo(Turno turno){
        Cliente cliente;
        if (turno.estaEnAtencion()){
            if (server.getEnEspera().contieneA(turno))
                server.getEnEspera().eliminaTurno(turno);
            server.getEnAtencion().agregaTurno(turno);
        }
        else if (turno.estaEnEspera()){
            server.getEnEspera().agregaTurno(turno);
        }
        else if (turno.estaAbandonado()){
            if (server.getEnAtencion().contieneA(turno))
                server.getEnAtencion().eliminaTurno(turno);
            server.getAbandonados().agregaTurno(turno);
        }
        else if (turno.estaAtendido()){
            if (server.getEnAtencion().contieneA(turno))
                server.getEnAtencion().eliminaTurno(turno);
            server.getAtendidos().agregaTurno(turno);
        }
        // TODO : Escribir turno en el archivo que corresponda.
    }

    public Turno llamaSiguienteTurno(String id) { //TODO: Tiene la id porque se la tiene que pasar al monitor...
        Turno turno = server.getEnEspera().llamaTurno();
        if (turno != null) {
            turno.atender(id);
            notificaPuestos();
            notificaTurnosAServers(turno);
            if (nodoMonitor != null){
                nodoMonitor.llamaMonitor(turno);
            }
            // TODO : Persistir cambios en ambas listas.
            Iterator<Turno> enAtencion = server.getEnAtencion().devuelveIterator();
            while (enAtencion.hasNext()){
                Turno turnoEnAtencionEnPuestoActual = enAtencion.next();
                if (turnoEnAtencionEnPuestoActual.getIdPuesto().equals(id)){

                    if (turnoEnAtencionEnPuestoActual.estaEnAtencion()){

                        turnoEnAtencionEnPuestoActual.atender(id); // pasa a estado Atendido
                        server.getEnAtencion().eliminaTurno(turnoEnAtencionEnPuestoActual);
                        server.getAtendidos().agregaTurno(turnoEnAtencionEnPuestoActual);
                        notificaTurnosAServers(turnoEnAtencionEnPuestoActual);
                        // TODO : Persistir turnos Atendidos por x puesto a x hora.
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
                if (nodoMonitor != null & turnoEnAtencionEnPuestoActual.getCantLlamados() < 3){
                    nodoMonitor.renotificaMonitor(turnoEnAtencionEnPuestoActual);
                    turnoEnAtencionEnPuestoActual.llamar();
                    notificaTurnosAServers(turnoEnAtencionEnPuestoActual);
                }
                else if (nodoMonitor != null){
                    System.out.println("ABANDONADOOOOOOOO\n\n");
                    server.getEnAtencion().eliminaTurno(turnoEnAtencionEnPuestoActual);
                    turnoEnAtencionEnPuestoActual.llamar();
                    server.getAbandonados().agregaTurno(turnoEnAtencionEnPuestoActual);
                    notificaTurnosAServers(turnoEnAtencionEnPuestoActual);
                }
                valido = true;
            }
        }
        return valido;
    }

    public void avisarAdmin (String msg, String tipoEvento){
        if (nodoAdmin != null){
            switch(tipoEvento){
                case AdminComunicaServerP.EVENTO_PRINCIPAL:
                    nodoAdmin.logEventoPrincipal(msg);
                    break;
                case AdminComunicaServerP.BIEN_PRINCIPAL:
                    nodoAdmin.logBienPrincipal(msg);
                    break;
                case AdminComunicaServerP.MAL_PRINCIPAL:
                    nodoAdmin.logMalPrincipal(msg);
                    break;
                case AdminComunicaServerP.EVENTO_RESPALDO:
                    nodoAdmin.logEventoRespaldo(msg);
                    break;
            }
        }
        
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

    public void notificaTurnosAServers(Turno turno){
        Iterator<IControllerObserver> nodosDeServer = observadoresServers.iterator();
        String estadoTurno;
        if (turno.estaEnEspera()){
            estadoTurno = IManejaServidores.TURNO_ESPERA;
        }
        else if (turno.estaEnAtencion()){
            estadoTurno = IManejaServidores.TURNO_ATENCION;
        }
        else if (turno.estaAbandonado()){
            estadoTurno =  IManejaServidores.TURNO_ABANDONADO;
        }
        else{ //(turno.estaAtendido())
            estadoTurno = IManejaServidores.TURNO_ATENDIDO;
        }
        while (nodosDeServer.hasNext()){
            ManejaServerRespaldo serverRespaldo = (ManejaServerRespaldo) nodosDeServer.next();
            serverRespaldo.comunicaTurno(turno, estadoTurno);
            // TODO : Escribir turno en el archivo que corresponda.
        }
    }

    public ManejaPuesto devuelvePrimerManejador(){
        ManejaPuesto encontrado = null;
        String id = "algo";
        Iterator<IControllerObserver> nodosPuesto = observadoresPuestos.iterator();
        while (nodosPuesto.hasNext() & !id.equals("-1")){
            encontrado = (ManejaPuesto) nodosPuesto.next();
            id = encontrado.getId();
        }
        return encontrado;
    }

    @Override
    public void desconectaAdmin() {
        if (nodoAdmin != null) {
            Socket s = nodoAdmin.getSocket();
            if (s != null && !s.isClosed()) {
                try {
                    s.close();
                } catch (IOException e) {
                    // ignorable, ya estaba en proceso de cierre
                }
            }
        }
    }

    
}