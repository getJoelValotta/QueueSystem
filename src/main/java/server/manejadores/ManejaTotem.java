package server.manejadores;

import java.io.IOException;

import server.ControllerServer;
import admin.AdminComunicaServerP;

public class ManejaTotem extends ManejadorDeNodos {

    public ManejaTotem(ManejadorEventListener controllerServer, String id) {
        super(controllerServer, id);
    }

    @Override
    public void comunicacion() {
        try {
            String respuesta = in.readUTF();
            boolean validacion = this.controllerServer.recibeYPersisteTurno(respuesta, IManejaServidores.TURNO_ESPERA); // Revisa si ya se encuentra en la fila y sino lo agrega
            out.writeUTF(String.valueOf(validacion));
            if (!validacion){
                controllerServer.avisarAdmin("DNI agregado desde Totem " + id, AdminComunicaServerP.BIEN_PRINCIPAL);
            } else {
                controllerServer.avisarAdmin("No se pudo agregar DNI desde Totem " + id, AdminComunicaServerP.MAL_PRINCIPAL);
            }
        } catch (Exception e) {
            try {
                socket.close();
                controllerServer.avisarAdmin("Totem con ID " + id + " desconectado.", AdminComunicaServerP.MAL_PRINCIPAL);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
    }

}
