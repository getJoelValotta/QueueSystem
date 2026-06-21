package shared.turno;

public class TurnoFactory {
    public static Turno crearTurno() {
        return new Turno();
    }

    // turno atendido o abandonado
    public static Turno crearTurno(String estado, String idpuesto) {
        Turno turno = new Turno();
        TurnoState turnoState;
        switch (estado) {
            case "atendido":
                turnoState = new TurnoAtendido(turno, idpuesto);
                break;
            case "abandonado":
                turnoState = new TurnoAbandonado(turno, idpuesto);
                break;
            default:
                throw new IllegalArgumentException("Estado de turno no válido: " + estado);
        }
        turno.setEstado(turnoState);
        return turno;
    }

    // turno en atencion
    public static Turno crearTurno(String estado, String idpuesto, int cantLlamados) {
        Turno turno = new Turno();
        TurnoState turnoState;
        switch (estado) {
            case "atencion":
                turnoState = new TurnoEnAtencion(turno, idpuesto, cantLlamados);
                break;
            default:
                throw new IllegalArgumentException("Estado de turno no válido: " + estado);
        }
        turno.setEstado(turnoState);
        return turno;
    }
}
