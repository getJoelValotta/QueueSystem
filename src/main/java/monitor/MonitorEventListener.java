package monitor;

import shared.turno.Turno;

public interface MonitorEventListener {

    public void eventoRecibeLlamado(Turno turno);
    public void eventoRenotificaLlamado(Turno turno);
    public String encriptar(String mensaje);
    public String desencriptar(String mensajeEncriptado);
    public void desconexionForzada();
    public void setMetodoEncriptacion(String modo);
    public void setMetodoPersistencia(String modo);

}
