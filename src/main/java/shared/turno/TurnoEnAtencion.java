package shared.turno;

public class TurnoEnAtencion extends TurnoState{
    private String idPuesto;
    private int cantLlamados;

    public TurnoEnAtencion(Turno turno, String idPuesto, int cantLlamados) {
        super(turno);
        this.idPuesto = idPuesto;
        this.cantLlamados = cantLlamados;
    }

    @Override
    public void llamar() {
        if (cantLlamados < 3){
            cantLlamados += 1;
        }
        else{
            turno.setEstado(new TurnoAbandonado(turno, idPuesto));
        }
    }

    

    @Override
    public void atender(String idPuesto) { // Si lo usa el server, atender significa que pasa de espera a en atencion, si lo usa el puesto pasa de en atencion a atendido.

    }

    @Override
    public String getIdPuesto() {
        return idPuesto;
    }

    @Override
    public int getCantLlamados(){
        return cantLlamados;
    }

    @Override
    public boolean estaEnAtencion() {
        return true;
    }
    
}
