package monitor.mapper.json;

import monitor.mapper.MonitorDTO;
import monitor.mapper.MonitorMapper;
import shared.persistencia.util.MapperJackson;
import shared.turno.mapper.json.TurnoJSONMapper;

public class MonitorJSONMapper extends MonitorMapper {

    public MonitorJSONMapper() {
        this.turnoMapper = new TurnoJSONMapper();
    }

    @Override
    protected String extension() {
        return "json";
    }

    @Override
    public String serializar(MonitorDTO obj) {
        try {
            return MapperJackson.json().writeValueAsString(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public MonitorDTO deserializar(String data) {
        try {
            return MapperJackson.json().readValue(data, MonitorDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
