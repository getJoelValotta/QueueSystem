package server.manejadores;

import java.io.IOException;

import admin.AdminComunicaServer;

public class ManejaAdmin extends ManejadorDeNodos{

    public ManejaAdmin(ManejadorEventListener controllerServer, String id) {
        super(controllerServer, id);
    }

    @Override
    public void comunicacion() {
        
    }

    public void logEventoPrincipal(String msg){
        try{
            out.writeUTF(AdminComunicaServer.EVENTO_PRINCIPAL);
            enviaLog(msg);
        }catch(IOException e){  
        }
    }

    public void logBienPrincipal(String msg){
        try{
            out.writeUTF(AdminComunicaServer.BIEN_PRINCIPAL);
            enviaLog(msg);
        }catch(IOException e){  
        }
    }

    public void logMalPrincipal(String msg){
        try{
            out.writeUTF(AdminComunicaServer.MAL_PRINCIPAL);
            enviaLog(msg);
        }catch(IOException e){  
        }
    }

    public void enviaLog (String msg){
        try{
            out.writeUTF(msg);
        } catch(IOException e){
        }
    }

}
