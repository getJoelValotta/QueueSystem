package shared.cliente;

public class ClienteDniInvalidoException extends Exception{
    private String dni;

    public ClienteDniInvalidoException(String dni){
        super(Cliente.msgB);
        this.dni = dni;
    }

    public String getDni() {
        return dni;
    }

}
