package totem;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import admin.AdminComunicaServerP;
import puesto.PuestoAjustesGUI;
import shared.VistasUtils;
import shared.cliente.Cliente;
import shared.cliente.ClienteDniInvalidoException;
import shared.cliente.ClienteDniVacioException;
import shared.conexion_server.ComunicaServer;
import shared.conexion_server.ConexionGUI;
import shared.conexion_server.ConexionListener;
import shared.criptografia.FactoryCriptografia;
import shared.criptografia.ICriptografia;

public class ControllerTotem implements ActionListener, ConexionListener, TotemEventListener {
    private ConexionGUI vistaConexion;
    private TotemGUI vistaTotem;
    private Totem totem;
    private TotemComunicaServer comunicaServer;
    private ICriptografia criptografia;
    private String claveSimetrica;
    private String metodoPersistencia = "txt";
    private String metodoEncriptacion;

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

        switch (e.getActionCommand()) {
            case ConexionGUI.CONECTAR:
                VistasUtils.ejecutarNoBloqueante(() -> {
                    comunicaServer.conectaServidorPrimeraVez(vistaConexion.getIP(),
                            Integer.parseInt(vistaConexion.getPuerto()), ComunicaServer.TOTEM,
                            vistaConexion.getClaveEncriptacion());

                    if (totem.getId() == null) {
                        String id = comunicaServer.solicitaID();
                        System.out.println("id = " + id);
                        totem.setId(id);
                    } else {
                        comunicaServer.informaID(totem.getId());
                    }
                });
                // iniciaTotem();
                break;
            case TotemGUI.REGISTRAR:
                try {
                    totem.setCliente(new Cliente(vistaTotem.getDNI()));
                    VistasUtils.ejecutarNoBloqueante(() -> {
                        String dni = String.valueOf(totem.getCliente().getDni());
                        String dniEncriptado = criptografia.encriptar(dni, claveSimetrica);
                        boolean validacion = comunicaServer.enviarDNI(dniEncriptado);
                        if (validacion) {
                            vistaTotem.setGuiaError("Usted ya se encuentra registrado.");
                        } else {
                            vistaTotem.setGuiaExito("DNI Ingresado");
                            vistaTotem.limpiaDNI();
                        }
                    });
                } catch (ClienteDniVacioException e1) { // Nunca se lanzaran ya que la vista controla esto antes.
                } catch (ClienteDniInvalidoException e1) {
                }
                break;
        }
    }

    public void iniciaTotem() {
        totem = new Totem();
        vistaConexion.mostrar(); // temporal
        //persistir();
    }

    @Override
    public void conexionErronea(String mensaje) {
        vistaConexion.appendLogError(mensaje);
    }

    @Override
    public void conexionExitosa() {
        new Thread(comunicaServer).start();
        vistaTotem.mostrar();
        vistaConexion.cerrar();
    }

    @Override
    public void mensajeError(String mensaje) {
        VistasUtils.enEDT(() -> vistaTotem.setGuiaError(mensaje));
    }

    public String getId() {
        return totem.getId();
    }


    public void setClaveEncriptacion(String clave) {
        this.claveSimetrica = clave;
        //persistir();
    }

    public String getMetodoEncriptacion() {
        return metodoEncriptacion;
    }

    public void setMetodoEncriptacion(String modo) {
        this.metodoEncriptacion = modo;
        if (modo.equals(PuestoAjustesGUI.AES)) {
            criptografia = FactoryCriptografia.getCifrador(ICriptografia.AES);
        } else if (modo.equals(PuestoAjustesGUI.CHACHA20)) {
            criptografia = FactoryCriptografia.getCifrador(ICriptografia.CHACHA20);
        }
        System.out.println("Encrip cambiada");
        //persistir();
    }

    public void setMetodoPersistencia(String modo) {
        this.metodoPersistencia = modo;
        //persistir();
    }

    public String getMetodoPersistencia() {
        return metodoPersistencia;
    }

    @Override
    public void desconexionForzada() {
        vistaTotem.cerrar();
        vistaConexion.mostrar();
    }

}
