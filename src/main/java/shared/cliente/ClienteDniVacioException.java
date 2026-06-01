package shared.cliente;

public class ClienteDniVacioException extends Exception {
    private String dni;

    public ClienteDniVacioException(String dni){
        super(Cliente.msgA);
        this.dni = dni;
    }

    public String getDni() {
        return dni;
    }
}
