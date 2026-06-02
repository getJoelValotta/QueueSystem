package shared.turno;

import shared.cliente.Cliente;

public class Turno {

    private Cliente cliente;
    private TurnoState estado;

    public Turno(){
        this.estado = new TurnoEspera(this);
    }

    public void llamar(){
        this.estado.llamar();
    }

    public void atender(String idPuesto){
        this.estado.atender(idPuesto);
    }

    public String getIdPuesto() {
        return estado.getIdPuesto();
    }

    public int getCantLlamados(){
        return estado.getCantLlamados();
    }

    public boolean estaEnEspera(){
        return estado.estaEnEspera();
    }

    public boolean estaEnAtencion(){
        return estado.estaEnAtencion();
    }

    public boolean estaAtendido(){
        return estado.estaAtendido();
    }

    public boolean estaAbandonado(){
        return estado.estaAbandonado();
    } 

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public TurnoState getEstado() {
        return estado;
    }

    public void setEstado(TurnoState estado) {
        this.estado = estado;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((cliente == null) ? 0 : cliente.hashCode());
        result = prime * result + ((estado == null) ? 0 : estado.hashCode());
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
        Turno other = (Turno) obj;
        if (cliente == null) {
            if (other.cliente != null)
                return false;
        } else if (!cliente.equals(other.cliente))
            return false;
        if (estado == null) {
            if (other.estado != null)
                return false;
        } else if (!estado.equals(other.estado))
            return false;
        return true;
    }
}
