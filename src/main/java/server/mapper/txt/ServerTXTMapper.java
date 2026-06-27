package server.mapper.txt;

import java.util.List;

import server.mapper.ServerDTO;
import server.mapper.ServerMapper;
import shared.turno.mapper.TurnoDTO;
import shared.turno.mapper.txt.TurnoTXTMapper;

/**
 * Mapper TXT del Server. Formato linea a linea (apto para append):
 * <pre>
 * GESTOR;contadorTotem;contadorPuesto;contadorMonitor
 * ESPERA;(turno)
 * ATENCION;(turno)
 * ABANDONADO;(turno)
 * ATENDIDO;(turno)
 * </pre>
 * Al ser lineas independientes, agregar un turno nuevo en espera es un simple
 * append; los cambios de lista disparan una reescritura completa del snapshot.
 */
public class ServerTXTMapper extends ServerMapper {

    public ServerTXTMapper() {
        this.turnoMapper = new TurnoTXTMapper();
    }

    @Override
    protected String extension() {
        return "txt";
    }

    @Override
    protected boolean soportaAppend() {
        return true;
    }

    @Override
    protected String lineaTurno(String categoria, TurnoDTO dto) {
        return categoria + ";" + turnoMapper.serializar(dto);
    }

    @Override
    public String serializar(ServerDTO obj) {
        if (obj == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(CAT_GESTOR).append(";")
                .append(obj.getContadorTotem()).append(";")
                .append(obj.getContadorPuesto()).append(";")
                .append(obj.getContadorMonitor()).append("\n");
        anexar(sb, CAT_ESPERA, obj.getEnEspera());
        anexar(sb, CAT_ATENCION, obj.getEnAtencion());
        anexar(sb, CAT_ABANDONADO, obj.getAbandonados());
        anexar(sb, CAT_ATENDIDO, obj.getAtendidos());
        return sb.toString();
    }

    private void anexar(StringBuilder sb, String categoria, List<TurnoDTO> turnos) {
        if (turnos == null) {
            return;
        }
        for (TurnoDTO turno : turnos) {
            sb.append(lineaTurno(categoria, turno)).append("\n");
        }
    }

    @Override
    public ServerDTO deserializar(String data) {
        if (data == null) {
            return null;
        }
        ServerDTO dto = new ServerDTO();
        for (String linea : data.split("\\r?\\n")) {
            if (linea.trim().isEmpty()) {
                continue;
            }
            int idx = linea.indexOf(';');
            if (idx < 0) {
                continue;
            }
            String categoria = linea.substring(0, idx).trim();
            String resto = linea.substring(idx + 1).trim();
            if (CAT_GESTOR.equals(categoria)) {
                String[] contadores = resto.split(";");
                if (contadores.length == 3) {
                    dto.setContadorTotem(Integer.parseInt(contadores[0].trim()));
                    dto.setContadorPuesto(Integer.parseInt(contadores[1].trim()));
                    dto.setContadorMonitor(Integer.parseInt(contadores[2].trim()));
                }
                continue;
            }
            TurnoDTO turno = turnoMapper.deserializar(resto);
            if (turno == null) {
                continue;
            }
            switch (categoria) {
                case CAT_ESPERA:
                    dto.getEnEspera().add(turno);
                    break;
                case CAT_ATENCION:
                    dto.getEnAtencion().add(turno);
                    break;
                case CAT_ABANDONADO:
                    dto.getAbandonados().add(turno);
                    break;
                case CAT_ATENDIDO:
                    dto.getAtendidos().add(turno);
                    break;
            }
        }
        return dto;
    }
}
