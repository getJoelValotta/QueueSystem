package server.id;

public class GestorID {
    private GestorIDListener controllerServer;
    private int contadorTotem;
    private int contadorPuesto;
    private int contadorMonitor;

    public GestorID(int contadorTotem, int contadorPuesto, int contadorMonitor, GestorIDListener controllerServer) {
        this.contadorTotem = contadorTotem;
        this.contadorPuesto = contadorPuesto;
        this.contadorMonitor = contadorMonitor;
        this.controllerServer = controllerServer;
    }

    private String generarId(String prefijo, int contador) {
        return String.format("%s_%03d", prefijo, contador);
    }

    public synchronized String generarIdTotem() {
        contadorTotem++;
        controllerServer.persisteYEnvia(this); // Que el envio y la persistencia este dentro de un synchronized
                                               // garantiza que ningun totem, en este caso,
        return generarId("tot", contadorTotem); // pida una ID al mismo tiempo que se estan enviando los datos, lo que
                                                // implica que si masivamente (cosa que es IMPOSIBLE
    } // para un software de este alcance) totems piden una ID unica estaran
      // "encolados" esperando a que se les asignen un id.

    public synchronized String generarIdPuesto() {
        contadorPuesto++;
        controllerServer.persisteYEnvia(this);
        return generarId("pue", contadorPuesto);
    }

    public synchronized String generarIdMonitor() {
        contadorMonitor++;
        controllerServer.persisteYEnvia(this);
        return generarId("mon", contadorMonitor);
    }

    public int getContadorTotem() {
        return contadorTotem;
    }

    public int getContadorPuesto() {
        return contadorPuesto;
    }

    public int getContadorMonitor() {
        return contadorMonitor;
    }

    public GestorIDListener getControllerServer() {
        return controllerServer;
    }

    public void setContadorTotem(int contadorTotem) {
        this.contadorTotem = contadorTotem;
    }

    public void setContadorPuesto(int contadorPuesto) {
        this.contadorPuesto = contadorPuesto;
    }

    public void setContadorMonitor(int contadorMonitor) {
        this.contadorMonitor = contadorMonitor;
    }

    @Override
    public String toString() { // Con el formato (contadortotem,contadorpuesto,contadormonitor)
        return "(" + contadorTotem + "," + contadorPuesto + "," + contadorMonitor + ')';
    }

}
