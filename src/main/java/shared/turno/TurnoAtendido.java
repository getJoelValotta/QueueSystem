package shared.turno;

public class TurnoAtendido extends TurnoState{
    private String idPuesto;

    public TurnoAtendido(Turno turno, String idPuesto) {
        super(turno);
        this.idPuesto = idPuesto;
    }

    @Override
    public void llamar() {
    }

    @Override
    public void atender(String idPuesto) {

    }

    public String getIdPuesto() {
        return idPuesto;
    }

    @Override
    public boolean estaAtendido() {
        return true;
    }

}
