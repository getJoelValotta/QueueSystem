package server.manejadores;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import admin.AdminComunicaServerP;

public class ManejaTotem extends ManejadorDeNodos implements IControllerObserver{
    private Socket socketSimple;
    protected DataOutputStream outSimple;
    protected DataInputStream inSimple;

    public ManejaTotem(ManejadorEventListener controllerServer, String id) {
        super(controllerServer, id);
    }

    public void setSocketSimple(Socket socket){
        this.socketSimple = socket;
        try {
            outSimple = new DataOutputStream(socketSimple.getOutputStream());
            inSimple = new DataInputStream(socketSimple.getInputStream());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void comunicacion() {
        try {
            String respuesta = inSimple.readUTF();
            boolean validacion = this.controllerServer.recibeYPersisteTurno(respuesta, IManejaServidores.TURNO_ESPERA); // Revisa si ya se encuentra en la fila y sino lo agrega
            outSimple.writeUTF(String.valueOf(validacion));
            if (!validacion){
                controllerServer.avisarAdmin("DNI agregado desde Totem " + id, AdminComunicaServerP.BIEN_PRINCIPAL);
            } else {
                controllerServer.avisarAdmin("No se pudo agregar DNI desde Totem " + id, AdminComunicaServerP.MAL_PRINCIPAL);
            }
        } catch (Exception e) {
            try {
                socketSimple.close();
                socket.close();
                controllerServer.avisarAdmin("Totem con ID " + id + " desconectado.", AdminComunicaServerP.MAL_PRINCIPAL);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    @Override
    public void actualizar() {
    }


}
