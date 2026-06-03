package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import shared.conexion_server.ComunicaServer;

public class Server implements Runnable{
    private static final String SERVER = "#SERVER#"; // Se utiliza para cuando se conecte un server de respaldo al sv principal
    private ListaTurnos enEspera, enAtencion;
    private ServerState estado;
    private ServerSocket socketServer;
    private String IP = "localhost";
    private int puerto1 = 1337, puerto2 = 1338;
    private DataOutputStream out;
    private DataInputStream in;

    public Server(){
        socketServer = null;
        enEspera = null;
        enAtencion = null;
        this.estado = null;
    }

    public void setServerState(ServerState estado){
        this.estado = estado;
        estado.setServer(this);
    }

    public void hearthbeat(){ //La logica del lado principal envia y del lado respaldo recibe
        estado.hearthbeat();
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

    public void abreConexion(){ //Este metodo es el primero que se ejecuta desde el controlador de servers. La idea es que defina que tipo de server es, si es principal 
        try {                  // es porque no hay otro server en el DNS configurado (localhost), pero si lo hay entonces es de respaldo, donde guarda el socket de conexion para luego.
            Socket socketEntreServers = conectarseExistente();
            if (socketEntreServers == null){
                this.socketServer = new ServerSocket(puerto1);
                this.setEstado(new ServerPrincipal());
            }
            else{
                this.setEstado(new ServerRespaldo(this, socketEntreServers));
            }
            new Thread(this).start();
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
            out.writeUTF(SERVER);
            return socketEntreServers;
        } catch (java.net.ConnectException e) {
            try {
                socketEntreServers = new Socket(DNS,puerto2);
                outConexionInicial = new DataOutputStream(socketEntreServers.getOutputStream());
                out.writeUTF(SERVER);
                return socketEntreServers;
            } catch (java.net.ConnectException e1) {
                e1.printStackTrace();
                return null;
            } catch (IOException e1) {
                e1.printStackTrace();
                return null;
            }
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
            while (!socketServer.isClosed()){
                socket = socketServer.accept();
                this.out = new DataOutputStream(socket.getOutputStream());
                this.in = new DataInputStream(socket.getInputStream());
                conectado = in.readUTF();
                solicitud = in.readUTF();
                switch (conectado){
                    case ComunicaServer.TOTEM:
                        if (solicitud.equals(ComunicaServer.ID)){

                        }
                        break;
                    case ComunicaServer.PUESTO:
                        if (solicitud.equals(ComunicaServer.ID)){
                            
                        }
                        break;
                    case ComunicaServer.MONITOR:

                        break;
                    case ComunicaServer.ADMIN:

                        break;
                    case SERVER:
                        
                        break;
                }
            }
        } catch (IOException e) {
            // Error de protocolo de TCP (extremadamente improbable) o se cayo el server.
            e.printStackTrace();
        }
    }
    
    public ListaTurnos getEnEspera() {
        return enEspera;
    }

    public ListaTurnos getEnAtencion() {
        return enAtencion;
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

}
