package server;

public class ServerConfig {
    private String modoPersistencia;
    private String tipoEncriptacion;
    private String gestorIDString;

    // Default constructor for Jackson deserialization
    public ServerConfig() {
    }

    public ServerConfig(String modoPersistencia, String gestorIDString, String tipoEncriptacion) {
        this.modoPersistencia = modoPersistencia;
        this.gestorIDString = gestorIDString;
        this.tipoEncriptacion = tipoEncriptacion;
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

    @Override
    public String toString() {
        return "ServerConfig{" +
                "modoPersistencia='" + modoPersistencia + '\'' +
                ", tipoEncriptacion='" + tipoEncriptacion + '\'' +
                ", gestorID='" + gestorIDString + '\'' +
                '}';
    }
}
