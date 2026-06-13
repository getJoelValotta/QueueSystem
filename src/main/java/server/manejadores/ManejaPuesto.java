package server.manejadores;

import java.io.IOException;

import puesto.Puesto;
import puesto.PuestoComunicaServer;
import shared.turno.Turno;

public class ManejaPuesto extends ManejadorDeNodos implements IControllerObserver {
    private Object mutex = new Object(); // Auxiliar para el manejo de zonas criticas de los in/out de los sockets.

    public ManejaPuesto(ManejadorEventListener controllerServer, String id) {
        super(controllerServer, id);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void comunicacion() {
        try {
            // socket.setSoTimeout(500);
            String respuesta = in.readUTF();

            System.out.println("\n\n respuesta = " + respuesta + "\n\n");
            switch (respuesta) {
                case PuestoComunicaServer.ATIENDE:
                    Turno turno = controllerServer.llamaSiguienteTurno(this.id);
                    System.out.println("EL TURNO ES " + (turno != null));
                    if (turno != null) {
                        System.out.println("DNI CLIENTE = " + String.valueOf(turno.getCliente().getDni()));
                        out.writeUTF(String.valueOf(turno.getCliente().getDni())); // turno nunca deberia ser nulo porque siempre llama cuando el boton no esata bloqueado.
                    }
                    System.out.println("LO MANDE WACHO!");
                    break;
                case PuestoComunicaServer.RENOTIFICA:
                    controllerServer.actualizaTurnoRenotificado(this.id);
                    break;
            }
        } catch (IOException e) {
            // try {
            // System.out.println("Puesto " + this.id + " desconectado. ERROR: " +
            // e.getMessage());
            // socket.close();
            // } //catch (IOException e1) {
            // TODO Auto-generated catch block
            // e1.printStackTrace();
            // }
        }
    }

    public void enviaCantidadEnEspera(int Cant) {
        synchronized (mutex) {
            try {
                out.writeUTF(String.valueOf(Cant));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void actualizar() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'actualizar'");
    }
}
