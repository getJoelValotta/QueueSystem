package totem.mapper.json;

import shared.persistencia.util.MapperJackson;
import totem.mapper.TotemDTO;
import totem.mapper.TotemMapper;

public class TotemJSONMapper extends TotemMapper {

    @Override
    protected String extension() {
        return "json";
    }

    @Override
    public String serializar(TotemDTO obj) {
        try {
            return MapperJackson.json().writeValueAsString(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public TotemDTO deserializar(String data) {
        try {
            return MapperJackson.json().readValue(data, TotemDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
