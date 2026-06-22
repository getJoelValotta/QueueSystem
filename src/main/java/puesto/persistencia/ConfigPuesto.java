package puesto.persistencia;

public class ConfigPuesto {
    String modoPersistencia;
    String modoEncriptacion;
    String claveEncriptacion;

    public ConfigPuesto(String modoPersistencia, String modoEncriptacion, String claveEncriptacion) {
        this.modoPersistencia = modoPersistencia;
        this.modoEncriptacion = modoEncriptacion;
        this.claveEncriptacion = claveEncriptacion;
    }

    public String getClaveEncriptacion() {
        return claveEncriptacion;
    }

    public void setClaveEncriptacion(String claveEncriptacion) {
        this.claveEncriptacion = claveEncriptacion;
    }

    public String getModoPersistencia() {
        return modoPersistencia;
    }

    public void setModoPersistencia(String modoPersistencia) {
        this.modoPersistencia = modoPersistencia;
    }

    public String getModoEncriptacion() {
        return modoEncriptacion;
    }

    public void setModoEncriptacion(String modoEncriptacion) {
        this.modoEncriptacion = modoEncriptacion;
    }

}
