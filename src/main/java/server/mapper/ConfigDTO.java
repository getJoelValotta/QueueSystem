package server.mapper;

//ATENCION: ESTA CLASE NO PERTENECE A NINGUNA CLASE DEL DOMINIO: SE SUPONE QUE PERSISTIRA EN EL SERVIDOR LOS ATRIBUTOS DE CLASE
// private String metodoPersistencia
// private String metodoEncriptacion
// private String claveEncriptacion

/**
 * DTO de la configuracion del servidor (ver {@link server.Config}).
 */
public class ConfigDTO {

    private String metodoPersistencia;
    private String metodoEncriptacion;
    private String claveEncriptacion;

    public ConfigDTO() {
    }

    public ConfigDTO(String metodoPersistencia, String metodoEncriptacion, String claveEncriptacion) {
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
