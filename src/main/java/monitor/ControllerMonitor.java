package monitor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.ListIterator;

import shared.VistasUtils;
import shared.conexion_server.ComunicaServer;
import shared.conexion_server.ConexionGUI;
import shared.conexion_server.ConexionListener;
import shared.criptografia.FactoryCriptografia;
import shared.criptografia.ICriptografia;
//import shared.criptografia.ICriptografia;
import shared.turno.Turno;

public class ControllerMonitor implements ActionListener, ConexionListener, MonitorEventListener {
    private ConexionGUI vistaConexion;
    private MonitorGUI vistaMonitor;
    private Monitor monitor;
    private MonitorEscuchaServer escuchaServer;
    private String modo = "txt";
    private ICriptografia criptografia;
    private String claveEncriptacion;

    public ControllerMonitor(ConexionGUI vistaConexion, MonitorGUI vistaMonitor, MonitorEscuchaServer escuchaServer) {
        this.vistaConexion = vistaConexion;
        this.vistaMonitor = vistaMonitor;
        this.vistaConexion.setActionListener(this);
        this.escuchaServer = escuchaServer;
        this.claveEncriptacion = null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        VistasUtils.ejecutarNoBloqueante(() -> escuchaServer.conectaServidorPrimeraVez(vistaConexion.getIP(),
                Integer.parseInt(vistaConexion.getPuerto()), ComunicaServer.MONITOR,
                vistaConexion.getClaveEncriptacion()));
    }

    public void iniciaMonitor() {
        monitor = new Monitor("unico", 5, new ListaLlamados(5));
        criptografia = FactoryCriptografia.getCifrador(ICriptografia.AES);
        vistaConexion.mostrar();
        cargarHistorialEnGUI();
    }

    @Override
    public String encriptar(String mensaje) {
        return criptografia.encriptar(mensaje, claveEncriptacion);
    }

    @Override
    public String desencriptar(String mensajeEncriptado) {
        return criptografia.desencriptar(mensajeEncriptado, claveEncriptacion);
    }

    @Override
    public void eventoRecibeLlamado(Turno turno) { // Implementa la logica del monitor para persistirlo, con la salvedad
                                                   // que la vista es la misma que la ultima vez
        monitor.agregaTurno(turno);
        vistaMonitor.registrarLlamado(String.valueOf(turno.getCliente().getDni()), turno.getIdPuesto());
    }

    @Override
    public void eventoRenotificaLlamado(Turno turno) {
        monitor.renotificaTurno(turno);
        vistaMonitor.registrarLlamado(String.valueOf(turno.getCliente().getDni()), turno.getIdPuesto());
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

    public void setClaveEncriptacion(String clave) {
        this.claveEncriptacion = clave;
    }

}
