package server.mapper.json;

import server.mapper.ConfigDTO;
import server.mapper.ConfigMapper;
import shared.persistencia.util.MapperJackson;

public class ConfigJSONMapper extends ConfigMapper {

    @Override
    protected String extension() {
        return "json";
    }

    @Override
    public String serializar(ConfigDTO obj) {
        try {
            return MapperJackson.json().writeValueAsString(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ConfigDTO deserializar(String data) {
        try {
            return MapperJackson.json().readValue(data, ConfigDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
