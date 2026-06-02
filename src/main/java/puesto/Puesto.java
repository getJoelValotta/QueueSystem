package puesto;

import shared.turno.Turno;

public class Puesto{

    private String id;
    private Turno turno;
    
    public Puesto(String id, Turno turno) {
        this.id = id;
        this.turno = turno;
    }

    public Puesto() {
        this.id = null;
        this.turno = null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Turno getTurno() {
        return turno;
    }

    public void setTurno(Turno turno) {
        this.turno = turno;
    }

    
}
