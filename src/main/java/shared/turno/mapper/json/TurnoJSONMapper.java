package shared.turno.mapper.json;

import shared.persistencia.util.MapperJackson;
import shared.turno.mapper.TurnoDTO;
import shared.turno.mapper.TurnoMapper;

public class TurnoJSONMapper extends TurnoMapper {

    @Override
    protected String extension() {
        return "json";
    }

    @Override
    public String serializar(TurnoDTO obj) {
        try {
            return MapperJackson.json().writeValueAsString(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public TurnoDTO deserializar(String data) {
        try {
            return MapperJackson.json().readValue(data, TurnoDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
