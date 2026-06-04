package puesto;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

import shared.cliente.Cliente;
import shared.cliente.ClienteDniInvalidoException;
import shared.cliente.ClienteDniVacioException;
import shared.conexion_server.ComunicaServer;
import shared.turno.Turno;

public class PuestoComunicaServer extends ComunicaServer implements Runnable {
    public static final String ATIENDE = "#SIGUIENTE#", RENOTIFICA = "#ACTUAL#";
    private PuestoEventListener escuchadorDeEventos;
    private Object mutex = new Object(); // Auxiliar para el manejo de zonas criticas de los in/out de los sockets.

    public void setEscuchadorDeEventos(PuestoEventListener escuchadorDeEventos) {
        this.escuchadorDeEventos = escuchadorDeEventos;
    }

    public Turno atiendeSiguiente(String idPuesto) {
        Turno turnoEnAtencion;
        Cliente cliente;
        synchronized (mutex) {
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
        }
        return null;
    }

    public boolean reNotifica() {
        boolean notifica = false;
        try {
            synchronized (mutex) {
                out.writeUTF(RENOTIFICA);
                notifica = Boolean.parseBoolean(in.readUTF());
            }
        } catch (IOException e) {
            // TODO : INFORMAR AL ADMIN (Server-side)
            e.printStackTrace();
        }
        return notifica;
    }

    // POSIBLE PROBLEMA CON ESTO: MIENTRAS UN PUESTO ATIENDE Y REALIZA UN OUT QUE
    // NECESITA RESPUESTA, ES POSIBLE QUE LLEGUE NUEVOS TURNOS POR PARTE DE UN TOTEM
    // Y POR ENDE NOTIFIQUE
    // Y DEJE UN "IN" PENDIENTE PARA ESTE PUESTO. SI EL PUESTO ATIENDE POR ENDE HACE
    // UN OUT Y NO LLEGA A RECIBIR UNA RESPUESTA, PUEDE RECIBIR UN DATO ERRONEO.
    // CHEQUEAR LUEGO
    @Override
    public void run() {
        String cantidadEnEspera;
        while (!getSocket().isClosed()) {
            try {
                getSocket().setSoTimeout(2000);
                synchronized (mutex) {
                    cantidadEnEspera = in.readUTF();
                }
                escuchadorDeEventos.eventoCantidadEnEspera(Integer.parseInt(cantidadEnEspera));
            } catch (SocketException e) {
            } catch (IOException e) {
                // TODO : INFORMAR AL ADMIN (Server-side)
                try {
                    getSocket().close();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                e.printStackTrace();
            }
        }
    }

    public PuestoEventListener getEscuchadorDeEventos() {
        return escuchadorDeEventos;
    }

}
