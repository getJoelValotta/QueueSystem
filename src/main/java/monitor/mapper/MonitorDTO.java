package monitor.mapper;

import java.util.ArrayList;
import java.util.List;

import shared.turno.mapper.TurnoDTO;

/**
 * DTO del Monitor. Persiste su id, la capacidad de la lista de llamados
 * ({@code size}) y los turnos llamados (del mas reciente al mas viejo).
 */
public class MonitorDTO {

    private String id;
    private int size;
    private List<TurnoDTO> llamados = new ArrayList<>();

    public MonitorDTO() {
    }

    public MonitorDTO(String id, int size, List<TurnoDTO> llamados) {
        this.id = id;
        this.size = size;
        this.llamados = llamados;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<TurnoDTO> getLlamados() {
        return llamados;
    }

    public void setLlamados(List<TurnoDTO> llamados) {
        this.llamados = llamados;
    }
}
