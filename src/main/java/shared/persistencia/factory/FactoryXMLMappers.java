package shared.persistencia.factory;

import monitor.mapper.MonitorMapper;
import monitor.mapper.xml.MonitorXMLMapper;
import puesto.mapper.PuestoMapper;
import puesto.mapper.xml.PuestoXMLMapper;
import server.mapper.ConfigMapper;
import server.mapper.ServerMapper;
import server.mapper.xml.ConfigXMLMapper;
import server.mapper.xml.ServerXMLMapper;
import shared.turno.mapper.TurnoMapper;
import shared.turno.mapper.xml.TurnoXMLMapper;
import totem.mapper.TotemMapper;
import totem.mapper.xml.TotemXMLMapper;

public class FactoryXMLMappers implements IFactoryPersistenciaArchivos {

    @Override
    public TotemMapper fabricaTotemMapper() {
        return new TotemXMLMapper();
    }

    @Override
    public PuestoMapper fabricaPuestoMapper() {
        return new PuestoXMLMapper();
    }

    @Override
    public MonitorMapper fabricaMonitorMapper() {
        return new MonitorXMLMapper();
    }

    @Override
    public TurnoMapper fabricaTurnoMapper() {
        return new TurnoXMLMapper();
    }

    @Override
    public ConfigMapper fabricaConfigMapper() {
        return new ConfigXMLMapper();
    }

    @Override
    public ServerMapper fabricaServerMapper() {
        return new ServerXMLMapper();
    }
}
