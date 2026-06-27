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
import shared.persistencia.factory.FabricaPersistencia;
import shared.persistencia.factory.IFactoryPersistenciaArchivos;
import shared.turno.Turno;
import puesto.mapper.PuestoDTO;
import puesto.mapper.PuestoMapper;

public class ControllerPuesto implements ActionListener, ConexionListener, PuestoEventListener {
    private ConexionGUI vistaConexion;
    private PuestoGUI vistaPuesto;
    private Puesto puesto;
    private PuestoComunicaServer comunicaServer;
    private String modoPersistencia = "txt";
    private ICriptografia criptografia;
    private String claveEncriptacion, modoEncriptacion;
    private IFactoryPersistenciaArchivos factoryPersistencia;
    private PuestoMapper puestoMapper;

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
                    persistirPuesto(); // persiste el id asignado
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
                        persistirPuesto(); // nuevo turno en atencion
                    }
                    vistaPuesto.setClienteActual(puesto.getTurno().getCliente().getDni()); // Esto antes estaba fuera de
                                                                                           // ejecutarNoBloqueante y se
                                                                                           // rompia porque se ejecutaba
                                                                                           // antes de esperar a la
                                                                                           // comunicacion.
                    vistaPuesto.bloquearRenotificarTimer(1);
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
                            persistirPuesto(); // cambio el estado del turno (renotificacion)
                            vistaPuesto.setMensajeExito();
                            vistaPuesto.bloquearRenotificarTimer(auxCantLlamados);
                            if (auxCantLlamados + 1 == 3)
                                vistaPuesto.cambiaTextRenotificarAabandonado();
                        } else {
                            vistaPuesto.setMensajeError();
                        }
                    });
                else { // En el caso de que este en el ultimo llamado (3), se cambia el boton de
                       // renotificar para marcarlo como abandono.
                    VistasUtils.ejecutarNoBloqueante(() -> {
                        System.out.println("Supere la cantidad de llamados voy a avisarle al renotifica.");
                        if (comunicaServer.reNotifica()) {
                            turnoActual.llamar();
                            persistirPuesto(); // el turno paso a abandonado
                            vistaPuesto.setMensajeExito();
                            vistaPuesto.cambiaTextAbandonadoARenotificar();
                            vistaPuesto.inhabilitaRenotificar();
                        } else {
                            vistaPuesto.setMensajeError();
                        }
                    });
                }
                break;
            case PuestoGUI.AJUSTES:
                System.out.println("ENTRE A AJUSTES!!!");
                System.out.println("modoEncriptacion = " + modoEncriptacion);
                System.out.println("AES = " + PuestoAjustesGUI.AES);
                System.out.println("CHACHA = " + PuestoAjustesGUI.CHACHA20);
                vistaPuesto.hookAjustes(modoEncriptacion, modoPersistencia);
                break;
            case PuestoAjustesGUI.ENVIAR_CLAVE:
                System.out.println(vistaPuesto.getClaveEncriptacion());
                comunicaServer.enviaClave(vistaPuesto.getClaveEncriptacion());
                //vistaPuesto.cerrar();
                break;
            case PuestoAjustesGUI.AES:
                VistasUtils.ejecutarNoBloqueante(() -> {
                    comunicaServer.enviaMetodoEncriptacion(PuestoAjustesGUI.AES);
                });
                break;
            case PuestoAjustesGUI.CHACHA20:
                VistasUtils.ejecutarNoBloqueante(() -> {
                    System.out.println("ENTRE ACA!!!");
                    comunicaServer.enviaMetodoEncriptacion(PuestoAjustesGUI.CHACHA20);
                });
                break;
            case PuestoAjustesGUI.TXT:
                VistasUtils.ejecutarNoBloqueante(() -> {
                    comunicaServer.enviaMetodoPersistencia(PuestoAjustesGUI.TXT);
                });
                break;
            case PuestoAjustesGUI.XML:
                VistasUtils.ejecutarNoBloqueante(() -> {
                    comunicaServer.enviaMetodoPersistencia(PuestoAjustesGUI.XML);
                });
                break;
            case PuestoAjustesGUI.JSON:
                VistasUtils.ejecutarNoBloqueante(() -> {
                    comunicaServer.enviaMetodoPersistencia(PuestoAjustesGUI.JSON);
                });
                break;
        }
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
    }

    public void iniciaPuesto() {
        puesto = new Puesto();
        cargarPuesto(); // restaura el estado persistido (id/turno) si existe
        if (puesto.getId() != null) {
            vistaPuesto.setNumPuesto(puesto.getId());
        }
        Turno turnoRestaurado = puesto.getTurno();
        if (turnoRestaurado != null && turnoRestaurado.estaEnAtencion() && turnoRestaurado.getCliente() != null) {
            vistaPuesto.setClienteActual(turnoRestaurado.getCliente().getDni());
        } else {
            vistaPuesto.inhabilitaRenotificar();
        }
        vistaConexion.mostrar(); //temporal
    }

    /** Autodetecta el formato persistido y restaura el Puesto si habia datos previos. */
    private void cargarPuesto() {
        factoryPersistencia = FabricaPersistencia.detectarOPara("puesto", modoPersistencia);
        puestoMapper = factoryPersistencia.fabricaPuestoMapper();
        try {
            PuestoDTO dto = puestoMapper.templateLeer();
            if (dto != null) {
                Puesto restaurado = puestoMapper.toDominio(dto);
                if (restaurado != null) {
                    puesto.setId(restaurado.getId());
                    puesto.setTurno(restaurado.getTurno());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Graba el estado actual del Puesto en el formato vigente. */
    private void persistirPuesto() {
        if (puestoMapper == null) {
            return;
        }
        try {
            puestoMapper.templateGrabar(puestoMapper.toDto(puesto));
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public void setMetodoEncriptacion(String modo) {
        this.modoEncriptacion = modo;
        System.out.println("MODO = " + modo);
        if (modo.equals(PuestoAjustesGUI.AES)) {
            criptografia = FactoryCriptografia.getCifrador(ICriptografia.AES);
        } else if (modo.equals(PuestoAjustesGUI.CHACHA20)) {
            criptografia = FactoryCriptografia.getCifrador(ICriptografia.CHACHA20);
        }
        System.out.println("Metodo de encriptacion cambiado a " + modo);
    }

    public void setMetodoPersistencia(String modo) {
        this.modoPersistencia = modo;
        // Cambio de formato: se reconstruye el mapper (Abstract Factory) y se persiste
        // el estado completo en el nuevo formato (los archivos previos no se borran).
        this.factoryPersistencia = FabricaPersistencia.para(modo);
        this.puestoMapper = factoryPersistencia.fabricaPuestoMapper();
        persistirPuesto();
        System.out.println("Metodo de persistencia cambiado a " + modo);
    }

   @Override
    public void desconexionForzada() {
        vistaPuesto.cerrar();
        vistaConexion.mostrar();
    }

    public String getModoEncriptacion() {
        return modoEncriptacion;
    }

}
