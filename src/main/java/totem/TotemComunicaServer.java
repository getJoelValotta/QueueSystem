package totem;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

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
    public boolean enviarDNI(long dni) {
        boolean validacion = false;
        try {
            outSimple.writeUTF(String.valueOf(dni)); // TODO : encriptar
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

    public boolean reintentarConexion(long dni){
        conectaServidor(IP, puerto, ComunicaServer.TOTEM);
        informaID(escuchadorDeNodoFisico.getId());
        return enviarDNI(dni);
    }

    @Override
    public void conectaServidor(String IP, int puerto, String nodo) {
        try {
            super.conectaServidor(IP, puerto, nodo);
            out.writeUTF(TOTEM_INIT);
            this.socketSimple = new Socket(IP, puerto);
            this.outSimple = new DataOutputStream(socketSimple.getOutputStream());
            this.inSimple = new DataInputStream(socketSimple.getInputStream());
            //out.writeUTF(claveEncriptacion);
            //boolean conexion = Boolean.parseBoolean(in.readUTF());
            //if (!conexion){
            //    System.out.println("CLAVE DE ENCRIPTACION INCORRECTA");
            //    escuchadorDeNodoFisico.conexionErronea("Clave de encriptacion incorrecta");
            //    socket.close();
            //}
            //else{
            //    out.writeUTF(nodo);
            //    escuchadorDeNodoFisico.conexionExitosa();
            //}
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
