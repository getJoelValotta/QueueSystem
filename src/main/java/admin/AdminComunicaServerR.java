package admin;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import shared.conexion_server.ComunicaServer;

public class AdminComunicaServerR extends ComunicaServer implements Runnable {
    private AdminEventListener escuchadorDeEventos;
    public static final String XML = "#XML#", JSON = "#JSON#", TXT = "#TXT#", MD5 = "#MD5#", SHA_2 = "#SHA_2#",
            EVENTO_PRINCIPAL = "#EV_PRI#", BIEN_PRINCIPAL = "#BIEN_PRI#", MAL_PRINCIPAL = "#MAL_PRI#",
            EVENTO_RESPALDO = "#EV_RES#";

    public AdminComunicaServerR(){
        super();
        puerto = 1338;
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
                Thread.sleep(2000);
                System.out.println("PUERTO = " + puerto);
            } catch (Exception e){}
            String msg;
            while (getSocket() != null && !getSocket().isClosed()) {
                try {
                    comando = in.readUTF();
                    switch (comando) {
                        case EVENTO_RESPALDO: 
                            msg = in.readUTF();
                            escuchadorDeEventos.muestraLog(msg, EVENTO_RESPALDO);// Cualquier cosa de interes del servidor de respaldo.
                            break;
                    }
                } catch (IOException e) {
                    try {
                        getSocket().close();
                        escuchadorDeEventos.cambiarEstado(ControllerAdmin.RESPALDO, false);
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
            out.writeUTF(nodo);
            escuchadorDeEventos.cambiarEstado(ControllerAdmin.RESPALDO, true);
        } catch (IOException e) { 
            //escuchadorDeNodoFisico.conexionErronea("Error de protocolo de conexion");
            e.printStackTrace();
        }
    }

}
