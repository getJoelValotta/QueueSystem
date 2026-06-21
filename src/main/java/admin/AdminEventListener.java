package admin;

public interface AdminEventListener {
    public void muestraLog (String msg, String server);
    public void cambiarEstado(String server, boolean estado);
}
