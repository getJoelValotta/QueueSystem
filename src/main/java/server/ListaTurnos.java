package server;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

import shared.turno.Turno;

public class ListaTurnos {
    private ConcurrentLinkedQueue<Turno> listaTurnos = new ConcurrentLinkedQueue<>();

    public ListaTurnos() {
    }

    public ListaTurnos(ConcurrentLinkedQueue<Turno> turnos) {
        this.listaTurnos = turnos;
    }

    public void agregaTurno(Turno turno) {
        listaTurnos.offer(turno);
    }

    public Turno llamaTurno() {
        return listaTurnos.poll();
    }

    public void eliminaTurno(Turno turno) {
        listaTurnos.remove(turno);
    }

    public int getCantidadTurnos() {
        return listaTurnos.size();
    }

    public boolean contieneA(Turno turno) {
        return listaTurnos.contains(turno);
    }

    public Iterator<Turno> devuelveIterator() {
        return listaTurnos.iterator();
    }

    public ConcurrentLinkedQueue<Turno> getListaTurnos() {
        return listaTurnos;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("ListaTurnos [\n");
        for (Turno t : listaTurnos) {
            sb.append("  ").append(t.toString()).append("\n");
        }
        sb.append("]");
        return sb.toString();
    }
}
