package totem;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import shared.conexion_server.ComunicaServer;

public class TotemComunicaServer extends ComunicaServer {
    private TotemEventListener escuchadorDeEventos;
    private Socket socketSimple;
    protected DataOutputStream outSimple;
    protected DataInputStream inSimple;

    public void setEscuchadorDeEventos(TotemEventListener escuchadorDeEventos) {
        this.escuchadorDeEventos = escuchadorDeEventos;
    }
    // El totem envia el DNI al socket de comunicacion con el server que este conectado y si ya estaba en el sistema retorna false.
    public boolean enviarDNI(String dni) {
        boolean validacion = false;
        try {
            outSimple.writeUTF(dni); // TODO : encriptar
            // TODO : Informar al ADMIN (server-side)
            validacion = Boolean.parseBoolean(inSimple.readUTF());
            return validacion;
        } catch (SocketException e) {
            System.out.println("EXCEPTION POR SOCKET");
            return reintentarConexion(dni);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        return validacion;
    }

    public boolean reintentarConexion(String dni){
        conectaServidor(IP, puerto, ComunicaServer.TOTEM);
        informaID(escuchadorDeNodoFisico.getId());
        return enviarDNI(dni);
    }

    @Override
    public void conectaServidorPrimeraVez(String IP, int puerto, String nodo, String claveEncriptacion){
        this.IP = IP;
        this.puerto = puerto;
        escuchadorDeNodoFisico.setClaveEncriptacion(claveEncriptacion);
        try {
            this.socket = new Socket(IP, puerto);
            this.out = new DataOutputStream(socket.getOutputStream());
            this.in = new DataInputStream(socket.getInputStream());
            out.writeUTF(CLAVE);
            out.writeUTF(claveEncriptacion);
            boolean claveValida = Boolean.parseBoolean(in.readUTF());
            if (!claveValida){
                System.out.println("CLAVE DE ENCRIPTACION INVALIDA");
                escuchadorDeNodoFisico.conexionErronea("Clave de encriptacion invalida");
                socket.close();
            }
            else{
                out.writeUTF(nodo);
                out.writeUTF(TOTEM_INIT);
                this.socketSimple = new Socket(IP, puerto);
                this.outSimple = new DataOutputStream(socketSimple.getOutputStream());
                this.inSimple = new DataInputStream(socketSimple.getInputStream());
                outSimple.writeUTF(nodo);
                outSimple.writeUTF(TOTEM_END);
                escuchadorDeNodoFisico.conexionExitosa();
            }
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
        }
    }

    

    @Override
    public void conectaServidor(String IP, int puerto, String nodo) {
        try {
            super.conectaServidor(IP, puerto, nodo);
            out.writeUTF(TOTEM_INIT);
            this.socketSimple = new Socket(IP, puerto);
            this.outSimple = new DataOutputStream(socketSimple.getOutputStream());
            this.inSimple = new DataInputStream(socketSimple.getInputStream());
            outSimple.writeUTF(nodo);
            outSimple.writeUTF(TOTEM_END);
        } catch (java.net.ConnectException e) { // Excepcion que discierne si el puerto es incorrecto o el host esta indispuesto.
            escuchadorDeNodoFisico.conexionErronea("Puerto incorrecto o host indispuesto");
            e.printStackTrace();
        } catch (IOException e) { 
            escuchadorDeNodoFisico.conexionErronea("Error de protocolo de conexion");
            e.printStackTrace();
        }
    }

    @Override
    public void informaID(String id) {
        try {
            synchronized (mutex){
                outSimple.writeUTF(id);
            }
        } catch (IOException e) {
            // TODO Informar al ADMIN (Server-Side) y manejar retry.
            e.printStackTrace();
        }
    }

    @Override
    public String solicitaID() {
        String nuevaID = null;
        try {
            synchronized (mutex) {
                outSimple.writeUTF(ID);
                nuevaID = inSimple.readUTF();
            }
        } catch (IOException e) {
            // TODO Informar al ADMIN (Server-Side) y manejar retry.
            e.printStackTrace();
        }
        return nuevaID;
    }

}
