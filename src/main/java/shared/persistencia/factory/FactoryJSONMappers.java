package shared.persistencia.factory;

import monitor.mapper.MonitorMapper;
import monitor.mapper.json.MonitorJSONMapper;
import puesto.mapper.PuestoMapper;
import puesto.mapper.json.PuestoJSONMapper;
import server.mapper.ConfigMapper;
import server.mapper.ServerMapper;
import server.mapper.json.ConfigJSONMapper;
import server.mapper.json.ServerJSONMapper;
import shared.turno.mapper.TurnoMapper;
import shared.turno.mapper.json.TurnoJSONMapper;
import totem.mapper.TotemMapper;
import totem.mapper.json.TotemJSONMapper;

public class FactoryJSONMappers implements IFactoryPersistenciaArchivos {

    @Override
    public TotemMapper fabricaTotemMapper() {
        return new TotemJSONMapper();
    }

    @Override
    public PuestoMapper fabricaPuestoMapper() {
        return new PuestoJSONMapper();
    }

    @Override
    public MonitorMapper fabricaMonitorMapper() {
        return new MonitorJSONMapper();
    }

    @Override
    public TurnoMapper fabricaTurnoMapper() {
        return new TurnoJSONMapper();
    }

    @Override
    public ConfigMapper fabricaConfigMapper() {
        return new ConfigJSONMapper();
    }

    @Override
    public ServerMapper fabricaServerMapper() {
        return new ServerJSONMapper();
    }
}
