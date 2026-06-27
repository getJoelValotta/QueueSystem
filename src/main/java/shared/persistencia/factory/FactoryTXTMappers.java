package shared.persistencia.factory;

import monitor.mapper.MonitorMapper;
import monitor.mapper.txt.MonitorTXTMapper;
import puesto.mapper.PuestoMapper;
import puesto.mapper.txt.PuestoTXTMapper;
import server.mapper.ConfigMapper;
import server.mapper.ServerMapper;
import server.mapper.txt.ConfigTXTMapper;
import server.mapper.txt.ServerTXTMapper;
import shared.turno.mapper.TurnoMapper;
import shared.turno.mapper.txt.TurnoTXTMapper;
import totem.mapper.TotemMapper;
import totem.mapper.txt.TotemTXTMapper;

public class FactoryTXTMappers implements IFactoryPersistenciaArchivos {

    @Override
    public TotemMapper fabricaTotemMapper() {
        return new TotemTXTMapper();
    }

    @Override
    public PuestoMapper fabricaPuestoMapper() {
        return new PuestoTXTMapper();
    }

    @Override
    public MonitorMapper fabricaMonitorMapper() {
        return new MonitorTXTMapper();
    }

    @Override
    public TurnoMapper fabricaTurnoMapper() {
        return new TurnoTXTMapper();
    }

    @Override
    public ConfigMapper fabricaConfigMapper() {
        return new ConfigTXTMapper();
    }

    @Override
    public ServerMapper fabricaServerMapper() {
        return new ServerTXTMapper();
    }
}
