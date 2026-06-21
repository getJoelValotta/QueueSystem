package admin;

public class ControllerAdmin implements AdminEventListener {
    AdminGUI vistaAdmin;
    AdminComunicaServer comunicaServer;

    public ControllerAdmin(AdminGUI vistaAdmin, AdminComunicaServer comunicaServer){
        this.vistaAdmin = vistaAdmin;
        this.comunicaServer = comunicaServer;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()){
            case vistaAdmin.XML:
                comunicaServer.actualizaPersistencia(vistaAdmin.XML);
                break;
            
            case vistaAdmin.JSON:
                comunicaServer.actualizaPersistencia(vistaAdmin.JSON);
                break;
            
            case vistaAdmin.TXT:
                comunicaServer.actualizaPersistencia(vistaAdmin.TXT);
                break;
            
            case vistaAdmin.MD5:
                comunicaServer.actualizaEncriptacion(vistaAdmin.MD5);
                break;

            case vistaAdmin.SHA_2:
                comunicaServer.actualizaEncriptacion(vistaAdmin.SHA_2);
                break;
                
            }
    }


    public void muestraLog (String server, String msg){ 
        if (server.equals("PRINCIPAL")){
            vistaAdmin.muestraLogPrincipal(msg);
        } else if (server.equals("RESPALDO")){
            vistaAdmin.muestraLogRespaldo(msg);
        }
    }
}
