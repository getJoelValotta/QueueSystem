package server.manejadores;

import server.ControllerServer;

public class ManejaTotem extends ManejadorDeNodos {

    public ManejaTotem(ManejadorEventListener controllerServer, String id) {
        super(controllerServer, id);
    }

    @Override
    public void comunicacion() {
        try {
            String respuesta = in.readUTF();
            boolean validacion = this.controllerServer.recibeYPersisteTurno(respuesta, "IManejaServidores.TURNO_ESPERA"); // Revisa si ya se encuentra en la fila y sino lo agrega
            out.writeBoolean(validacion);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
