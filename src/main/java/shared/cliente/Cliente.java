package shared.cliente;

public class Cliente {
    private long dni;
    public static final String msgA = "El DNI no puede estar vacío.";
    public static final String msgB = "El DNI solo puede contener números.";

    public Cliente(String dni) throws ClienteDniVacioException, ClienteDniInvalidoException {
        if (dni == null || dni.trim().isEmpty()) {
            throw new ClienteDniVacioException(dni);
        }
        if (!dni.matches("\\d+")) {
            throw new ClienteDniInvalidoException(dni);
        }
        this.dni = Long.parseLong(dni);
    }

    public long getDni() {
        return dni;
    }
}
