package shared.persistencia.factory;

import monitor.mapper.MonitorMapper;
import puesto.mapper.PuestoMapper;
import server.mapper.ConfigMapper;
import server.mapper.ServerMapper;
import shared.turno.mapper.TurnoMapper;
import totem.mapper.TotemMapper;

public interface IFactoryPersistenciaArchivos {
    public TotemMapper fabricaTotemMapper();
    public PuestoMapper fabricaPuestoMapper();
    public MonitorMapper fabricaMonitorMapper();
    public TurnoMapper fabricaTurnoMapper();
    public ConfigMapper fabricaConfigMapper(); //     if (new File(cache, "config.json").exists()) return new JSONMapperFactory() if (new File(cache, "config.xml").exists())  return new XMLMapperFactory(); if (new File(cache, "config.txt").exists())  return new TXTMapperFactory();
    public ServerMapper fabricaServerMapper();

}
