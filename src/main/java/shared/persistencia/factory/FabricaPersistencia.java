package shared.persistencia.factory;

import java.io.File;

import shared.persistencia.ArchivosMapper;

/**
 * Punto de seleccion del Abstract Factory de persistencia.
 *
 * <p>Traduce el "metodo de persistencia" que viaja por el sistema (que puede
 * llegar como {@code "#TXT#"}, {@code "txt"}, {@code "JSON"}, etc.) a la
 * fabrica concreta correspondiente, y permite autodetectar el formato ya
 * persistido en disco mirando que extension de archivo existe.</p>
 */
public final class FabricaPersistencia {

    public static final String TXT = "txt";
    public static final String JSON = "json";
    public static final String XML = "xml";

    private FabricaPersistencia() {
    }

    /** Normaliza cualquier variante del metodo a "txt" / "json" / "xml". */
    public static String normalizar(String metodo) {
        if (metodo == null) {
            return TXT;
        }
        String s = metodo.replace("#", "").trim().toLowerCase();
        switch (s) {
            case JSON:
                return JSON;
            case XML:
                return XML;
            case TXT:
            default:
                return TXT;
        }
    }

    /** Devuelve la fabrica concreta para el metodo solicitado. */
    public static IFactoryPersistenciaArchivos para(String metodo) {
        switch (normalizar(metodo)) {
            case JSON:
                return new FactoryJSONMappers();
            case XML:
                return new FactoryXMLMappers();
            case TXT:
            default:
                return new FactoryTXTMappers();
        }
    }

    /**
     * Autodetecta el formato ya persistido para un nombre base mirando los
     * archivos existentes en la carpeta {@code data}. Si conviven varios
     * formatos (porque al cambiar de metodo no se borran los anteriores), elige
     * el archivo modificado mas recientemente ("el ultimo formato gana").
     * Devuelve null si no hay ningun archivo previo para ese nombre base.
     */
    public static IFactoryPersistenciaArchivos detectarPorArchivo(String nombreBase) {
        File dir = new File(ArchivosMapper.CARPETA_DATA);
        File json = new File(dir, nombreBase + "." + JSON);
        File xml = new File(dir, nombreBase + "." + XML);
        File txt = new File(dir, nombreBase + "." + TXT);
        File elegido = masReciente(masReciente(existente(json), existente(xml)), existente(txt));
        if (elegido == null) {
            return null;
        }
        String nombre = elegido.getName();
        if (nombre.endsWith("." + JSON)) {
            return new FactoryJSONMappers();
        }
        if (nombre.endsWith("." + XML)) {
            return new FactoryXMLMappers();
        }
        return new FactoryTXTMappers();
    }

    private static File existente(File f) {
        return (f != null && f.exists()) ? f : null;
    }

    private static File masReciente(File a, File b) {
        if (a == null) {
            return b;
        }
        if (b == null) {
            return a;
        }
        return (b.lastModified() > a.lastModified()) ? b : a;
    }

    /** Igual que {@link #detectarPorArchivo(String)} pero con un metodo por defecto si no hay archivo. */
    public static IFactoryPersistenciaArchivos detectarOPara(String nombreBase, String metodoPorDefecto) {
        IFactoryPersistenciaArchivos detectada = detectarPorArchivo(nombreBase);
        return detectada != null ? detectada : para(metodoPorDefecto);
    }
}
