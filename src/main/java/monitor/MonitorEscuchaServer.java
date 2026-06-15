package monitor;

import java.io.IOException;

import shared.cliente.Cliente;
import shared.cliente.ClienteDniInvalidoException;
import shared.cliente.ClienteDniVacioException;
import shared.conexion_server.ComunicaServer;
import shared.turno.*;

public class MonitorEscuchaServer extends ComunicaServer implements Runnable{
    public static final String LLAMA = "#LLAMA#", RENOTIFICA = "#RENOTIFICA#";
    private MonitorEventListener escuchadorDeEventos;


    public void setEscuchadorDeEventos(MonitorEventListener escuchadorDeEventos) {
        this.escuchadorDeEventos = escuchadorDeEventos;
    }

    @Override
    public void run() {
        while (!getSocket().isClosed()) {
            try {
                String accion = in.readUTF();
                String dniRecibido = in.readUTF(); // TODO : Desencriptar
                String puesto = in.readUTF();
                Cliente cliente = new Cliente(dniRecibido);
                Turno turno = new Turno();
                turno.setCliente(cliente);
                TurnoEnAtencion turnoEnAtencionState = new TurnoEnAtencion(turno, puesto,1);
                turno.setEstado(turnoEnAtencionState);
                switch (accion){
                    case LLAMA:
                        escuchadorDeEventos.eventoRecibeLlamado(turno);
                    break;
                    case RENOTIFICA:
                        escuchadorDeEventos.eventoRenotificaLlamado(turno);
                    break;
                }
                escuchadorDeEventos.eventoRecibeLlamado(turno);
            } catch (IOException e) {
                // TODO : Informar al Admin (Server-Side)
                e.printStackTrace();
            } catch (ClienteDniVacioException | ClienteDniInvalidoException e) {
            }
        }
    }
    
    

}
