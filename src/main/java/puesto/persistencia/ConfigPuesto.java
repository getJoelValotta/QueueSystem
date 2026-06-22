package puesto.persistencia;

public class ConfigPuesto {
    String modoPersistencia;
    String modoEncriptacion;

    public ConfigPuesto(String modoPersistencia, String modoEncriptacion) {
        this.modoPersistencia = modoPersistencia;
        this.modoEncriptacion = modoEncriptacion;
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
