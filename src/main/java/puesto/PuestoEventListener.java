package puesto;

public interface PuestoEventListener {

    public void eventoCantidadEnEspera(int cantEspera);
    public String desencriptar(String dniEncriptado);
    public void setClaveEncriptacion(String clave);
    public void setMetodoEncriptacion(String modo);
    public void setMetodoPersistencia(String modo);
    public void desconexionForzada();
}
