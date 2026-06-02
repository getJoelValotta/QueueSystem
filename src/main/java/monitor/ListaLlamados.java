package monitor;

import java.util.LinkedList;
import shared.turno.Turno;

public class ListaLlamados {
    private LinkedList<Turno> llamados = new LinkedList<>();
    private int size;

    public ListaLlamados(int size){
        this.size = size;
    }

    public void agregaTurno(Turno turno){
        llamados.push(turno);
        if (llamados.size() > size){
            llamados.removeLast();
        }
    }

    public void renotificaTurno(Turno turno){
        if (llamados.size() > 0){
            llamados.remove(turno);
            llamados.push(turno);
        }
    }

    public boolean contieneA(Turno turno){
        return llamados.contains(turno);
    }
}
