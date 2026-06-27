package monitor.mapper.xml;

import monitor.mapper.MonitorDTO;
import monitor.mapper.MonitorMapper;
import shared.persistencia.util.MapperJackson;
import shared.turno.mapper.xml.TurnoXMLMapper;

public class MonitorXMLMapper extends MonitorMapper {

    public MonitorXMLMapper() {
        this.turnoMapper = new TurnoXMLMapper();
    }

    @Override
    protected String extension() {
        return "xml";
    }

    @Override
    public String serializar(MonitorDTO obj) {
        try {
            return MapperJackson.xml().writeValueAsString(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public MonitorDTO deserializar(String data) {
        try {
            return MapperJackson.xml().readValue(data, MonitorDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
