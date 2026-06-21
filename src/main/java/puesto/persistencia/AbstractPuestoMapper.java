package puesto.persistencia;

import shared.persistencia.AbstractFileMapper;
import shared.turno.*;
import puesto.Puesto;

public abstract class AbstractPuestoMapper extends AbstractFileMapper<Puesto> {
    String filePath = "puestos.txt";

    public AbstractPuestoMapper(String filePath) {
        super(filePath);
    }

    protected final String getStringState(Turno turno) {
        // formato: (estado,cantLlamados,idPuesto,dni) -1 para undefined
        if (turno == null) {
            return "(-1;-1;-1,-1)";
        }
        if (turno.estaEnEspera()) {
            return "(espera;-1;-1," + turno.getDniCliente() + ")";
        } else if (turno.estaEnAtencion()) {
            return "(atencion;" + turno.getCantLlamados() + ";" + turno.getIdPuesto() + "," + turno.getDniCliente()
                    + ")";
        } else if (turno.estaAtendido()) {
            return "(atendido;-1;" + turno.getIdPuesto() + "," + turno.getDniCliente() + ")";
        } else if (turno.estaAbandonado()) {
            return "(abandonado;-1;" + turno.getIdPuesto() + "," + turno.getDniCliente() + ")";
        }
        throw new IllegalArgumentException("Estado de turno no válido");
    }

    protected final Turno getTurnoFromString(String state) {
        // Si el string no empieza con parentesis, esta jodido chavales
        if (!state.startsWith("(") || !state.endsWith(")")) {
            throw new IllegalArgumentException("Formato de estado de turno no válido: " + state);
        }
        // le saco los aprentesis
        String content = state.substring(1, state.length() - 1);
        // divido el contenido por comas
        String[] parts = content.split("; ");
        if (parts.length != 4) {
            throw new IllegalArgumentException("Formato de estado de turno no válido: " + state);
        }

        Turno turno = new Turno();
        turno.setCliente(Long.parseLong(parts[3]));
        switch (parts[0]) {
            // El turno ya viene definido en espera, asi que no hago nada
            case "-1":
                return null;
            case "espera":
                return turno;
            // El turno necesita idpuesto y cantLlamados, asi que se los seteo
            case "atencion":
                TurnoState atencionState = new TurnoEnAtencion(turno, parts[2], Integer.parseInt(parts[1]));
                turno.setEstado(atencionState);
                return turno;
            // El turno solo necesita idpuesto
            case "atendido":
                TurnoState atendidoState = new TurnoAtendido(turno, parts[2]);
                turno.setEstado(atendidoState);
                return turno;
            case "abandonado":
                TurnoState abandonadoState = new TurnoAbandonado(turno, parts[2]);
                turno.setEstado(abandonadoState);
                return turno;
            default:
                throw new IllegalArgumentException("Estado de turno no válido: " + state);
        }
    }

}
