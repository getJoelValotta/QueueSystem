package shared.persistencia.factory;

import monitor.mapper.MonitorMapper;
import puesto.mapper.PuestoMapper;
import server.mapper.ConfigMapper;
import server.mapper.ServerMapper;
import shared.turno.mapper.TurnoMapper;
import totem.mapper.TotemMapper;

public class FactoryXMLMappers implements IFactoryPersistenciaArchivos{

    @Override
    public TotemMapper fabricaTotemMapper() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fabricaTotemMapper'");
    }

    @Override
    public PuestoMapper fabricaPuestoMapper() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fabricaPuestoMapper'");
    }

    @Override
    public MonitorMapper fabricaMonitorMapper() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fabricaMonitorMapper'");
    }

    @Override
    public TurnoMapper fabricaTurnoMapper() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fabricaTurnoMapper'");
    }

    @Override
    public ConfigMapper fabricaConfigMapper() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fabricaConfigMapper'");
    }

    @Override
    public ServerMapper fabricaServerMapper() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fabricaServerMapper'");
    }

}
