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
import shared.criptografia.ICriptografia;

import totem.persistencia.TotemConfigTXTMapper;

public class ControllerTotem implements ActionListener, ConexionListener, TotemEventListener {
    private ConexionGUI vistaConexion;
    private TotemGUI vistaTotem;
    private Totem totem;
    private TotemComunicaServer comunicaServer;
    private ICriptografia criptografia;
    private String claveSimetrica;

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
                        System.out.println("PIDO ID");
                        String id = comunicaServer.solicitaID();
                        System.out.println("id = " + id);
                        totem.setId(id);
                    } else {
                        System.out.println("INFORMO EL ID = " + totem.getId());
                        comunicaServer.informaID(totem.getId());
                    }
                });
                // iniciaTotem();
                break;
            case TotemGUI.REGISTRAR:
                try {
                    totem.setCliente(new Cliente(vistaTotem.getDNI()));
                    VistasUtils.ejecutarNoBloqueante(() -> {
                        boolean validacion = comunicaServer.enviarDNI(totem.getCliente().getDni());
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
        try {
            Thread.sleep(100);
        } catch (Exception e1) {
            System.out.println("Error sleep: " + e1.getMessage());
        }
        persistir();
    }

    public void iniciaTotem() {
        try {
            this.totem = recuperar();
            System.out.println("Totem recuperado con ID: " + totem.getId());
        } catch (RuntimeException e) {
            System.out.println("Error al recuperar el totem, se creará un nuevo totem: " + e.getMessage());
            this.totem = new Totem();
        }
        vistaConexion.mostrar(); // temporal
        persistir();
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

    public String getId() {
        return totem.getId();
    }

    private void persistir() {
        LlamaMappersTotem.persistir(totem);
    }

    private Totem recuperar() throws RuntimeException {
        return LlamaMappersTotem.recuperar();
    }

}
