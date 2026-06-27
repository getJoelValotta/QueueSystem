package totem.mapper;

/**
 * DTO del Totem. Persiste su id y el dni del ultimo cliente cargado
 * ({@code -1} cuando no hay cliente).
 */
public class TotemDTO {

    private String id;
    private long dni = -1;

    public TotemDTO() {
    }

    public TotemDTO(String id, long dni) {
        this.id = id;
        this.dni = dni;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getDni() {
        return dni;
    }

    public void setDni(long dni) {
        this.dni = dni;
    }
}
