package puesto;

import java.io.IOException;

import shared.conexion_server.ComunicaServer;


public class PuestoComunicaServer extends ComunicaServer implements Runnable{
    private PuestoEventListener escuchadorDeEventos;

    public void setEscuchadorDeEventos(PuestoEventListener escuchadorDeEventos) {
        this.escuchadorDeEventos = escuchadorDeEventos;
    }

    public void atiendeSiguiente(){

    }

    public void reNotifica(){

    }

    @Override
    public void run() {
        String cantidadEnEspera;
        while(!getSocket().isClosed()){
            try {
                cantidadEnEspera = in.readUTF();
                escuchadorDeEventos.eventoCantidadEnEspera(Integer.parseInt(cantidadEnEspera));
            } catch (IOException e) {
                // TODO : INFORMAR AL ADMIN (Server-side)
                e.printStackTrace();
            }
        }
    }

    public PuestoEventListener getEscuchadorDeEventos() {
        return escuchadorDeEventos;
    }


}
