package shared.persistencia;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * Clase base del patron Data Mapper que implementa el patron Template Method
 * para la persistencia en archivos.
 *
 * <p>El "esqueleto" del algoritmo de grabado/lectura vive aca (manejo del
 * archivo, codificacion, creacion de la carpeta {@code data}, etc.) mientras
 * que los pasos variables ({@link #serializar(Object)} y
 * {@link #deserializar(String)}) los completan las subclases concretas de cada
 * formato (TXT / JSON / XML).</p>
 *
 * <p>Cada mapper persiste en {@code data/<nombreBase>.<extension>}. Como la
 * extension depende del formato, al cambiar de metodo de persistencia se
 * escribe un archivo distinto y los anteriores NO se borran: el sistema
 * sobreescribe unicamente el archivo del formato actual.</p>
 */
public abstract class ArchivosMapper<T> implements PersistenciaMapper<T> {

    /** Carpeta donde se persiste todo (relativa al directorio de ejecucion). */
    public static final String CARPETA_DATA = "data";

    // ----------------------------------------------------------------------
    // Pasos variables (primitivos) que completan las subclases
    // ----------------------------------------------------------------------

    /** Nombre del archivo sin extension (ej: "totem", "server"). Lo define el mapper de dominio. */
    protected abstract String nombreBaseArchivo();

    /** Extension/formato del archivo (ej: "txt", "json", "xml"). Lo define el mapper de formato. */
    protected abstract String extension();

    public abstract String serializar(T obj);

    public abstract T deserializar(String data);

    /**
     * Indica si el formato soporta "append" valido (solo el formato linea a
     * linea, TXT). Los formatos estructurados (JSON/XML) reescriben completo.
     */
    protected boolean soportaAppend() {
        return false;
    }

    // ----------------------------------------------------------------------
    // Template Methods (esqueleto invariante, no se sobreescriben)
    // ----------------------------------------------------------------------

    /** Graba el objeto completo, sobreescribiendo el archivo del formato actual. */
    @Override
    public final void templateGrabar(T obj) {
        if (obj == null) {
            return;
        }
        escribir(serializar(obj), false);
    }

    /** Lee y deserializa el contenido del archivo del formato actual (null si no existe / vacio). */
    @Override
    public final T templateLeer() {
        File archivo = archivo();
        if (!archivo.exists()) {
            return null;
        }
        try {
            byte[] bytes = Files.readAllBytes(archivo.toPath());
            String data = new String(bytes, StandardCharsets.UTF_8);
            if (data.trim().isEmpty()) {
                return null;
            }
            return deserializar(data);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // ----------------------------------------------------------------------
    // Utilidades comunes para las subclases
    // ----------------------------------------------------------------------

    /** Devuelve el File destino, creando la carpeta {@code data} si hace falta. */
    protected final File archivo() {
        File dir = new File(CARPETA_DATA);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return new File(dir, nombreBaseArchivo() + "." + extension());
    }

    /** True si el archivo del formato actual ya existe en disco. */
    public final boolean existeArchivo() {
        return archivo().exists();
    }

    /** Escribe texto en el archivo (append=false sobreescribe, append=true agrega al final). */
    protected final void escribir(String contenido, boolean append) {
        if (contenido == null) {
            return;
        }
        File archivo = archivo();
        try (Writer w = new OutputStreamWriter(new FileOutputStream(archivo, append), StandardCharsets.UTF_8)) {
            w.write(contenido);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Agrega una linea cruda al final del archivo del formato actual. Solo debe
     * usarse cuando {@link #soportaAppend()} es true (formato TXT). Se utiliza
     * para la optimizacion "primero todo completo, luego appends".
     */
    protected final void appendLinea(String linea) {
        if (linea == null) {
            return;
        }
        if (!linea.endsWith("\n")) {
            linea = linea + "\n";
        }
        escribir(linea, true);
    }
}
