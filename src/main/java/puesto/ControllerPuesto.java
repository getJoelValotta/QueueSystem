package puesto;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import admin.AdminComunicaServerP;
import shared.VistasUtils;
import shared.conexion_server.ComunicaServer;
import shared.conexion_server.ConexionGUI;
import shared.conexion_server.ConexionListener;
import shared.criptografia.FactoryCriptografia;
import shared.criptografia.ICriptografia;
import shared.turno.Turno;

import puesto.persistencia.ConfigPuesto;

public class ControllerPuesto implements ActionListener, ConexionListener, PuestoEventListener {
    private ConexionGUI vistaConexion;
    private PuestoGUI vistaPuesto;
    private Puesto puesto;
    private PuestoComunicaServer comunicaServer;
    private String modoPersistencia = "txt";
    private ICriptografia criptografia;
    private String claveEncriptacion, modoEncriptacion;

    public ControllerPuesto(ConexionGUI vistaConexion, PuestoGUI vistaPuesto, PuestoComunicaServer comunicaServer) {
        this.vistaConexion = vistaConexion;
        this.vistaPuesto = vistaPuesto;
        this.vistaConexion.setActionListener(this);
        this.vistaPuesto.setActionListener(this);
        this.comunicaServer = comunicaServer;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case ConexionGUI.CONECTAR:
                VistasUtils.ejecutarNoBloqueante(() -> {
                    comunicaServer.conectaServidorPrimeraVez(vistaConexion.getIP(),
                            Integer.parseInt(vistaConexion.getPuerto()), ComunicaServer.PUESTO,
                            vistaConexion.getClaveEncriptacion());
                    // vistaPuesto.inhabilitaRenotificar();
                    if (puesto.getId() == null) {
                        System.out.println("Solicito id");
                        String id = comunicaServer.solicitaID();
                        puesto.setId(id);
                    } else {
                        comunicaServer.informaID(puesto.getId());
                    }
                    vistaPuesto.setNumPuesto(puesto.getId());
                    new Thread(comunicaServer).start();
                });
                break;
            case PuestoGUI.LLAMAR: // Aca comienza a atender a un cliente y es llamado por primera vez.
                vistaPuesto.limpiarClienteActual();
                VistasUtils.ejecutarNoBloqueante(() -> {
                    Turno turno = comunicaServer.atiendeSiguiente(puesto.getId());
                    if (turno != null) {
                        puesto.setTurno(turno);
                        vistaPuesto.setClienteActual(turno.getCliente().getDni());
                    }
                    vistaPuesto.setClienteActual(puesto.getTurno().getCliente().getDni()); // Esto antes estaba fuera de
                                                                                           // ejecutarNoBloqueante y se
                                                                                           // rompia porque se ejecutaba
                                                                                           // antes de esperar a la
                                                                                           // comunicacion.
                    vistaPuesto.habilitaRenotificar();
                });
                break;
            case PuestoGUI.RENOTIFICAR: // Tomo el turno y consulto si su estado esta en atencion para poder hacer otro
                                        // llamado, de ser asi mando al sv la peticion y si me da el ok llamo
                Turno turnoActual = puesto.getTurno(); // En caso de que la comunicacion falle (devuelva false por algun
                                                       // motivo) se informa a la vista con un mensaje de error que no
                                                       // se envio su peticion.
                int auxCantLlamados = turnoActual.getCantLlamados();
                if (turnoActual.estaEnAtencion() & auxCantLlamados < 3)
                    VistasUtils.ejecutarNoBloqueante(() -> {
                        if (comunicaServer.reNotifica()) {
                            System.out.println("====Renotificacion exitosa====");
                            turnoActual.llamar();
                            vistaPuesto.setMensajeExito();
                            if (auxCantLlamados + 1 == 3)
                                vistaPuesto.cambiaTextRenotificarAabandonado();
                        } else {
                            vistaPuesto.setMensajeError();
                        }
                    });
                else { // En el caso de que este en el ultimo llamado (3), se cambia el boton de
                       // renotificar para marcarlo como abandono.
                    VistasUtils.ejecutarNoBloqueante(() -> {
                        if (comunicaServer.reNotifica()) {
                            turnoActual.llamar();
                            vistaPuesto.setMensajeExito();
                            vistaPuesto.cambiaTextAbandonadoARenotificar();
                            vistaPuesto.inhabilitaRenotificar();
                        } else {
                            vistaPuesto.setMensajeError();
                        }
                    });
                }
                break;
        }
        try {
            // TREMENDO HARDCODEO PERO FUNCIONA
            Thread.sleep(250);
        } catch (InterruptedException es) {
            System.out.println("Error al hacer sleep despues de accion realizada");
        }
        persistePuesto(modoPersistencia);
    }

    @Override
    public void eventoCantidadEnEspera(int cantEspera) {
        vistaPuesto.setCantClientes(cantEspera);
        if (cantEspera == 0) {
            vistaPuesto.inhabilitarBtn();
        } else {
            vistaPuesto.habilitarBtn();
        }
    }

    @Override
    public void setClaveEncriptacion(String clave) {
        this.claveEncriptacion = clave;
        LlamaMappers.persistirConfig(this.modoPersistencia, this.modoEncriptacion, this.claveEncriptacion);
    }

    public void iniciaPuesto() {
        // Carga el puesto por persistencia. si no hay archivo entonces le pido al
        // server la id por primera vez: pue_001
        try {
            ConfigPuesto config = LlamaMappers.cargarConfig();
            this.modoPersistencia = config.getModoPersistencia();
            this.modoEncriptacion = config.getModoEncriptacion();
            this.claveEncriptacion = config.getClaveEncriptacion();
        } catch (RuntimeException e) {
            System.err.println("Error al cargar config, puesto se configura en txt y AES: " + e.getMessage());
            this.modoPersistencia = "txt";
            this.modoEncriptacion = "AES";
            this.claveEncriptacion = "clave";
        }

        try {
            this.puesto = LlamaMappers.cargarPuesto(modoPersistencia);
        } catch (RuntimeException e) {
            System.err.println("Error al cargar puesto, se inicia uno nuevo: " + e.getMessage());
            puesto = new Puesto();
        }
        LlamaMappers.persistirConfig(modoPersistencia, modoEncriptacion, claveEncriptacion);

        // Actualizar vista si hay un puesto y turno cargado
        if (puesto != null && puesto.getId() != null) {
            vistaPuesto.setNumPuesto(puesto.getId());
            if (puesto.getTurno() != null) {
                vistaPuesto.setClienteActual(puesto.getTurno().getCliente().getDni());
                if (puesto.getTurno().estaEnAtencion()) {
                    vistaPuesto.habilitaRenotificar();
                } else {
                    vistaPuesto.inhabilitaRenotificar();
                }
                vistaPuesto.habilitarBtn();
            }
        } else {
            vistaPuesto.inhabilitarBtn();
            vistaPuesto.inhabilitaRenotificar();
        }
        if (this.modoEncriptacion == "AES") {
            this.criptografia = FactoryCriptografia.getCifrador(ICriptografia.AES);
        } else if (this.modoEncriptacion == "CHACHA20") {
            this.criptografia = FactoryCriptografia.getCifrador(ICriptografia.CHACHA20);
        } else {
            System.err.println("Modo de encriptacion no reconocido, se setea AES por defecto");
            this.modoEncriptacion = "AES";
            this.criptografia = FactoryCriptografia.getCifrador(ICriptografia.AES);
        }
        vistaConexion.mostrar(); // temporal
    }

    private void persistePuesto(String modo) {
        LlamaMappers.persistir(modo, this.puesto);
    }

    @Override
    public void conexionErronea(String mensaje) {
        vistaConexion.appendLogError(mensaje);
    }

    @Override
    public void conexionExitosa() {
        vistaPuesto.mostrar();
        vistaConexion.cerrar();
    }

    public String getId() {
        return puesto.getId();

    }

    @Override
    public String desencriptar(String mensajeEncriptado) {
        return criptografia.desencriptar(mensajeEncriptado, claveEncriptacion);
    }

    public void setModoEncriptacion(String modo) {
        this.modoEncriptacion = modo;
        if (modo.equals(AdminComunicaServerP.AES)) {
            criptografia = FactoryCriptografia.getCifrador(ICriptografia.AES);
        } else if (modo.equals(AdminComunicaServerP.CHACHA20)) {
            criptografia = FactoryCriptografia.getCifrador(ICriptografia.CHACHA20);
        }
        LlamaMappers.persistirConfig(this.modoPersistencia, this.modoEncriptacion, this.claveEncriptacion);
    }

    public void setModoPersistencia(String modo) {
        this.modoPersistencia = modo;
        LlamaMappers.persistirConfig(this.modoPersistencia, this.modoEncriptacion, this.claveEncriptacion);
    }

}
