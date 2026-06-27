package puesto.mapper.json;

import puesto.mapper.PuestoDTO;
import puesto.mapper.PuestoMapper;
import shared.persistencia.util.MapperJackson;
import shared.turno.mapper.json.TurnoJSONMapper;

public class PuestoJSONMapper extends PuestoMapper {

    public PuestoJSONMapper() {
        this.turnoMapper = new TurnoJSONMapper();
    }

    @Override
    protected String extension() {
        return "json";
    }

    @Override
    public String serializar(PuestoDTO obj) {
        try {
            return MapperJackson.json().writeValueAsString(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public PuestoDTO deserializar(String data) {
        try {
            return MapperJackson.json().readValue(data, PuestoDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
