package server.manejadores;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public abstract class ManejadorDeNodos implements Runnable{ //Aplica template method
    protected Socket socket;
    protected DataOutputStream out;
    protected DataInputStream in;

    @Override
    public void run() {
        while (!socket.isClosed()){
            comunicacion();
        }
    }
    
    public abstract void comunicacion();

}
