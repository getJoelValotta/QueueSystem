package server.manejadores;

import shared.turno.Turno;

public interface ManejadorEventListener {

    public void recibeYPersisteGestor(String totem, String puesto, String monitor);
    public boolean recibeYPersisteTurno(String dni, String estado);
    public Turno llamaSiguienteTurno(String id);
    public void cambiaEstadoServer();
    public void serverDejaDeObservar(IControllerObserver suscriptor);
    public boolean actualizaTurnoRenotificado(String idPuesto);
    public void recibeTurnoEnRespaldo(Turno turno);

}
