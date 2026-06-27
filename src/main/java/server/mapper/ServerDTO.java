package server.mapper;

import java.util.ArrayList;
import java.util.List;

import shared.turno.mapper.TurnoDTO;

/**
 * DTO del Server. Representa el estado persistible del servidor:
 * las cuatro listas de turnos y los contadores del GestorID.
 */
public class ServerDTO {

    private List<TurnoDTO> enEspera = new ArrayList<>();
    private List<TurnoDTO> enAtencion = new ArrayList<>();
    private List<TurnoDTO> abandonados = new ArrayList<>();
    private List<TurnoDTO> atendidos = new ArrayList<>();

    private int contadorTotem;
    private int contadorPuesto;
    private int contadorMonitor;

    public ServerDTO() {
    }

    public List<TurnoDTO> getEnEspera() {
        return enEspera;
    }

    public void setEnEspera(List<TurnoDTO> enEspera) {
        this.enEspera = enEspera;
    }

    public List<TurnoDTO> getEnAtencion() {
        return enAtencion;
    }

    public void setEnAtencion(List<TurnoDTO> enAtencion) {
        this.enAtencion = enAtencion;
    }

    public List<TurnoDTO> getAbandonados() {
        return abandonados;
    }

    public void setAbandonados(List<TurnoDTO> abandonados) {
        this.abandonados = abandonados;
    }

    public List<TurnoDTO> getAtendidos() {
        return atendidos;
    }

    public void setAtendidos(List<TurnoDTO> atendidos) {
        this.atendidos = atendidos;
    }

    public int getContadorTotem() {
        return contadorTotem;
    }

    public void setContadorTotem(int contadorTotem) {
        this.contadorTotem = contadorTotem;
    }

    public int getContadorPuesto() {
        return contadorPuesto;
    }

    public void setContadorPuesto(int contadorPuesto) {
        this.contadorPuesto = contadorPuesto;
    }

    public int getContadorMonitor() {
        return contadorMonitor;
    }

    public void setContadorMonitor(int contadorMonitor) {
        this.contadorMonitor = contadorMonitor;
    }
}
