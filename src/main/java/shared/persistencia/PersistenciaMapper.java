package shared.persistencia;

public interface PersistenciaMapper<T> {
    public void templateGrabar(T obj); 
    public T templateLeer();
}

