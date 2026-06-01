package shared.turno;

public class TurnoAbandonado extends TurnoState{
    private String idPuesto;

    public TurnoAbandonado(Turno turno, String idPuesto) {
        super(turno);
        this.idPuesto = idPuesto;
    }

    @Override
    public void llamar() {
    }

    @Override
    public void atender(String idPuesto) {
    }

    public void setIdPuesto(String idPuesto) {
        this.idPuesto = idPuesto;
    }

    public String getIdPuesto() {
        return idPuesto;
    }

    @Override
    public boolean estaAbandonado() {
        return true;
    }

}
