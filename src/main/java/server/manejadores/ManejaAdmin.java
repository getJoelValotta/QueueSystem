package server.manejadores;

import java.io.IOException;

import admin.AdminComunicaServerP;

public class ManejaAdmin extends ManejadorDeNodos {

    public ManejaAdmin(ManejadorEventListener controllerServer, String id) {
        super(controllerServer, id);
    }

    @Override
    public void comunicacion() {
        try {
            String solicitud = in.readUTF();
            switch (solicitud) {
                case AdminComunicaServerP.PERSISTENCIA:
                    solicitud = in.readUTF();
                    //controllerServer.setModo(solicitud);
                    break;
                case AdminComunicaServerP.ENCRIPTACION:
                    solicitud = in.readUTF();
                    controllerServer.setMetodoEncriptacion(solicitud);
                    break;
                case AdminComunicaServerP.CLAVE:
                    solicitud = in.readUTF();
                    controllerServer.setClaveEncriptacion(solicitud);
                    break;
            }
        } catch (IOException e) {
            try {
                socket.close();
            } catch (IOException ex) {}
        }
    }

    public void logEventoPrincipal(String msg) {
        try {
            out.writeUTF(AdminComunicaServerP.EVENTO_PRINCIPAL);
            enviaLog(msg);
        } catch (IOException e) {
        }
    }

    public void logBienPrincipal(String msg) {
        try {
            out.writeUTF(AdminComunicaServerP.BIEN_PRINCIPAL);
            enviaLog(msg);
        } catch (IOException e) {
        }
    }

    public void logMalPrincipal(String msg) {
        try {
            out.writeUTF(AdminComunicaServerP.MAL_PRINCIPAL);
            enviaLog(msg);
        } catch (IOException e) {
        }
    }

    public void logEventoRespaldo(String msg) {
        try {
            out.writeUTF(AdminComunicaServerP.EVENTO_RESPALDO);
            enviaLog(msg);
        } catch (IOException e) {
        }
    }

    public void enviaLog(String msg) {
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
        }
    }

}
