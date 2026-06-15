package monitor;

import shared.turno.Turno;

public interface MonitorEventListener {

    public void eventoRecibeLlamado(Turno turno);
    public void eventoRenotificaLlamado(Turno turno);

}
