package server.manejadores;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import admin.AdminComunicaServerP;
import puesto.PuestoAjustesGUI;
import puesto.PuestoComunicaServer;
import shared.turno.Turno;

public class ManejaPuesto extends ManejadorDeNodos implements IControllerObserver {
    private Object mutex = new Object(); // Auxiliar para el manejo de zonas criticas de los in/out de los sockets.
    private Socket socketSimple;
    protected DataOutputStream outSimple;
    protected DataInputStream inSimple;
    
    public ManejaPuesto(ManejadorEventListener controllerServer, String id) {
        super(controllerServer, id);
        // TODO Auto-generated constructor stub
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
            // socket.setSoTimeout(500);
            String respuesta = inSimple.readUTF();
            switch (respuesta) {
                case PuestoComunicaServer.ATIENDE:
                    Turno turno = controllerServer.llamaSiguienteTurno(this.id);
                    String dni = String.valueOf(turno.getCliente().getDni());
                    String dniEncriptado = controllerServer.encriptar(dni);
                    outSimple.writeUTF(dniEncriptado); // turno nunca deberia ser nulo porque siempre llama cuando el boton no esata bloqueado.
                    controllerServer.avisarAdmin("Llamando siguiente desde Puesto " + id, AdminComunicaServerP.BIEN_PRINCIPAL);
                    break;
                case PuestoComunicaServer.RENOTIFICA:
                    boolean valido = controllerServer.actualizaTurnoRenotificado(this.id);
                    outSimple.writeUTF(String.valueOf(valido));
                    controllerServer.avisarAdmin("Renotificando cliente desde Puesto " + id, AdminComunicaServerP.BIEN_PRINCIPAL);
                    break;
                case PuestoAjustesGUI.ENVIAR_CLAVE:
                    System.out.println("LLego solicitud de nueva clave");
                    respuesta = inSimple.readUTF();
                    controllerServer.setClaveEncriptacion(respuesta);
                    controllerServer.desconexionForzada();
                    break;
                case PuestoAjustesGUI.AES:
                case PuestoAjustesGUI.CHACHA20:
                    controllerServer.setMetodoEncriptacion(respuesta);
                    break;
                case PuestoAjustesGUI.JSON:
                case PuestoAjustesGUI.TXT:
                case PuestoAjustesGUI.XML:
                    controllerServer.setMetodoPersistencia(respuesta);
                    break;

            }
        } catch (IOException e) {
            try{
                socket.close();
                socketSimple.close(); //TODO : OJO CON ESTO, SI HACE CAGADA EN PUESTO BORRAR
                controllerServer.avisarAdmin("Puesto con ID " + id + " desconectado.", AdminComunicaServerP.MAL_PRINCIPAL);
            } catch(Exception e1){}
            
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
