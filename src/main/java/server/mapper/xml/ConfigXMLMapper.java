package server.mapper.xml;

import server.mapper.ConfigDTO;
import server.mapper.ConfigMapper;
import shared.persistencia.util.MapperJackson;

public class ConfigXMLMapper extends ConfigMapper {

    @Override
    protected String extension() {
        return "xml";
    }

    @Override
    public String serializar(ConfigDTO obj) {
        try {
            return MapperJackson.xml().writeValueAsString(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ConfigDTO deserializar(String data) {
        try {
            return MapperJackson.xml().readValue(data, ConfigDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
