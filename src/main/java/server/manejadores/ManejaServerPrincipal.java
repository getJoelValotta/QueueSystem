package server.manejadores;

import java.io.IOException;
import java.net.SocketException;

import server.ListaTurnos;
import server.id.GestorID;
import shared.turno.Turno;

public class ManejaServerPrincipal extends ManejadorDeNodos implements IManejaServidores {
    private int cantErrores;
    private ManejadorEventListener controllerServer;

    public ManejaServerPrincipal(ManejadorEventListener controllerServer) {
        this.controllerServer = controllerServer;
        this.cantErrores = 0;
    }

    @Override
    public void comunicacion() {
        String dni;
        try {
            socket.setSoTimeout(IManejaServidores.TIMEOUT_CAIDA_MS); // Es el tiempo que espera a que le llegue algo y determinar si se cayo o no (hearthbeat)                              
            String respuesta = in.readUTF();
            switch (respuesta) {
                case IManejaServidores.GESTOR:
                    String totem, puesto, monitor;
                    totem = in.readUTF();
                    puesto = in.readUTF();
                    monitor = in.readUTF();
                    controllerServer.recibeYPersiste(totem, puesto, monitor);
                    break;
                case IManejaServidores.TURNO_ESPERA:
                    dni = in.readUTF();
                    controllerServer.recibeYPersisteTurno(dni, IManejaServidores.TURNO_ESPERA);
                    break;
                case IManejaServidores.TURNO_ATENCION:
                    dni = in.readUTF();
                    controllerServer.recibeYPersisteTurno(dni, IManejaServidores.TURNO_ATENCION);
                    break;
                case IManejaServidores.HBOUT:
                    // TODO : Informarle al admin 
                    break;
            }
        } catch (SocketException e) {
            this.cantErrores += 1;
            if (cantErrores == 2){
                //TODO : Informar a ControllerServer que debe ejecutar server.switchServer();
            }
            e.printStackTrace();
        } catch (IOException e) {
            //Informar a Controller que debe ejecutar server.switchServer();
            e.printStackTrace();
        }
    }

    @Override
    public void comunicaGestor(GestorID gestorID) {
    }

    @Override
    public void comunicaTurno(Turno turno, String tipo) {
    }

    @Override
    public void comunicaListaTurnos(ListaTurnos turnos, String tipo) {
    }

    public ManejadorEventListener getControllerServer() {
        return controllerServer;
    }

}
