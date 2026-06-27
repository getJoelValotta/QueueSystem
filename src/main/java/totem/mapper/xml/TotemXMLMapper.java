package totem.mapper.xml;

import shared.persistencia.util.MapperJackson;
import totem.mapper.TotemDTO;
import totem.mapper.TotemMapper;

public class TotemXMLMapper extends TotemMapper {

    @Override
    protected String extension() {
        return "xml";
    }

    @Override
    public String serializar(TotemDTO obj) {
        try {
            return MapperJackson.xml().writeValueAsString(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public TotemDTO deserializar(String data) {
        try {
            return MapperJackson.xml().readValue(data, TotemDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
