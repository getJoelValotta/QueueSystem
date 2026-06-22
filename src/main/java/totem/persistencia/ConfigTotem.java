package totem.persistencia;

public class ConfigTotem {
    private String ID;
    private String modoEncriptacion;
    private String claveEncriptacion;

    public ConfigTotem(String ID, String modoEncriptacion, String claveEncriptacion) {
        this.ID = ID;
        this.modoEncriptacion = modoEncriptacion;
        this.claveEncriptacion = claveEncriptacion;
    }

    public String getID() {
        return ID;
    }

    public void setID(String iD) {
        ID = iD;
    }

    public String getModoEncriptacion() {
        return modoEncriptacion;
    }

    public void setModoEncriptacion(String modoEncriptacion) {
        this.modoEncriptacion = modoEncriptacion;
    }

    public String getClaveEncriptacion() {
        return claveEncriptacion;
    }

    public void setClaveEncriptacion(String claveEncriptacion) {
        this.claveEncriptacion = claveEncriptacion;
    }
}
