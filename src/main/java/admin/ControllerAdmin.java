package admin;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import shared.VistasUtils;

public class ControllerAdmin implements AdminEventListener, ActionListener {
    public static final String PRINCIPAL = "#PRINCIPAL#", RESPALDO = "#RESPALDO#", DESCONECTA = "#DESCONECTA#";
    AdminGUI vistaAdmin;
    Admin admin;
    AdminComunicaServerP comunicaServerPrincipal;
    AdminComunicaServerR comunicaServerRespaldo;
    String claveSimetrica;

    public ControllerAdmin(AdminGUI vistaAdmin, AdminComunicaServerP comunicaServerPrincipal,
            AdminComunicaServerR comunicaServerRespaldo) {
        this.vistaAdmin = vistaAdmin;
        this.comunicaServerPrincipal = comunicaServerPrincipal;
        this.comunicaServerRespaldo = comunicaServerRespaldo;
        this.vistaAdmin.setActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case AdminGUI.XML:
                VistasUtils.ejecutarNoBloqueante(() -> {
                    comunicaServerPrincipal.enviaTipoPersistencia(AdminComunicaServerP.XML);
                });
                admin.setMetodoPersistencia("XML");
                break;

            case AdminGUI.JSON:
                VistasUtils.ejecutarNoBloqueante(() -> {
                    comunicaServerPrincipal.enviaTipoPersistencia(AdminComunicaServerP.JSON);
                });
                admin.setMetodoPersistencia("JSON");
                break;

            case AdminGUI.TXT:
                VistasUtils.ejecutarNoBloqueante(() -> {
                    comunicaServerPrincipal.enviaTipoPersistencia(AdminComunicaServerP.TXT);
                });
                admin.setMetodoPersistencia("TXT");
                break;

            case AdminGUI.AES:
                VistasUtils.ejecutarNoBloqueante(() -> {
                    System.out.println("Estoy aes");
                    comunicaServerPrincipal.enviaTipoEncriptacion(AdminComunicaServerP.AES);
                });
                admin.setMetodoEncriptacion("AES");
                break;

            case AdminGUI.CHACHA20:
                VistasUtils.ejecutarNoBloqueante(() -> {
                    comunicaServerPrincipal.enviaTipoEncriptacion(AdminComunicaServerP.CHACHA20);
                });
                admin.setMetodoEncriptacion("CHACHA20");
                break;

            case AdminGUI.ENVIAR_CLAVE:
                VistasUtils.ejecutarNoBloqueante(() -> {
                    comunicaServerPrincipal.enviaNuevaClave(vistaAdmin.getClaveEncriptacion());
                });
                break;
        }
        persisteConfig();
    }

    public void iniciaAdmin() {

        try {
            this.admin = cargaConfig();
        } catch (RuntimeException e) {
            this.admin = new Admin("TXT", "MD5");
        }
        vistaAdmin.mostrar();
        new Thread(comunicaServerPrincipal).start();
        new Thread(comunicaServerRespaldo).start();
        persisteConfig();

    }

    public void muestraLog(String msg, String tipoEvento) {
        switch (tipoEvento) {
            case AdminComunicaServerP.EVENTO_PRINCIPAL:
                vistaAdmin.logEventoPrincipal(msg);
                break;
            case AdminComunicaServerP.BIEN_PRINCIPAL:
                vistaAdmin.logBienPrincipal(msg);
                break;
            case AdminComunicaServerP.MAL_PRINCIPAL:
                vistaAdmin.logMalPrincipal(msg);
                break;
            case AdminComunicaServerP.EVENTO_RESPALDO:
                vistaAdmin.logEventoRespaldo(msg);
                break;
        }
    }

    public void cambiarEstado(String server, boolean estado) {
        if (server.equals(PRINCIPAL)) {
            vistaAdmin.setEstadoPrincipal(estado);
        } else if (server.equals(RESPALDO)) {
            vistaAdmin.setEstadoRespaldo(estado);
        }
    }

    private Admin cargaConfig() throws RuntimeException {
        return LlamaMapperAdmin.recupera();
    }

    private void persisteConfig() {
        LlamaMapperAdmin.persistir(admin);
    }

}
