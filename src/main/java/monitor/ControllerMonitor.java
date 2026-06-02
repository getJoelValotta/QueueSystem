package monitor;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import shared.VistasUtils;
import shared.conexion_server.ComunicaServer;
import shared.conexion_server.ConexionGUI;
import shared.conexion_server.ConexionListener;
import shared.turno.Turno;

public class ControllerMonitor implements ActionListener, ConexionListener, MonitorEventListener{
    private ConexionGUI vistaConexion;
    private MonitorGUI vistaMonitor;
    private Monitor monitor;
    private MonitorEscuchaServer escuchaServer;
    
    public ControllerMonitor(ConexionGUI vistaConexion, MonitorGUI vistaMonitor, MonitorEscuchaServer escuchaServer) {
        this.vistaConexion = vistaConexion;
        this.vistaMonitor = vistaMonitor;
        this.escuchaServer = escuchaServer;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        VistasUtils.ejecutarNoBloqueante(() ->
            escuchaServer.conectaServidor(vistaConexion.getIP(), Integer.parseInt(vistaConexion.getPuerto()), ComunicaServer.PUESTO)
        );
    }

    public void iniciaMonitor(){
        monitor = new Monitor();
        vistaMonitor.mostrar();
    }

    @Override
    public void eventoRecibeLlamado(Turno turno) {

    }

    @Override
    public void conexionErronea(String mensaje) {
        vistaConexion.appendLogError(mensaje);
    }

    @Override
    public void conexionExitosa() {
        vistaMonitor.mostrar();
        vistaConexion.cerrar();
        new Thread(escuchaServer).start();
    }

}
