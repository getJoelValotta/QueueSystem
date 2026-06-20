package server.manejadores;

import java.io.IOException;

public class ManejaAdmin extends ManejadorDeNodos{

    public ManejaAdmin(ManejadorEventListener controllerServer, String id) {
        super(controllerServer, id);
    }

    @Override
    public void comunicacion() {
    }

    public void enviaLog (String msg){
        try{
            out.writeUTF(msg);
        } catch(IOException e){
        }
    }

}
