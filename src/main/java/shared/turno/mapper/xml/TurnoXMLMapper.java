package shared.turno.mapper.xml;

import shared.persistencia.util.MapperJackson;
import shared.turno.mapper.TurnoDTO;
import shared.turno.mapper.TurnoMapper;

public class TurnoXMLMapper extends TurnoMapper {

    @Override
    protected String extension() {
        return "xml";
    }

    @Override
    public String serializar(TurnoDTO obj) {
        try {
            return MapperJackson.xml().writeValueAsString(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public TurnoDTO deserializar(String data) {
        try {
            return MapperJackson.xml().readValue(data, TurnoDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
