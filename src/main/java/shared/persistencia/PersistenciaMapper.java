package shared.persistencia;

public abstract class PersistenciaMapper<Dominio, DTO> {

    // Template Method
    public final void guardar(Dominio objeto) {
        DTO dto = toDTO(objeto);      // convierte dominio -> DTO
        escribir(dto);                // escribe el DTO a disco o inclusive podria ser una base de datoss
    }

    public final Dominio cargar(String id) {
        DTO dto = leer(id);           // lee DTO de disco
        return toDominio(dto);        // reconstruye el objeto de dominio
    }

    protected abstract DTO toDTO(Dominio objeto);
    protected abstract Dominio toDominio(DTO dto);
    protected abstract void escribir(DTO dto);
    protected abstract DTO leer(String id);
}