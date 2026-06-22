package monitor;

import java.util.LinkedList;
import shared.turno.Turno;

import shared.persistencia.TurnoToStringUtil;

public class ListaLlamados {
    private LinkedList<Turno> llamados = new LinkedList<>();
    private int size;

    public ListaLlamados(int size) {
        this.size = size;
    }

    public ListaLlamados(LinkedList<Turno> llamados, int size) {
        this.llamados = llamados;
        this.size = size;
    }

    public void agregaTurno(Turno turno) {
        llamados.push(turno);
        if (llamados.size() > size) {
            llamados.removeLast();
        }
    }

    public void renotificaTurno(Turno turno) {
        if (llamados.size() > 0) {
            if (llamados.remove(turno) == false) {
                agregaTurno(turno);
            } else
                llamados.push(turno);
        }
    }

    public boolean contieneA(Turno turno) {
        return llamados.contains(turno);
    }

    public String llamadosAString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (Turno turno : this.llamados) {
            sb.append(TurnoToStringUtil.getStringDelTurno(turno)).append("|");
        }
        sb.append(']');
        return sb.toString();
    }

    public ListaLlamados parseLlamadosString(String llamadosString) {
        LinkedList<Turno> listaLlamados = new LinkedList<>();
        // Saco los corchetes y separo por "|"
        String[] turnoStrings = llamadosString.substring(1, llamadosString.length() - 1).split("\\|");
        for (String turnoString : turnoStrings) {
            Turno turno = TurnoToStringUtil.getTurnoFromString(turnoString);
            if (turno != null) {
                listaLlamados.add(turno);
            }
        }
        ListaLlamados parsedLlamados = new ListaLlamados(listaLlamados, listaLlamados.size());
        return parsedLlamados;
    }

    public LinkedList<Turno> getLlamadosList() {
        return llamados;
    }
}
