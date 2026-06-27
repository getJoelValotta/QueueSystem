package puesto.mapper;

import shared.turno.mapper.TurnoDTO;

/**
 * DTO del Puesto. Persiste su id y el turno que esta atendiendo
 * ({@code null} cuando no atiende a nadie).
 */
public class PuestoDTO {

    private String id;
    private TurnoDTO turno;

    public PuestoDTO() {
    }

    public PuestoDTO(String id, TurnoDTO turno) {
        this.id = id;
        this.turno = turno;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TurnoDTO getTurno() {
        return turno;
    }

    public void setTurno(TurnoDTO turno) {
        this.turno = turno;
    }
}
