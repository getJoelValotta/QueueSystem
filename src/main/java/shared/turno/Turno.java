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
}
