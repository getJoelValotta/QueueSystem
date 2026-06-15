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
        this.vistaConexion.setActionListener(this);
        this.escuchaServer = escuchaServer;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        VistasUtils.ejecutarNoBloqueante(() ->
            escuchaServer.conectaServidor(vistaConexion.getIP(), Integer.parseInt(vistaConexion.getPuerto()), ComunicaServer.MONITOR)
        );
    }

    public void iniciaMonitor(){
        monitor = new Monitor();
        vistaConexion.mostrar();
    }

    @Override 
    public void eventoRecibeLlamado(Turno turno) { // Implementa la logica del monitor para persistirlo, con la salvedad que la vista es la misma que la ultima vez
        monitor.agregaTurno(turno);
        vistaMonitor.registrarLlamado( String.valueOf(turno.getCliente().getDni()) , turno.getIdPuesto());
    }

    @Override
    public void eventoRenotificaLlamado(Turno turno){
        monitor.renotificaTurno(turno);
        vistaMonitor.registrarLlamado( String.valueOf(turno.getCliente().getDni()) , turno.getIdPuesto());
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
