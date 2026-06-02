package totem;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import shared.VistasUtils;
import shared.cliente.Cliente;
import shared.cliente.ClienteDniInvalidoException;
import shared.cliente.ClienteDniVacioException;
import shared.conexion_server.ComunicaServer;
import shared.conexion_server.ConexionGUI;
import shared.conexion_server.ConexionListener;

public class ControllerTotem implements ActionListener, ConexionListener{
    private ConexionGUI vistaConexion;
    private TotemGUI vistaTotem;
    private Totem totem;
    private TotemComunicaServer comunicaServer;

    public static int idx = 1;

    public ControllerTotem(ConexionGUI vistaConexion, TotemGUI vistaTotem, TotemComunicaServer comunicaServer) {
        this.vistaConexion = vistaConexion;
        this.vistaTotem = vistaTotem;
        this.vistaConexion.setActionListener(this);
        this.vistaTotem.setActionListener(this);
        this.comunicaServer = comunicaServer;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        switch (e.getActionCommand()){
            case ConexionGUI.CONECTAR:
                VistasUtils.ejecutarNoBloqueante(() ->
                    comunicaServer.conectaServidor(vistaConexion.getIP(), Integer.parseInt(vistaConexion.getPuerto()), ComunicaServer.TOTEM)
                );
                // iniciaTotem();
                break;
            case TotemGUI.REGISTRAR:
                try {
                    totem.setCliente(new Cliente(vistaTotem.getDNI()));
                    VistasUtils.ejecutarNoBloqueante(() -> {
                        boolean validacion = comunicaServer.enviarDNI(totem.getCliente().getDni());
                        if (validacion == false){
                            vistaTotem.setGuiaError("Usted se encuentra ya registrado.");
                        }
                    });
                } catch (ClienteDniVacioException e1) { //Nunca se lanzaran ya que la vista controla esto antes.
                } catch (ClienteDniInvalidoException e1) {}
                break;
        }
    }

    public void iniciaTotem(){
        // TODO : persistencia + logica de asignacion de ids estilo tot_001... si no tiene persistencia solicita a server primer id y luego persiste.
        this.totem = new Totem(String.valueOf(idx), null);
        idx = idx+1;
        // si id es null o no hay archivo persistido para el totem, invocar ComunicaServer.solicitaID();
        vistaConexion.setVisible(true);
    }

    @Override
    public void conexionErronea(String mensaje) {
        vistaConexion.appendLogError(mensaje);
    }

    @Override
    public void conexionExitosa() {
        vistaTotem.mostrar();
        vistaConexion.cerrar();
    }

    @Override
    public void mensajeError(String mensaje) {
        VistasUtils.enEDT(() -> vistaTotem.setGuiaError(mensaje));
    }

}
