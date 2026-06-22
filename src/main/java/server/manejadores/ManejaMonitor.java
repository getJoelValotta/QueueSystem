package server.manejadores;

import java.io.IOException;

import monitor.MonitorEscuchaServer;
import shared.turno.Turno;
import admin.AdminComunicaServerP;

public class ManejaMonitor extends ManejadorDeNodos{

    public ManejaMonitor(ManejadorEventListener controllerServer, String id) {
        super(controllerServer, id);
    }

    @Override
    public void comunicacion() { 

    }


    public void llamaMonitor(Turno turno){
        try {
            out.writeUTF(MonitorEscuchaServer.LLAMA);
            out.writeUTF(turno.getIdPuesto());
            String dni = String.valueOf(turno.getCliente().getDni());
            String dniEncriptado = controllerServer.encriptar(dni);
            out.writeUTF(dniEncriptado);
        } catch (IOException e) {
            e.printStackTrace();
            try{
                socket.close();
                controllerServer.avisarAdmin("Monitor con ID " + id + " desconectado.", AdminComunicaServerP.MAL_PRINCIPAL);
            } catch(Exception e1){}
        }
    }

    public void renotificaMonitor(Turno turno){
        try {
            out.writeUTF(MonitorEscuchaServer.RENOTIFICA);
            out.writeUTF(turno.getIdPuesto());
            String dni = String.valueOf(turno.getCliente().getDni());
            String dniEncriptado = controllerServer.encriptar(dni);
            out.writeUTF(dniEncriptado);
        } catch (IOException e) {
            e.printStackTrace();
            try{
                socket.close();
                controllerServer.avisarAdmin("Monitor con ID " + id + " desconectado.", AdminComunicaServerP.MAL_PRINCIPAL);
            } catch(Exception e1){}
        }
    }
}
