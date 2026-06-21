package admin;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import shared.VistasUtils;
import shared.conexion_server.ComunicaServer;
import shared.conexion_server.ConexionListener;

public class ControllerAdmin implements AdminEventListener, ActionListener, ConexionListener{
    public static final String PRINCIPAL = "#PRINCIPAL#", RESPALDO = "#RESPALDO#";
    AdminGUI vistaAdmin;
    Admin admin;
    AdminComunicaServer comunicaServer;

    public ControllerAdmin(AdminGUI vistaAdmin, AdminComunicaServer comunicaServer){
        this.vistaAdmin = vistaAdmin;
        this.comunicaServer = comunicaServer;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()){
            case AdminGUI.XML:
                VistasUtils.ejecutarNoBloqueante(() ->{
                    comunicaServer.enviaTipoPersistencia(AdminComunicaServer.XML);
                });
                break;
            
            case AdminGUI.JSON:
                VistasUtils.ejecutarNoBloqueante(() ->{
                    comunicaServer.enviaTipoPersistencia(AdminComunicaServer.JSON);
                });
                break;
            
            case AdminGUI.TXT:
                VistasUtils.ejecutarNoBloqueante(() ->{
                    comunicaServer.enviaTipoPersistencia(AdminComunicaServer.TXT);
                });
                break;
            
            case AdminGUI.MD5:
                VistasUtils.ejecutarNoBloqueante(() ->{
                    comunicaServer.enviaTipoEncriptacion(AdminComunicaServer.MD5);
                });
                break;

            case AdminGUI.SHA_2:
                VistasUtils.ejecutarNoBloqueante(() ->{
                    comunicaServer.enviaTipoEncriptacion(AdminComunicaServer.SHA_2);
                });
                break;
            }
    }


    public void iniciaAdmin(){
        // Carga el Admin por persistencia. Primera vez hardcodeado con un archivo con formato a definir. si cambia el formato
        admin = new Admin("TXT","MD5");
        comunicaServer.conectaServidor("localhost", 1337, ComunicaServer.ADMIN);
        new Thread(comunicaServer).start();
    }


    public void muestraLog (String msg, String server){ 
        if (server.equals(PRINCIPAL)){
            vistaAdmin.logServerPrincipal(msg);
        } else if (server.equals(RESPALDO)){
            vistaAdmin.logServerRespaldo(msg);
        }
    }

    @Override
    public void conexionErronea(String mensaje) {
        comunicaServer.conectaServidor("localhost", 1337, ComunicaServer.ADMIN);
    }

    @Override
    public void conexionExitosa() {
        vistaAdmin.mostrar();
    }

    @Override
    public String getId() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getId'");
    }

    public void cambiarEstado(String server, boolean estado){
        if (server.equals(PRINCIPAL)){
            vistaAdmin.setEstadoPrincipal(estado);
        } else if (server.equals(RESPALDO)){
            vistaAdmin.setEstadoRespaldo(estado);
        }    
    }
}
