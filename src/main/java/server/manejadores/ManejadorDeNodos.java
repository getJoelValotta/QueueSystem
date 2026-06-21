    package server.manejadores;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public abstract class ManejadorDeNodos implements Runnable{ //Aplica template method
    protected ManejadorEventListener controllerServer;
    protected Socket socket;
    protected DataOutputStream out;
    protected DataInputStream in;
    protected String id;

    public ManejadorDeNodos(ManejadorEventListener controllerServer, String id) {
        this.controllerServer = controllerServer;
        this.id = id;
    }

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
    
    public String getId(){
        return this.id;
    }

    public void setId(String id){
        this.id = id;
    }

    public abstract void comunicacion();

    public Socket getSocket(){
        return socket;
    }
}
