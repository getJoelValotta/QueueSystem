package totem;

import shared.cliente.Cliente;

public class Totem{
    private String id;
    private Cliente cliente;

    public Totem(String id, Cliente cliente){
        this.id = id;
        this.cliente = cliente;
    }

    public Totem(){
        this.id = null;
        this.cliente = null;
    }
    
    public String getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void setId(String id) {
        this.id = id;
    }
 
}
