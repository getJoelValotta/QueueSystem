package server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import server.id.GestorID;


public class Server implements Runnable{
    public static final String SERVER = "#SERVER#"; // Se utiliza para cuando se conecte un server de respaldo al sv principal
    private ListaTurnos enEspera, enAtencion, abandonados, atendidos;
    private ServerState estado;
    private ServerSocket socketServer;
    private GestorID gestorID;
    private String IP = "localhost";
    private int puerto1 = 1337, puerto2 = 1338;
    private SocketListener escuchadorDeSockets;

    public Server(){
        socketServer = null;
        enEspera = null;
        enAtencion = null;
        abandonados = null;
        atendidos = null;
        this.estado = null;
        gestorID = null;
    }

    public void setServerState(ServerState estado){
        this.estado = estado;
        estado.setServer(this);
    }

    public void switchServer(){ //El estado principal se desconecta (cierra sus sockets por failover o switchback), cambia su estado a respaldo y el que estaba de respaldo
        estado.switchServer(); //deja de recibir hearthbeats por lo que cambia su estado a principal e instancia su serverSocket como corresponde?
    }

    public boolean esPrincipal(){
        return estado.esPrincipal();
    }

    public boolean esRespaldo(){
        return estado.esRespaldo();
    }

    public Socket getSocketEntreServers(){
        return estado.getSocketEntreServers();
    }

    public void abreConexion(){ //Este metodo es el primero que se ejecuta desde el controlador de servers. La idea es que defina que tipo de server es, si es principal 
        try {                  // es porque no hay otro server en el DNS configurado (localhost), pero si lo hay entonces es de respaldo, donde guarda el socket de conexion para luego.
            Socket socketEntreServers = conectarseExistente();
            if (socketEntreServers == null){
                this.socketServer = new ServerSocket(puerto1);
                this.setEstado(new ServerPrincipal());
                new Thread(this).start(); // Si es principal, acepta conexiones a nodos.
            }
            else{
                this.socketServer = new ServerSocket(puerto2);
                this.setEstado(new ServerRespaldo(this, socketEntreServers));
                new Thread(this).start();
                // Si es de respaldo, solo acepta conexion a ADMIN (idealmente)
            }
        } catch (IOException e) {
            //Error interno de TCP
            e.printStackTrace();
        }
    }
    
    public Socket conectarseExistente(){ //devuelve el socket a donde se conecto si logra conectarse a un server prendido, null en caso contrario
        String DNS = IP; // siempre es localhost hasta que configuremos una red....
        DataOutputStream outConexionInicial;
        Socket socketEntreServers;
        try {
            socketEntreServers = new Socket(DNS,puerto1);
            outConexionInicial = new DataOutputStream(socketEntreServers.getOutputStream()); // Le notifica al otro servidor que se conecto otro server a él
            outConexionInicial.writeUTF(SERVER);
            return socketEntreServers;
        } catch (java.net.ConnectException e) {
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public void run() {
        Socket socket;
        String conectado, solicitud;
        try {
            if (this.esPrincipal()){
                while (!socketServer.isClosed()){
                    socket = socketServer.accept();
                    escuchadorDeSockets.atiendeSockets(socket);
                }
            }
            else{
                socket = socketServer.accept();
                escuchadorDeSockets.atiendeSockets(socket);
            }
        } catch (IOException e) {
            // Error de protocolo de TCP (extremadamente improbable) o se cayo el server.
            e.printStackTrace();
        }
    }
    
    public void inicializaListas(){
        this.enEspera = new ListaTurnos();
        this.enAtencion = new ListaTurnos();
        this.abandonados = new ListaTurnos();
        this.atendidos = new ListaTurnos();
    }

    public ListaTurnos getEnEspera() {
        return enEspera;
    }

    public ListaTurnos getEnAtencion() {
        return enAtencion;
    }

    public ListaTurnos getAbandonados() {
        return abandonados;
    }

    public ListaTurnos getAtendidos() {
        return atendidos;
    }

    public ServerState getEstado() {
        return estado;
    }

    public void setEstado(ServerState estado) {
        this.estado = estado;
    }

    public String getIP() {
        return IP;
    }

    public int getPuerto1() {
        return puerto1;
    }

    public int getPuerto2() {
        return puerto2;
    }

    public void setGestorID(GestorID gestorID) {
        this.gestorID = gestorID;
    }

    public GestorID getGestorID() {
        return gestorID;
    }

    public ServerSocket getServerSocket(){
        return socketServer;
    }

    public void setSocketListener(SocketListener controller){
        this.escuchadorDeSockets = controller;
    }



}
