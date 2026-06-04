package server.manejadores;

import java.io.IOException;

import puesto.Puesto;
import puesto.PuestoComunicaServer;
import shared.turno.Turno;

public class ManejaPuesto extends ManejadorDeNodos {
    private Object mutex = new Object(); // Auxiliar para el manejo de zonas criticas de los in/out de los sockets.

    public ManejaPuesto(ManejadorEventListener controllerServer, String id) {
        super(controllerServer, id);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void comunicacion() {
        synchronized (mutex) {
            try {
                String respuesta = in.readUTF();
                switch (respuesta) {
                    case PuestoComunicaServer.ATIENDE:
                        Turno turno = controllerServer.llamaSiguienteTurno(this.id);
                        if (turno != null) {
                            out.writeUTF(String.valueOf(turno.getCliente().getDni())); // turno nunca deberia ser nulo porque siempre llama cuando el boton no esata bloqueado.
                        }
                        break;
                    case PuestoComunicaServer.RENOTIFICA:
                        controllerServer.actualizaTurnoRenotificado(this.id);
                        break;
                }
            } catch (IOException e) {
                try {
                    socket.close();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        }
    }

    public void enviaCantidadEnEspera(int Cant){
        synchronized (mutex){
            try {
                out.writeUTF(String.valueOf(Cant));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
