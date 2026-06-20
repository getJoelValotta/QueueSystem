package admin;

public class Admin {
    private int metodoPersistencia;
    private int metodoEncriptacion;

    public Admin(int metodoPersistencia, int metodoEncriptacion){
        this.metodoEncriptacion = metodoEncriptacion;
        this.metodoPersistencia = metodoPersistencia;
    }

    public int getMetodoPersistencia() {
        return metodoPersistencia;
    }

    public void setMetodoPersistencia(int metodoPersistencia) {
        this.metodoPersistencia = metodoPersistencia;
    }

    public int getMetodoEncriptacion() {
        return metodoEncriptacion;
    }

    public void setMetodoEncriptacion(int metodoEncriptacion) {
        this.metodoEncriptacion = metodoEncriptacion;
    }

    
}
