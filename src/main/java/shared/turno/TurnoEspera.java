package shared.turno;

public class TurnoEspera extends TurnoState{

    public TurnoEspera(Turno turno) {
        super(turno);
    }

    @Override
    public void llamar() { // Si esta en Espera no puede llamar.
    }

    @Override
    public void atender(String idPuesto) {
        this.turno.setEstado(new TurnoEnAtencion(turno, idPuesto, 1));
    }

    @Override
    public boolean estaEnEspera() {
        return true;
    }

}
