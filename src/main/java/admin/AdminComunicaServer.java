package admin;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import shared.conexion_server.ComunicaServer;

public class AdminComunicaServer extends ComunicaServer implements Runnable {
    private AdminEventListener escuchadorDeEventos;
    public static final String XML = "#XML#", JSON = "#JSON#", TXT = "#TXT#", MD5 = "#MD5#", SHA_2 = "#SHA_2#",
            EVENTO_PRINCIPAL = "#EV_PRI#", BIEN_PRINCIPAL = "#BIEN_PRI#", MAL_PRINCIPAL = "#MAL_PRI#",
            EVENTO_RESPALDO = "#EV_RES#";
    private int puerto2 = puerto + 1;
    private Socket socketRespaldo = null;
    protected DataOutputStream outRespaldo;
    protected DataInputStream inRespaldo;

    public void setEscuchadorDeEventos(AdminEventListener escuchadorDeEventos) {
        this.escuchadorDeEventos = escuchadorDeEventos;
    }

    @Override
    public void run() {
        String comando;
        while (true) {
            conectaServidor(IP, puerto2, ComunicaServer.ADMIN);
            System.out.println("Voy a intentar conectar respaldo");
            if (socketRespaldo != null && !socketRespaldo.isClosed())
                conectaServidoRespaldo(IP, puerto2, ComunicaServer.ADMIN);
            while (getSocket() != null && !getSocket().isClosed()) {
                try {
                    comando = in.readUTF();
                    switch (comando) {
                        case EVENTO_PRINCIPAL: // Aca se colocan mensajes en la consola de nodos que se conectan o se
                                               // desconectan
                            // escuchadorDeEventos.muestraLog();
                            break;
                        case BIEN_PRINCIPAL: // Aca se colocan mensajes en la consola de requests de los nodos al server
                                             // principal
                            break;
                        case MAL_PRINCIPAL: // Aca se colocan los mensajes cuya verificacion es negativa o mal hecha
                            break;
                        case EVENTO_RESPALDO: // Cualquier cosa de interes del servidor de respaldo.
                            break;
                    }

                    if (socketRespaldo == null || socketRespaldo.isClosed()) {
                        //new Thread(() -> {
                            //conectaServidoRespaldo(IP, puerto2, ComunicaServer.ADMIN);
                        //}).start();
                        conectaServidoRespaldo(IP, puerto2, ComunicaServer.ADMIN);
                    }

                } catch (IOException e) {
                    try {
                        getSocket().close();
                    } catch (IOException e1) {
                    }
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void conectaServidor(String IP, int puerto, String nodo) {
        super.conectaServidor(IP, puerto, nodo);
        escuchadorDeEventos.cambiarEstado(ControllerAdmin.PRINCIPAL, true);
    }

    public void conectaServidoRespaldo(String IP, int puerto, String nodo) {
        try {
            System.out.println("Conectando respaldo");
            if (socketRespaldo != null) {
                socketRespaldo.close();
            }
            this.socketRespaldo = new Socket(IP, puerto);
            System.out.println("Socket creado");
            this.outRespaldo = new DataOutputStream(socketRespaldo.getOutputStream());
            this.inRespaldo = new DataInputStream(socketRespaldo.getInputStream());
            outRespaldo.writeUTF(nodo);
            escuchadorDeEventos.cambiarEstado(ControllerAdmin.RESPALDO, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void switchServers() {
    }

    public boolean enviaTipoPersistencia(String tipoPersistencia) {
        boolean respuesta = false;
        try {
            outRespaldo.writeUTF(tipoPersistencia);
            respuesta = Boolean.parseBoolean(inRespaldo.readUTF());
        } catch (IOException e) {
        }

        return respuesta;
    }

    public boolean enviaTipoEncriptacion(String tipoEncriptacion) {
        boolean respuesta = false;
        try {
            outRespaldo.writeUTF(tipoEncriptacion);
            respuesta = Boolean.parseBoolean(inRespaldo.readUTF());
        } catch (IOException e) {
        }

        return respuesta;
    }

}
