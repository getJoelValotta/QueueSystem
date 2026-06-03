package server.manejadores;

import java.io.IOException;

public class ManejaPuesto extends ManejadorDeNodos{

    @Override
    public void comunicacion() {
        try {
            String mensaje = in.readUTF(); // bloquea hasta recibir algo
        // procesar mensaje
        } catch (IOException e) {
            try {
                socket.close();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }

}
