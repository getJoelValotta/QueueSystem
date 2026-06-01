package shared.turno;

public abstract class TurnoState {
    protected Turno turno;

    public TurnoState(Turno turno){
        this.turno = turno;
    }

    public String getIdPuesto(){
        return null;
    }

    public int getCantLlamados(){
        return -1;
    }

    public boolean estaEnEspera(){ 
        return false; 
    }

    public boolean estaEnAtencion(){ 
        return false; 
    }

    public boolean estaAtendido(){ 
        return false; 
    }

    public boolean estaAbandonado(){ 
        return false; 
    }

    public abstract void llamar();
    public abstract void atender(String idPuesto);
    
}
