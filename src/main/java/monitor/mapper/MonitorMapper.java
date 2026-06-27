package monitor.mapper;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import monitor.ListaLlamados;
import monitor.Monitor;
import shared.persistencia.ArchivosMapper;
import shared.turno.Turno;
import shared.turno.mapper.TurnoDTO;
import shared.turno.mapper.TurnoMapper;

/**
 * Data Mapper del Monitor. Compone un {@link TurnoMapper} (mismo formato) para
 * mapear la lista de turnos llamados.
 */
public abstract class MonitorMapper extends ArchivosMapper<MonitorDTO> {

    protected TurnoMapper turnoMapper;

    @Override
    protected String nombreBaseArchivo() {
        return "monitor";
    }

    public void setTurnoMapper(TurnoMapper turnoMapper) {
        this.turnoMapper = turnoMapper;
    }

    public MonitorDTO toDto(Monitor monitor) {
        if (monitor == null) {
            return null;
        }
        List<TurnoDTO> llamados = new ArrayList<>();
        if (monitor.getLlamados() != null) {
            for (Turno turno : monitor.getLlamados().getLlamadosList()) {
                TurnoDTO dto = turnoMapper.toDto(turno);
                if (dto != null) {
                    llamados.add(dto);
                }
            }
        }
        return new MonitorDTO(monitor.getId(), monitor.getSize(), llamados);
    }

    public Monitor toDominio(MonitorDTO dto) {
        if (dto == null) {
            return null;
        }
        LinkedList<Turno> turnos = new LinkedList<>();
        if (dto.getLlamados() != null) {
            for (TurnoDTO turnoDto : dto.getLlamados()) {
                Turno turno = turnoMapper.toDominio(turnoDto);
                if (turno != null) {
                    turnos.add(turno);
                }
            }
        }
        ListaLlamados llamados = new ListaLlamados(turnos, dto.getSize());
        return new Monitor(dto.getId(), dto.getSize(), llamados);
    }
}
