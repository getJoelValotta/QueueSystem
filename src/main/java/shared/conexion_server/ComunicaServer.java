package shared.conexion_server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public abstract class ComunicaServer {
    protected Socket socket;
    protected ConexionListener escuchadorDeNodoFisico;
    public static final String PUESTO = "#PUESTO#", TOTEM = "#TOTEM#", MONITOR = "#MONITOR#", ADMIN = "#ADMIN#", PUESTO_COLA = "#COLA#", PUESTO_LLAMADOS = "#LLAMADOS#", TOTEM_INIT = "#TOTEM_INIT#", TOTEM_END = "#TOTEM_END#",
            ID = "#IDX#", CLAVE = "#CLAVE#";
    protected DataOutputStream out;
    protected DataInputStream in;
    protected Object mutex = new Object(); // Auxiliar para el manejo de zonas criticas de los in/out de los sockets.
    protected String IP;
    protected int puerto;
    protected String claveEncriptacion = null;

    public ComunicaServer(){
        socket = null;
    }

    public void setEscuchadorDeNodoFisico(ConexionListener escuchadorDeNodoFisico) { // El concepto "escuchador" aca se usa como un actionlistener, ya que al que al que le
        this.escuchadorDeNodoFisico = escuchadorDeNodoFisico; // Similar a un observer, aunque de instancia unica y mas particular.
    }

    public void conectaServidorPrimeraVez(String IP, int puerto, String nodo, String claveEncriptacion){
        this.IP = IP;
        this.puerto = puerto;
        //this.claveEncriptacion = claveEncriptacion;
        conectaServidor(IP, puerto, nodo);
    }

    // Asumimos que la infra de la conexion de las ips es con ayuda de un DNS con un
    // TTL tendiente a 0, por ello nosotros usamos localhost para las pruebas de
    // conexion.
    public void conectaServidor(String IP, int puerto, String nodo) {
        try {
            if (socket != null){
                socket.close();
                Thread.sleep(1500);
            }
            this.socket = new Socket(IP, puerto);
            this.out = new DataOutputStream(socket.getOutputStream());
            this.in = new DataInputStream(socket.getInputStream());
            System.out.println("ACABO DE INSTANCIAR UN SOCKET");
            out.writeUTF(nodo);
            escuchadorDeNodoFisico.conexionExitosa();
        } catch (UnknownHostException e) { // Excepcion que discierne si el formato de la IP es invalido
            System.out.println("IP ERRONEA ");
            escuchadorDeNodoFisico.conexionErronea("Formato de IP Invalido");
            e.printStackTrace();
        } catch (java.net.ConnectException e) { // Excepcion que discierne si el puerto es incorrecto o el host esta
                                                // indispuesto.
            System.out.println("IP QUE NO EXISTE ");
            escuchadorDeNodoFisico.conexionErronea("Puerto incorrecto o host indispuesto");
            e.printStackTrace();
        } catch (IOException e) { // Excepcion que discierne si hubo un error en el handshake del protocolo TCP
                                  // (SYN, SYNACK, ACK).
            escuchadorDeNodoFisico.conexionErronea("Error de protocolo de conexion");
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String solicitaID() {
        String nuevaID = null;
        try {
            synchronized (mutex) {
                out.writeUTF(ID);
                nuevaID = in.readUTF();
            }
        } catch (IOException e) {
            // TODO Informar al ADMIN (Server-Side) y manejar retry.
            e.printStackTrace();
        }
        return nuevaID;
    }

    public void informaID(String id) {
        try {
            synchronized (mutex){
                out.writeUTF(id);
            }
        } catch (IOException e) {
            // TODO Informar al ADMIN (Server-Side) y manejar retry.
            e.printStackTrace();
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public ConexionListener getEscuchadorDeNodoFisico() {
        return escuchadorDeNodoFisico;
    }

}
