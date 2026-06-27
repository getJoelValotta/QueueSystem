package puesto.mapper.txt;

import puesto.mapper.PuestoDTO;
import puesto.mapper.PuestoMapper;
import shared.turno.mapper.txt.TurnoTXTMapper;

/**
 * Mapper TXT del Puesto. Formato: {@code id|(turno)}, donde el turno se delega
 * al {@link TurnoTXTMapper}.
 */
public class PuestoTXTMapper extends PuestoMapper {

    public PuestoTXTMapper() {
        this.turnoMapper = new TurnoTXTMapper();
    }

    @Override
    protected String extension() {
        return "txt";
    }

    @Override
    public String serializar(PuestoDTO obj) {
        if (obj == null) {
            return null;
        }
        String id = (obj.getId() == null) ? "" : obj.getId();
        return id + "|" + turnoMapper.serializar(obj.getTurno());
    }

    @Override
    public PuestoDTO deserializar(String data) {
        if (data == null) {
            return null;
        }
        String s = data.trim();
        int idx = s.indexOf('|');
        if (idx < 0) {
            return null;
        }
        String id = s.substring(0, idx).trim();
        String turnoTxt = s.substring(idx + 1).trim();
        PuestoDTO dto = new PuestoDTO();
        dto.setId(id.isEmpty() ? null : id);
        dto.setTurno(turnoMapper.deserializar(turnoTxt));
        return dto;
    }
}
