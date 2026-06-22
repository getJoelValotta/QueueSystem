package puesto;

public interface PuestoEventListener {

    public void eventoCantidadEnEspera(int cantEspera);
    public String desencriptar(String dniEncriptado);
    public void setClaveEncriptacion(String clave);
    public void setModoEncriptacion(String modo);
    public void setModoPersistencia(String modo);
}
