package server.manejadores;

import java.io.IOException;

import shared.turno.Turno;

public class ManejaPuesto extends ManejadorDeNodos{

    public ManejaPuesto(ManejadorEventListener controllerServer, String id) {
        super(controllerServer, id);
        //TODO Auto-generated constructor stub
    }

    @Override
    public void comunicacion() {
        try {
            String mensaje = in.readUTF(); // bloquea hasta recibir algo
            if (mensaje.equals("ATIENDE")){
                Turno turno = controllerServer.llamaSiguienteTurno(this.id);
                if (turno != null){
                    out.writeUTF(String.valueOf(turno.getCliente().getDni()));
                } else {
                    out.writeUTF("LISTA_VACIA");
                }
            }
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
