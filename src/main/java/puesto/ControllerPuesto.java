package puesto;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import shared.VistasUtils;
import shared.conexion_server.ComunicaServer;
import shared.conexion_server.ConexionGUI;
import shared.conexion_server.ConexionListener;


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
            case PuestoGUI.LLAMAR:
                vistaPuesto.limpiarClienteActual();
                // Puesto envia el llamado correspondiente
                break;
            case PuestoGUI.RENOTIFICAR:
                // Puesto envia el llamado correspondiente
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
        puesto.notifyAll(); //ahre me molestaba el warning y puse algo random xdxd
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
