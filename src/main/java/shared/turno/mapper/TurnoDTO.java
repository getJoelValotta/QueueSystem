package shared.turno.mapper;

/**
 * DTO del Turno. Aplana el estado (patron State) del turno a datos planos
 * serializables, siguiendo el mismo criterio que
 * {@code shared.turno.TurnoToStringUtil}:
 * {@code (estado; cantLlamados; idPuesto; dni)}.
 *
 * <ul>
 *   <li>{@code estado}: "espera" | "atencion" | "atendido" | "abandonado"</li>
 *   <li>{@code cantLlamados}: -1 cuando no aplica</li>
 *   <li>{@code idPuesto}: null cuando no aplica</li>
 *   <li>{@code dni}: dni del cliente</li>
 * </ul>
 */
public class TurnoDTO {

    private String estado;
    private int cantLlamados = -1;
    private String idPuesto;
    private long dni;

    public TurnoDTO() {
    }

    public TurnoDTO(String estado, int cantLlamados, String idPuesto, long dni) {
        this.estado = estado;
        this.cantLlamados = cantLlamados;
        this.idPuesto = idPuesto;
        this.dni = dni;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getCantLlamados() {
        return cantLlamados;
    }

    public void setCantLlamados(int cantLlamados) {
        this.cantLlamados = cantLlamados;
    }

    public String getIdPuesto() {
        return idPuesto;
    }

    public void setIdPuesto(String idPuesto) {
        this.idPuesto = idPuesto;
    }

    public long getDni() {
        return dni;
    }

    public void setDni(long dni) {
        this.dni = dni;
    }
}
