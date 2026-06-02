package monitor;

public class Monitor{
    private String id;
    private int size;
    private ListaLlamados llamados;

    public Monitor(String id, int size, ListaLlamados llamados) {
        this.id = id;
        this.llamados = llamados;
    }
    public Monitor() {
        this.id = null;
        this.size = -1;
        this.llamados = null;
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
    
}
