package shared.persistencia;

public abstract class ArchivosMapper<T> implements PersistenciaMapper<T>{

    @Override
    public final void templateGrabar(T obj) {
        //if no creado -> lo crea
        //serializar
        //
        
    }

    @Override
    public final T templateLeer() {

        return null;
    }

    public abstract String serializar(T obj);
    public abstract T deserializar(String data);
    
}
