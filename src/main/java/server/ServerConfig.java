package server;

public class ServerConfig {
    private String modoPersistencia;
    private String tipoEncriptacion;
    private String gestorIDString;
    private String claveEncriptacion;

    // Default constructor for Jackson deserialization
    public ServerConfig() {
    }

    public ServerConfig(String modoPersistencia, String gestorIDString, String tipoEncriptacion,
            String claveEncriptacion) {
        this.modoPersistencia = modoPersistencia;
        this.gestorIDString = gestorIDString;
        this.tipoEncriptacion = tipoEncriptacion;
        this.claveEncriptacion = claveEncriptacion;
    }

    public String getModoPersistencia() {
        return modoPersistencia;
    }

    public void setModoPersistencia(String modoPersistencia) {
        this.modoPersistencia = modoPersistencia;
    }

    public String getGestorIDString() {
        return gestorIDString;
    }

    public void setGestorID(String gestorID) {
        this.gestorIDString = gestorID;
    }

    public void setGestorIDString(String gestorIDString) {
        this.gestorIDString = gestorIDString;
    }

    public String getTipoEncriptacion() {
        return tipoEncriptacion;
    }

    public void setTipoEncriptacion(String tipoEncriptacion) {
        this.tipoEncriptacion = tipoEncriptacion;
    }

    public String getClaveEncriptacion() {
        return claveEncriptacion;
    }

    public void setClaveEncriptacion(String claveEncriptacion) {
        this.claveEncriptacion = claveEncriptacion;
    }

    @Override
    public String toString() {
        return "ServerConfig{" +
                "modoPersistencia='" + modoPersistencia + '\'' +
                ", tipoEncriptacion='" + tipoEncriptacion + '\'' +
                ", gestorID='" + gestorIDString + '\'' +
                ", claveEncriptacion='" + claveEncriptacion + '\'' +
                '}';
    }
}
