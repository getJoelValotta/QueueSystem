package server.manejadores;

public interface ManejadorEventListener {

    public void recibeYPersiste(String totem, String puesto, String monitor);
    public void recibeYPersisteTurno(String dni, String estado);

}
