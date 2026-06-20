package admin;

import shared.conexion_server.ComunicaServer;

public class AdminComunicaServer extends ComunicaServer implements Runnable{
    private AdminEventListener escuchadorDeEventos;
    public static final String XML = "#XML#", JSON = "#JSON#", TXT = "#TXT#", MD5 = "#MD5#", SHA_2 = "#SHA_2#",
    EVENTO_PRINCIPAL = "#EV_PRI#", BIEN_PRINCIPAL = "#BIEN_PRI#", ERROR_PRINCIPAL = "#ERROR_PRI#", EVENTO_RESPALDO = "#EV_RES#"; 

    public void setEscuchadorDeEventos(AdminEventListener escuchadorDeEventos) {
        this.escuchadorDeEventos = escuchadorDeEventos;
    }

    @Override
    public void run() {
        while (!getSocket().isClosed()){
            
        }
    }


}
