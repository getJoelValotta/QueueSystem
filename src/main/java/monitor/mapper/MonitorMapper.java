package monitor.mapper;

import shared.persistencia.ArchivosMapper;
import shared.turno.mapper.TurnoMapper;
import monitor.Monitor;
import monitor.mapper.MonitorDTO;

public abstract class MonitorMapper extends ArchivosMapper<MonitorDTO>{

    protected TurnoMapper turnoMapper;

    public MonitorDTO toDto(Monitor monitor){
        return null;
    }

    public Monitor toDominio(MonitorDTO monitorDto){
        return null;
    }

}