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

    public Cliente(long dni) {
        this.dni = dni;
    }

    public long getDni() {
        return dni;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (dni ^ (dni >>> 32));
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
        Cliente other = (Cliente) obj;
        if (dni != other.dni)
            return false;
        return true;
    }
}
