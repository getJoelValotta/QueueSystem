package monitor;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

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
        Socket socketActual = getSocket();
        while (!socketActual.isClosed()) {
            try {
                System.out.println("LLEGUE AL IN");
                String accion = in.readUTF();
                String puesto = in.readUTF();
                String dniEncriptado = in.readUTF(); // TODO : Desencriptar
                String dniRecibido = escuchadorDeEventos.desencriptar(dniEncriptado);
                System.out.println("ACCION = " + accion + ", DNI = " + dniRecibido + ", PUESTO = " + puesto);
                Cliente cliente = new Cliente(dniRecibido);
                Turno turno = new Turno();
                turno.setCliente(cliente);
                TurnoEnAtencion turnoEnAtencionState = new TurnoEnAtencion(turno, puesto,1);
                turno.setEstado(turnoEnAtencionState);
                switch (accion){
                    case LLAMA:
                        System.out.println("LLAMANDO A TURNO...");
                        escuchadorDeEventos.eventoRecibeLlamado(turno);
                        System.out.println("LLAMADO RECIBIDO");
                    break;
                    case RENOTIFICA:
                        escuchadorDeEventos.eventoRenotificaLlamado(turno);
                    break;
                }
                escuchadorDeEventos.eventoRecibeLlamado(turno);
            } catch (SocketException e) {
                System.out.println("ENTRE SOCKET EXCEPTION");
                conectaServidor(IP, puerto, ComunicaServer.MONITOR);
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("entre ioexception");
                // TODO : Informar al Admin (Server-Side)
                e.printStackTrace();
            } catch (ClienteDniVacioException | ClienteDniInvalidoException e) {
            }
        }
    }

}
