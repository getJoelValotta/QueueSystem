package monitor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.ListIterator;

import shared.VistasUtils;
import shared.conexion_server.ComunicaServer;
import shared.conexion_server.ConexionGUI;
import shared.conexion_server.ConexionListener;
import shared.turno.Turno;

public class ControllerMonitor implements ActionListener, ConexionListener, MonitorEventListener {
    private ConexionGUI vistaConexion;
    private MonitorGUI vistaMonitor;
    private Monitor monitor;
    private MonitorEscuchaServer escuchaServer;
    private String modo = "txt";

    public ControllerMonitor(ConexionGUI vistaConexion, MonitorGUI vistaMonitor, MonitorEscuchaServer escuchaServer) {
        this.vistaConexion = vistaConexion;
        this.vistaMonitor = vistaMonitor;
        this.vistaConexion.setActionListener(this);
        this.escuchaServer = escuchaServer;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        VistasUtils.ejecutarNoBloqueante(() -> escuchaServer.conectaServidorPrimeraVez(vistaConexion.getIP(),
                Integer.parseInt(vistaConexion.getPuerto()), ComunicaServer.MONITOR,
                vistaConexion.getClaveEncriptacion()));
    }

    public void iniciaMonitor() {
        try {
            monitor = recuperar();
            System.out.println("Monitor recuperado exitosamente: " + monitor.getId());
        } catch (RuntimeException e) {
            System.out.println("No se pudo recuperar el monitor, se iniciará uno nuevo: " + e.getMessage());
            monitor = new Monitor("unico", 5, new ListaLlamados(5));
        }
        vistaConexion.mostrar();
        cargarHistorialEnGUI();
        persist();
    }

    @Override
    public void eventoRecibeLlamado(Turno turno) { // Implementa la logica del monitor para persistirlo, con la salvedad
                                                   // que la vista es la misma que la ultima vez
        monitor.agregaTurno(turno);
        vistaMonitor.registrarLlamado(String.valueOf(turno.getCliente().getDni()), turno.getIdPuesto());
        persist();
    }

    @Override
    public void eventoRenotificaLlamado(Turno turno) {
        monitor.renotificaTurno(turno);
        vistaMonitor.registrarLlamado(String.valueOf(turno.getCliente().getDni()), turno.getIdPuesto());
        persist();
    }

    @Override
    public void conexionErronea(String mensaje) {
        vistaConexion.appendLogError(mensaje);
    }

    @Override
    public void conexionExitosa() {
        vistaMonitor.mostrar();
        vistaConexion.cerrar();
        System.out.println("CONEXION EXITOSA");
        new Thread(escuchaServer).start();
        System.out.println("INSTANCIE MI THREAD");
    }

    public String getId() {
        return monitor.getId();
    }

    private void persist() {
        LlamaMappersMonitor.persist(modo, monitor);
    }

    private Monitor recuperar() throws RuntimeException {
        System.out.println("Intentando recuperar el monitor...");
        Monitor monitor = LlamaMappersMonitor.load(modo);
        System.out.println("Monitor recuperado exitosamente: " + monitor.getId());
        return monitor;
    }

    private void cargarHistorialEnGUI() {
        LinkedList<Turno> llamados = monitor.getLlamados().getLlamadosList();
        // Iterar en orden inverso: del más viejo al más reciente
        ListIterator<Turno> it = llamados.listIterator(llamados.size());
        while (it.hasPrevious()) {
            Turno turno = it.previous();
            vistaMonitor.registrarLlamado(
                    String.valueOf(turno.getCliente().getDni()),
                    turno.getIdPuesto());
        }
    }
}
