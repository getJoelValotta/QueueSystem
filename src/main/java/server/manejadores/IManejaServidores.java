package server.manejadores;

import java.net.Socket;

import server.ListaTurnos;
import server.id.GestorID;
import shared.turno.Turno;

public interface IManejaServidores extends Runnable {

    public void setSocket(Socket socket);

    public void comunicaGestor(GestorID gestorID);

    public void comunicaTurnoEspera(Turno turno);

    public void comunicaListaTurnosEspera(ListaTurnos turnos);


}
