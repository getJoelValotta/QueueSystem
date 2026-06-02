package shared.conexion_server;

public interface ConexionListener {

    public void conexionErronea(String mensaje);
    public void conexionExitosa();

}
