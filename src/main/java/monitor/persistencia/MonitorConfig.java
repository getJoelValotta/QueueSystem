package monitor.persistencia;

import monitor.Monitor;

public class MonitorConfig {
    String id;
    int size;
    String llamados;

    public MonitorConfig(String id, int size, String llamados) {
        this.id = id;
        this.size = size;
        this.llamados = llamados;
    }

    public MonitorConfig() {
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

    public String getLlamados() {
        return llamados;
    }

    public void setLlamados(String llamados) {
        this.llamados = llamados;
    }

    public Monitor getMonitorFromConfig() {
        Monitor monitor = new Monitor();
        monitor.setId(this.id);
        monitor.setSize(this.size);
        monitor.setLlamados(monitor.parseLlamadosString(llamados));
        return monitor;
    }

}
