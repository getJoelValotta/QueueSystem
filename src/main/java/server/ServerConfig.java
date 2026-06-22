package server;

import server.id.GestorID;

public class ServerConfig {
    private String modoPersistencia, tipoEncriptacion;
    private GestorID gestorID;

    public ServerConfig(String modoPersistencia, GestorID gestorID, String tipoEncriptacion) {
        this.modoPersistencia = modoPersistencia;
        this.gestorID = gestorID;
        this.tipoEncriptacion = tipoEncriptacion;
    }

    public String getModoPersistencia() {
        return modoPersistencia;
    }

    public void setModoPersistencia(String modoPersistencia) {
        this.modoPersistencia = modoPersistencia;
    }

    public GestorID getGestorID() {
        return gestorID;
    }

    public void setGestorID(GestorID gestorID) {
        this.gestorID = gestorID;
    }

    public String getTipoEncriptacion() {
        return tipoEncriptacion;
    }

    public void setTipoEncriptacion(String tipoEncriptacion) {
        this.tipoEncriptacion = tipoEncriptacion;
    }

}
