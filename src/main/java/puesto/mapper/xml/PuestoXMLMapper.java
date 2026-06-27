package puesto.mapper.xml;

import puesto.mapper.PuestoDTO;
import puesto.mapper.PuestoMapper;
import shared.persistencia.util.MapperJackson;
import shared.turno.mapper.xml.TurnoXMLMapper;

public class PuestoXMLMapper extends PuestoMapper {

    public PuestoXMLMapper() {
        this.turnoMapper = new TurnoXMLMapper();
    }

    @Override
    protected String extension() {
        return "xml";
    }

    @Override
    public String serializar(PuestoDTO obj) {
        try {
            return MapperJackson.xml().writeValueAsString(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public PuestoDTO deserializar(String data) {
        try {
            return MapperJackson.xml().readValue(data, PuestoDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
