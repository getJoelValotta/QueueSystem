package shared.persistencia.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/**
 * Provee instancias compartidas y ya configuradas de los ObjectMapper de
 * Jackson para JSON y XML, de modo que los mappers concretos queden de una sola
 * linea y todos serialicen/deserialicen con la misma configuracion.
 */
public final class MapperJackson {

    private static final ObjectMapper JSON = construirJson();
    private static final XmlMapper XML = construirXml();

    private MapperJackson() {
    }

    private static ObjectMapper construirJson() {
        ObjectMapper m = new ObjectMapper();
        m.enable(SerializationFeature.INDENT_OUTPUT);
        m.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return m;
    }

    private static XmlMapper construirXml() {
        XmlMapper m = new XmlMapper();
        m.enable(SerializationFeature.INDENT_OUTPUT);
        m.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return m;
    }

    public static ObjectMapper json() {
        return JSON;
    }

    public static XmlMapper xml() {
        return XML;
    }
}
