package app;

import com.formdev.flatlaf.FlatLightLaf;

import shared.conexion_server.ConexionGUI;
import totem.ControllerTotem;
import totem.TotemComunicaServer;
import totem.TotemGUI;

public class TotemApp {

    public static void main(String[] args) {
        FlatLightLaf.setup();
        ConexionGUI vistaConexion = new ConexionGUI();
        TotemGUI vistaTotem = new TotemGUI();
        TotemComunicaServer comunicaServer = new TotemComunicaServer();
        ControllerTotem controladorTotem = new ControllerTotem(vistaConexion, vistaTotem, comunicaServer);
        comunicaServer.setEscuchadorDeNodoFisico(controladorTotem);
        comunicaServer.setEscuchadorDeEventos(controladorTotem);
        controladorTotem.iniciaTotem();
    }
}
