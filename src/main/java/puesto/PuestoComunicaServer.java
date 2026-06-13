package puesto;

import java.io.DataOutputStream;
import java.io.DataInputStream;
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
    private Socket socketSimple;
    protected DataOutputStream outSimple;
    protected DataInputStream inSimple;

    public void setEscuchadorDeEventos(PuestoEventListener escuchadorDeEventos) {
        this.escuchadorDeEventos = escuchadorDeEventos;
    }

    public Turno atiendeSiguiente(String idPuesto) {
        Turno turnoEnAtencion;
        Cliente cliente;
        synchronized (mutex) {
            try {
                outSimple.writeUTF(ATIENDE);
                String dniRecibido = inSimple.readUTF();
                cliente = new Cliente(dniRecibido); // TODO : desencriptar
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
                outSimple.writeUTF(RENOTIFICA);
                notifica = Boolean.parseBoolean(inSimple.readUTF());
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
                //getSocket().setSoTimeout(2000);
                //synchronized (mutex) {
                    cantidadEnEspera = in.readUTF();
                //}
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

    @Override
    public void conectaServidor(String IP, int puerto, String nodo) {
        // TODO Auto-generated method stub
        try {
            super.conectaServidor(IP, puerto, nodo);
            out.writeUTF(PUESTO_COLA);
            this.socketSimple = new Socket(IP, puerto);
            this.outSimple = new DataOutputStream(socketSimple.getOutputStream());
            this.inSimple = new DataInputStream(socketSimple.getInputStream());
            outSimple.writeUTF(nodo);
            outSimple.writeUTF(PUESTO_LLAMADOS);
        } catch (java.net.ConnectException e) { // Excepcion que discierne si el puerto es incorrecto o el host esta indispuesto.
            escuchadorDeNodoFisico.conexionErronea("Puerto incorrecto o host indispuesto");
            e.printStackTrace();
        } catch (IOException e) { 
            escuchadorDeNodoFisico.conexionErronea("Error de protocolo de conexion");
            e.printStackTrace();
        }
    }

    @Override
    public void informaID(String id) {
        try {
            synchronized (mutex){
                outSimple.writeUTF(id);
            }
        } catch (IOException e) {
            // TODO Informar al ADMIN (Server-Side) y manejar retry.
            e.printStackTrace();
        }
    }

    @Override
    public String solicitaID() {
        String nuevaID = null;
        try {
            synchronized (mutex) {
                outSimple.writeUTF(ID);
                nuevaID = inSimple.readUTF();
            }
        } catch (IOException e) {
            // TODO Informar al ADMIN (Server-Side) y manejar retry.
            e.printStackTrace();
        }
        return nuevaID;
    }

}
