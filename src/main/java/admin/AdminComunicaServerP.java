package admin;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import shared.conexion_server.ComunicaServer;

public class AdminComunicaServerP extends ComunicaServer implements Runnable {
    private AdminEventListener escuchadorDeEventos;
    public static final String XML = "#XML#", JSON = "#JSON#", TXT = "#TXT#", MD5 = "#MD5#", SHA_2 = "#SHA_2#",
            EVENTO_PRINCIPAL = "#EV_PRI#", BIEN_PRINCIPAL = "#BIEN_PRI#", MAL_PRINCIPAL = "#MAL_PRI#",
            EVENTO_RESPALDO = "#EV_RES#";

    public AdminComunicaServerP(){
        super();
        puerto = 1337;
    }

    public void setEscuchadorDeEventos(AdminEventListener escuchadorDeEventos) {
        this.escuchadorDeEventos = escuchadorDeEventos;
    }

    @Override
    public void run() {
        String comando;
        while (true) {
            try{
                conectaServidor(IP, puerto, ComunicaServer.ADMIN);
                System.out.println("PUERTO = " + puerto);
                Thread.sleep(2000);
            } catch (Exception e){}
            String msg;
            while (getSocket() != null && !getSocket().isClosed()) {
                try {
                    comando = in.readUTF();
                    switch (comando) {
                        case EVENTO_PRINCIPAL: // Aca se colocan mensajes en la consola de nodos que se conectan o se desconectan
                            msg = in.readUTF();
                            escuchadorDeEventos.muestraLog(msg, EVENTO_PRINCIPAL);
                            break;
                        case BIEN_PRINCIPAL: // Aca se colocan mensajes en la consola de requests de los nodos al server principal
                            msg = in.readUTF();
                            escuchadorDeEventos.muestraLog(msg, BIEN_PRINCIPAL);
                            break;
                        case MAL_PRINCIPAL: // Aca se colocan los mensajes cuya verificacion es negativa o mal hecha
                            msg = in.readUTF();
                            escuchadorDeEventos.muestraLog(msg, MAL_PRINCIPAL);
                            break;
                    }
                } catch (IOException e) {
                    try {
                        getSocket().close();
                        escuchadorDeEventos.cambiarEstado(ControllerAdmin.PRINCIPAL, false);
                    } catch (IOException e1) {
                    }
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void conectaServidor(String IP, int puerto, String nodo) {
        try{
            this.socket = new Socket(IP, puerto);
            this.out = new DataOutputStream(socket.getOutputStream());
            this.in = new DataInputStream(socket.getInputStream());
            System.out.println("ACABO DE INSTANCIAR UN SOCKET");
            out.writeUTF(claveEncriptacion);
            out.writeUTF(nodo);
            escuchadorDeEventos.cambiarEstado(ControllerAdmin.PRINCIPAL, true);
        } catch (IOException e) { 
            //escuchadorDeNodoFisico.conexionErronea("Error de protocolo de conexion");
            e.printStackTrace();
        }
    }

    public boolean enviaTipoPersistencia(String tipoPersistencia) {
        boolean respuesta = false;
        try {
            out.writeUTF(tipoPersistencia);
            respuesta = Boolean.parseBoolean(in.readUTF());
        } catch (IOException e) {
        }

        return respuesta;
    }

    public boolean enviaTipoEncriptacion(String tipoEncriptacion) {
        boolean respuesta = false;
        try {
            out.writeUTF(tipoEncriptacion);
            respuesta = Boolean.parseBoolean(in.readUTF());
        } catch (IOException e) {
        }

        return respuesta;
    }
}
