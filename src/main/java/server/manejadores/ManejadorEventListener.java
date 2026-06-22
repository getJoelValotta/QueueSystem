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
    public void avisarAdmin (String msg, String tipoEvento);
    public String getClave();
    public String encriptar(String mensaje);
    public String desencriptar(String mensajeEncriptado);
    public void setModo(String modo);
    public void setClaveEncriptacion(String clave);
    public void setModoEncriptacion(String modoEncriptacion);

}
