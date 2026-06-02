package app;

import com.formdev.flatlaf.FlatLightLaf;

import puesto.ControllerPuesto;
import puesto.PuestoComunicaServer;
import puesto.PuestoGUI;
import shared.conexion_server.ConexionGUI;

public class PuestoApp {

    public static void main(String[] args) {
        FlatLightLaf.setup();
        ConexionGUI vistaConexion = new ConexionGUI();
        PuestoGUI vistaPuesto = new PuestoGUI();
        PuestoComunicaServer comunicaServer = new PuestoComunicaServer();
        ControllerPuesto controladorPuesto = new ControllerPuesto(vistaConexion, vistaPuesto, comunicaServer);
        comunicaServer.setEscuchadorDeNodoFisico(controladorPuesto);
        comunicaServer.setEscuchadorDeEventos(controladorPuesto);
        controladorPuesto.iniciaPuesto();
    }
}
