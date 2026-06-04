package server.manejadores;

import java.net.Socket;

import server.ListaTurnos;
import server.id.GestorID;
import shared.turno.Turno;

public interface IManejaServidores extends Runnable {
    public static final int INTERVALO_HB_MS = 2000;
    public static final int TIMEOUT_CAIDA_MS = 8000;
    public static final String HBIN = "#PONG#", HBOUT = "#PING#", GESTOR = "#GESTOR#", TURNO_ESPERA = "#ESPERA#",  TURNO_ATENCION= "#ATENCION#";

    public void setSocket(Socket socket);

    public void comunicaGestor(GestorID gestorID);

    public void comunicaTurno(Turno turno, String idTipo);

    public void comunicaListaTurnos(ListaTurnos turnos, String idTipo);

}
