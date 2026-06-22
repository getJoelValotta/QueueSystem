package totem.persistencia;

public class ConfigTotem {
    private String ID;
    private String modoEncriptacion;

    public ConfigTotem(String ID, String modoEncriptacion) {
        this.ID = ID;
        this.modoEncriptacion = modoEncriptacion;
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
}
