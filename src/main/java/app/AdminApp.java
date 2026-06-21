package app;

import admin.AdminComunicaServer;
import admin.AdminGUI;
import admin.ControllerAdmin;

public class AdminApp {

    public static void main (String[] args){
        AdminGUI vistaAdmin = new AdminGUI();
        AdminComunicaServer comunicaServer = new AdminComunicaServer();
        ControllerAdmin controladorAdmin = new ControllerAdmin(vistaAdmin, comunicaServer);
        comunicaServer.setEscuchadorDeNodoFisico(controladorAdmin);
        comunicaServer.setEscuchadorDeEventos(controladorAdmin);
        controladorAdmin.iniciaAdmin();
    }
}
