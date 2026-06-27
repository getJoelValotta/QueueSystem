package server;

/**
 * Clase de dominio (logico) de la configuracion del servidor.
 *
 * <p>NO pertenece al dominio de negocio (turnos / puestos / etc.): representa
 * los atributos de configuracion que hoy viven sueltos en
 * {@link server.ControllerServer} ({@code metodoPersistencia},
 * {@code metodoEncriptacion} y {@code claveEncriptacion}) y que se persisten en
 * el servidor para poder reestablecer el estado al reiniciar.</p>
 */
public class Config {

    private String metodoPersistencia;
    private String metodoEncriptacion;
    private String claveEncriptacion;

    public Config() {
    }

    public Config(String metodoPersistencia, String metodoEncriptacion, String claveEncriptacion) {
        this.metodoPersistencia = metodoPersistencia;
        this.metodoEncriptacion = metodoEncriptacion;
        this.claveEncriptacion = claveEncriptacion;
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

    public String getClaveEncriptacion() {
        return claveEncriptacion;
    }

    public void setClaveEncriptacion(String claveEncriptacion) {
        this.claveEncriptacion = claveEncriptacion;
    }
}
