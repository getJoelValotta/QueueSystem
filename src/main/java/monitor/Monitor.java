package monitor;

import shared.turno.Turno;

public class Monitor {
    private String id;
    private int size;
    private ListaLlamados llamados;

    public Monitor(String id, int size, ListaLlamados llamados) {
        this.id = id;
        this.size = size;
        this.llamados = llamados;
    }

    public Monitor(String id, int size, String llamadosString) {
        this.id = id;
        this.size = size;
        this.llamados = parseLlamadosString(llamadosString);
    }

    public Monitor() {
        this.id = null;
        this.size = -1;
        this.llamados = null;
    }

    public void agregaTurno(Turno turno) {
        llamados.agregaTurno(turno);
    }

    public void renotificaTurno(Turno turno) {
        llamados.renotificaTurno(turno);
    }

    public boolean listaContieneA(Turno turno) {
        return llamados.contieneA(turno);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public ListaLlamados getLlamados() {
        return llamados;
    }

    public void setLlamados(ListaLlamados llamados) {
        this.llamados = llamados;
    }

    public String llamadosAString() {
        return llamados.llamadosAString();
    }

    public ListaLlamados parseLlamadosString(String llamadosString) {
        // this.llamados may be null when called from constructors; use a temporary
        // ListaLlamados with the current size to parse the string safely.
        ListaLlamados temp = new ListaLlamados(this.size > 0 ? this.size : 0);
        return temp.parseLlamadosString(llamadosString);
    }

}
