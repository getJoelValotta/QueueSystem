package puesto;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import shared.VistasUtils;
import shared.conexion_server.ComunicaServer;
import shared.conexion_server.ConexionGUI;
import shared.conexion_server.ConexionListener;
import shared.turno.Turno;


public class ControllerPuesto implements ActionListener, ConexionListener, PuestoEventListener{
    private ConexionGUI vistaConexion;
    private PuestoGUI vistaPuesto;
    private Puesto puesto;
    private PuestoComunicaServer comunicaServer;

    public ControllerPuesto(ConexionGUI vistaConexion, PuestoGUI vistaPuesto, PuestoComunicaServer comunicaServer){
        this.vistaConexion = vistaConexion;
        this.vistaPuesto = vistaPuesto;
        this.vistaConexion.setActionListener(this);
        this.vistaPuesto.setActionListener(this);
        this.comunicaServer = comunicaServer;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()){
            case ConexionGUI.CONECTAR:
                VistasUtils.ejecutarNoBloqueante(() ->
                    comunicaServer.conectaServidor(vistaConexion.getIP(), Integer.parseInt(vistaConexion.getPuerto()), ComunicaServer.PUESTO)
                );
                break;
            case PuestoGUI.LLAMAR: //Aca comienza a atender a un cliente y es llamado por primera vez.
                vistaPuesto.limpiarClienteActual();
                VistasUtils.ejecutarNoBloqueante(() ->
                    puesto.setTurno(comunicaServer.atiendeSiguiente(puesto.getId())
                ));
                vistaPuesto.setClienteActual(puesto.getTurno().getCliente().getDni());
                vistaPuesto.habilitaRenotificar();
                break;
            case PuestoGUI.RENOTIFICAR:
                Turno turnoActual = puesto.getTurno();
                int auxCantLlamados = turnoActual.getCantLlamados();
                if (turnoActual.estaEnAtencion() & auxCantLlamados < 3)
                    turnoActual.llamar();
                    if (auxCantLlamados + 1 == 3)
                        vistaPuesto.cambiaTextRenotificarAabandonado();
                else{
                    turnoActual.llamar();
                    vistaPuesto.cambiaTextAbandonadoARenotificar();
                    vistaPuesto.inhabilitaRenotificar();
                }
                break;
            }
        }
        
        @Override
        public void eventoCantidadEnEspera(int cantEspera) {
            vistaPuesto.setCantClientes(cantEspera);
        }

    public void iniciaPuesto(){
        // Carga el puesto por persistencia. si no hay archivo entonces le pido al server la id por primera vez: pue_001
        puesto = new Puesto();
        vistaConexion.mostrar(); //temporal
        // Agregar que si el estado del turno tiene exactamente 3 llamados, cambie el boton renotificar a "Marcar como abandonado"
    }

    @Override
    public void conexionErronea(String mensaje) {
        vistaConexion.appendLogError(mensaje);
    }

    @Override
    public void conexionExitosa() {
        vistaPuesto.mostrar();
        vistaConexion.cerrar();
        new Thread(comunicaServer).start();
    }

    // No hay Validacion de datos, por lo que no hago nada con este metodo.
    @Override
    public void mensajeError(String mensaje) {
    }


}
