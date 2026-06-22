package app;

import admin.AdminComunicaServerP;
import admin.AdminComunicaServerR;
import admin.AdminGUI;
import admin.ControllerAdmin;

public class AdminApp {

    public static void main (String[] args){ñ
        AdminGUI vistaAdmin = new AdminGUI();
        AdminComunicaServerP comunicaServerPrincipal = new AdminComunicaServerP();
        AdminComunicaServerR comunicaServerRespaldo = new AdminComunicaServerR();
        ControllerAdmin controladorAdmin = new ControllerAdmin(vistaAdmin, comunicaServerPrincipal, comunicaServerRespaldo);
        comunicaServerPrincipal.setEscuchadorDeEventos(controladorAdmin);
        comunicaServerRespaldo.setEscuchadorDeEventos(controladorAdmin);
        controladorAdmin.iniciaAdmin();
    }
}
