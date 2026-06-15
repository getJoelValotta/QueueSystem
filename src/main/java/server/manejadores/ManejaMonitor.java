package server.manejadores;

import java.io.IOException;

import monitor.MonitorEscuchaServer;
import shared.turno.Turno;

public class ManejaMonitor extends ManejadorDeNodos{

    public ManejaMonitor(ManejadorEventListener controllerServer, String id) {
        super(controllerServer, id);
    }

    @Override
    public void comunicacion() { 

    }


    public void llamaMonitor(Turno turno){
        try {
            System.out.println("Soy ManejaMonitor y llame1");
            out.writeUTF(MonitorEscuchaServer.LLAMA);
            System.out.println("Soy ManejaMonitor y llame2");
            out.writeUTF(turno.getIdPuesto());
            System.out.println("Soy ManejaMonitor y llame3");
            out.writeUTF(String.valueOf(turno.getCliente().getDni()));
            System.out.println("Soy ManejaMonitor y llame4");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void renotificaMonitor(Turno turno){
        try {
            out.writeUTF(MonitorEscuchaServer.RENOTIFICA);
            out.writeUTF(turno.getIdPuesto());
            out.writeUTF(String.valueOf(turno.getCliente().getDni()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
