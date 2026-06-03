package server;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

import shared.turno.Turno;

public class ListaTurnos {
    private ConcurrentLinkedQueue<Turno> listaTurnos = new ConcurrentLinkedQueue<>();

    public void agregaTurno(Turno turno){
        listaTurnos.offer(turno);
    }

    public Turno llamaTurno(){
        return listaTurnos.poll();    
    }
    
    public int getCantidadTurnos(){
        return listaTurnos.size();
    }

    public boolean contieneA(Turno turno){
        return listaTurnos.contains(turno);
    }

    public Iterator<Turno> devuelveIterator(){
        return listaTurnos.iterator();
    }
}
