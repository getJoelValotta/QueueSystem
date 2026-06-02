package app;

import com.formdev.flatlaf.FlatLightLaf;

import monitor.ControllerMonitor;
import monitor.MonitorEscuchaServer;
import monitor.MonitorGUI;
import shared.conexion_server.ConexionGUI;

public class MonitorApp {

    public static void main(String[] args) {
        FlatLightLaf.setup();
        ConexionGUI vistaConexion = new ConexionGUI();
        MonitorGUI vistaMonitor = new MonitorGUI();
        MonitorEscuchaServer escuchaServer = new MonitorEscuchaServer();
        ControllerMonitor controladorMonitor = new ControllerMonitor(vistaConexion, vistaMonitor, escuchaServer);
        escuchaServer.setEscuchadorDeEventos(controladorMonitor);
        escuchaServer.setEscuchadorDeNodoFisico(controladorMonitor);
        controladorMonitor.iniciaMonitor();
    }
}
