package puesto;

import java.io.IOException;

import shared.cliente.Cliente;
import shared.cliente.ClienteDniInvalidoException;
import shared.cliente.ClienteDniVacioException;
import shared.conexion_server.ComunicaServer;
import shared.turno.Turno;


public class PuestoComunicaServer extends ComunicaServer implements Runnable{
    public static final String ATIENDE = "#SIGUIENTE#", RENOTIFICA = "#ACTUAL#";
    private PuestoEventListener escuchadorDeEventos;

    public void setEscuchadorDeEventos(PuestoEventListener escuchadorDeEventos) {
        this.escuchadorDeEventos = escuchadorDeEventos;
    }

    public Turno atiendeSiguiente(String idPuesto){
        Turno turnoEnAtencion;
        Cliente cliente;
        try {
            out.writeUTF(ATIENDE);
            cliente = new Cliente(in.readUTF()); // TODO : desencriptar
            turnoEnAtencion = new Turno();
            turnoEnAtencion.setCliente(cliente);
            turnoEnAtencion.atender(idPuesto);
            return turnoEnAtencion;
        } catch (IOException e) {
            // TODO : INFORMAR AL ADMIN (Server-side)
            e.printStackTrace();
        } catch (ClienteDniVacioException e) {
        } catch (ClienteDniInvalidoException e) {
        }
        return null;
    }

    public boolean reNotifica(){
        boolean notifica = false;
        try {
            out.writeUTF(RENOTIFICA);
            notifica = Boolean.parseBoolean(in.readUTF());
        } catch (IOException e) {
            // TODO : INFORMAR AL ADMIN (Server-side)
            e.printStackTrace();
        }
        return notifica;
    
    }

    @Override
    public void run() {
        String cantidadEnEspera;
        while(!getSocket().isClosed()){
            try {
                cantidadEnEspera = in.readUTF();
                escuchadorDeEventos.eventoCantidadEnEspera(Integer.parseInt(cantidadEnEspera));
            } catch (IOException e) {
                // TODO : INFORMAR AL ADMIN (Server-side)
                e.printStackTrace();
            }
        }
    }

    public PuestoEventListener getEscuchadorDeEventos() {
        return escuchadorDeEventos;
    }


}
