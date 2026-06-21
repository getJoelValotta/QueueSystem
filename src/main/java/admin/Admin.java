package admin;

public class Admin {
    private String metodoPersistencia;
    private String metodoEncriptacion;

    public Admin(String metodoPersistencia, String metodoEncriptacion){
        this.metodoEncriptacion = metodoEncriptacion;
        this.metodoPersistencia = metodoPersistencia;
    }

    public String getMetodoPersistencia() {
        return metodoPersistencia;
    }

    public void setMetodoPersistencia(String metodoPersistencia) {
        this.metodoPersistencia = metodoPersistencia;
    }

    public String getMetodoEncriptacion() {
        return metodoEncriptacion;
    }

    public void setMetodoEncriptacion(String metodoEncriptacion) {
        this.metodoEncriptacion = metodoEncriptacion;
    }

    
}
