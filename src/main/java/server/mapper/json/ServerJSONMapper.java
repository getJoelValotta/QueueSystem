package server.mapper.json;

import server.mapper.ServerDTO;
import server.mapper.ServerMapper;
import shared.persistencia.util.MapperJackson;
import shared.turno.mapper.json.TurnoJSONMapper;

public class ServerJSONMapper extends ServerMapper {

    public ServerJSONMapper() {
        this.turnoMapper = new TurnoJSONMapper();
    }

    @Override
    protected String extension() {
        return "json";
    }

    @Override
    public String serializar(ServerDTO obj) {
        try {
            return MapperJackson.json().writeValueAsString(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ServerDTO deserializar(String data) {
        try {
            return MapperJackson.json().readValue(data, ServerDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
