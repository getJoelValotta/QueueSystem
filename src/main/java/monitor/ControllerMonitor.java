package monitor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.ListIterator;

import puesto.PuestoAjustesGUI;
import shared.VistasUtils;
import shared.conexion_server.ComunicaServer;
import shared.conexion_server.ConexionGUI;
import shared.conexion_server.ConexionListener;
import shared.criptografia.FactoryCriptografia;
import shared.criptografia.ICriptografia;
//import shared.criptografia.ICriptografia;
import shared.persistencia.factory.FabricaPersistencia;
import shared.persistencia.factory.IFactoryPersistenciaArchivos;
import shared.turno.Turno;
import monitor.mapper.MonitorDTO;
import monitor.mapper.MonitorMapper;

public class ControllerMonitor implements ActionListener, ConexionListener, MonitorEventListener {
    private ConexionGUI vistaConexion;
    private MonitorGUI vistaMonitor;
    private Monitor monitor;
    private MonitorEscuchaServer escuchaServer;
    private String metodoPersistencia = "txt";
    private String metodoEncriptacion;
    private ICriptografia criptografia;
    private String claveEncriptacion, modoEncriptacion;
    private IFactoryPersistenciaArchivos factoryPersistencia;
    private MonitorMapper monitorMapper;

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
        cargarMonitor(); // restaura los llamados persistidos antes de mostrarlos
        vistaConexion.mostrar();
        cargarHistorialEnGUI();
    }

    /** Autodetecta el formato persistido y restaura el Monitor si habia datos previos. */
    private void cargarMonitor() {
        factoryPersistencia = FabricaPersistencia.detectarOPara("monitor", metodoPersistencia);
        monitorMapper = factoryPersistencia.fabricaMonitorMapper();
        try {
            MonitorDTO dto = monitorMapper.templateLeer();
            if (dto != null) {
                Monitor restaurado = monitorMapper.toDominio(dto);
                if (restaurado != null && restaurado.getSize() > 0) {
                    this.monitor = restaurado;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Graba el estado actual del Monitor en el formato vigente. */
    private void persistirMonitor() {
        if (monitorMapper == null) {
            return;
        }
        try {
            monitorMapper.templateGrabar(monitorMapper.toDto(monitor));
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        persistirMonitor(); // nuevo llamado recibido
        vistaMonitor.registrarLlamado(String.valueOf(turno.getCliente().getDni()), turno.getIdPuesto());
    }

    @Override
    public void eventoRenotificaLlamado(Turno turno) {
        monitor.renotificaTurno(turno);
        persistirMonitor(); // llamado renotificado
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

    @Override
    public void desconexionForzada() {
        vistaMonitor.cerrar();
        vistaConexion.mostrar();
        System.out.println("llame a las vistas");
    }


    public void setMetodoEncriptacion(String modo) {
        this.modoEncriptacion = modo;
        if (modo.equals(PuestoAjustesGUI.AES)) {
            criptografia = FactoryCriptografia.getCifrador(ICriptografia.AES);
        } else if (modo.equals(PuestoAjustesGUI.CHACHA20)) {
            criptografia = FactoryCriptografia.getCifrador(ICriptografia.CHACHA20);
        }
    }

    public void setMetodoPersistencia(String modo) {
        this.metodoPersistencia = modo;
        // Cambio de formato: se reconstruye el mapper (Abstract Factory) y se persiste
        // el estado completo en el nuevo formato (los archivos previos no se borran).
        this.factoryPersistencia = FabricaPersistencia.para(modo);
        this.monitorMapper = factoryPersistencia.fabricaMonitorMapper();
        persistirMonitor();
    }

}
