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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((turno == null) ? 0 : turno.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TurnoState other = (TurnoState) obj;
        if (estaEnEspera() & !other.estaEnEspera())
            return false;
        if (estaEnAtencion() & !other.estaEnAtencion())
            return false;
        if (estaAtendido() & !other.estaAtendido())
            return false;
        if (estaAbandonado() & !other.estaAbandonado())
            return false;
        return true;
    }
    
}
