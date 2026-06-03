package server.manejadores;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public abstract class ManejadorDeNodos implements Runnable{ //Aplica template method
    protected Socket socket;
    protected DataOutputStream out;
    protected DataInputStream in;

    public void setSocket(Socket socket){
        this.socket = socket;
        try {
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @Override
    public void run() {
        while (!socket.isClosed()){
            comunicacion();
        }
    }
    
    public abstract void comunicacion();
}
