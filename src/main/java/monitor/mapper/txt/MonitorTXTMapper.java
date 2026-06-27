package monitor.mapper.txt;

import java.util.ArrayList;
import java.util.List;

import monitor.mapper.MonitorDTO;
import monitor.mapper.MonitorMapper;
import shared.turno.mapper.TurnoDTO;
import shared.turno.mapper.txt.TurnoTXTMapper;

/**
 * Mapper TXT del Monitor. Formato:
 * <pre>
 * id;size
 * (turno)
 * (turno)
 * </pre>
 */
public class MonitorTXTMapper extends MonitorMapper {

    public MonitorTXTMapper() {
        this.turnoMapper = new TurnoTXTMapper();
    }

    @Override
    protected String extension() {
        return "txt";
    }

    @Override
    public String serializar(MonitorDTO obj) {
        if (obj == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        String id = (obj.getId() == null) ? "" : obj.getId();
        sb.append(id).append(";").append(obj.getSize()).append("\n");
        if (obj.getLlamados() != null) {
            for (TurnoDTO turno : obj.getLlamados()) {
                sb.append(turnoMapper.serializar(turno)).append("\n");
            }
        }
        return sb.toString();
    }

    @Override
    public MonitorDTO deserializar(String data) {
        if (data == null) {
            return null;
        }
        String[] lineas = data.split("\\r?\\n");
        MonitorDTO dto = new MonitorDTO();
        List<TurnoDTO> llamados = new ArrayList<>();
        boolean headerLeido = false;
        for (String linea : lineas) {
            if (linea.trim().isEmpty()) {
                continue;
            }
            if (!headerLeido) {
                String[] header = linea.split(";", 2);
                dto.setId(header[0].trim().isEmpty() ? null : header[0].trim());
                dto.setSize((header.length > 1 && !header[1].trim().isEmpty())
                        ? Integer.parseInt(header[1].trim()) : 0);
                headerLeido = true;
            } else {
                TurnoDTO turno = turnoMapper.deserializar(linea.trim());
                if (turno != null) {
                    llamados.add(turno);
                }
            }
        }
        dto.setLlamados(llamados);
        return dto;
    }
}
