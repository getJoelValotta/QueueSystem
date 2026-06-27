package server.mapper.xml;

import server.mapper.ServerDTO;
import server.mapper.ServerMapper;
import shared.persistencia.util.MapperJackson;
import shared.turno.mapper.xml.TurnoXMLMapper;

public class ServerXMLMapper extends ServerMapper {

    public ServerXMLMapper() {
        this.turnoMapper = new TurnoXMLMapper();
    }

    @Override
    protected String extension() {
        return "xml";
    }

    @Override
    public String serializar(ServerDTO obj) {
        try {
            return MapperJackson.xml().writeValueAsString(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ServerDTO deserializar(String data) {
        try {
            return MapperJackson.xml().readValue(data, ServerDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
