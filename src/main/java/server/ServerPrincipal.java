package server;

import java.io.IOException;

import server.manejadores.IManejaServidores;

public class ServerPrincipal extends ServerState{

    @Override
    public void switchServer() { //Este metodo lo ejecuta el admin.
        try {
            server.getServerSocket().close();
            Thread.sleep(IManejaServidores.TIMEOUT_CAIDA_MS); // 8 segundos
            server.abreConexion();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

/*   
    @Override
    public void hearthbeat(DataInputStream in, DataOutputStream out) {}
    @Override
    public void comunicaGestor(GestorID gestorID, DataInputStream in, DataOutputStream out) {}
    @Override
    public void comunicaTurnoEspera(Turno turno, DataInputStream in, DataOutputStream out) {}
    @Override
    public void comunicaListaTurnosEspera(ListaTurnos turnos, DataInputStream in, DataOutputStream out) {}
 */ 
}
