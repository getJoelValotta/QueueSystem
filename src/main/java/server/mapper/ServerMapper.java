package server.mapper;

import server.Server;
import shared.persistencia.ArchivosMapper;
import shared.turno.mapper.TurnoMapper;


public abstract class ServerMapper extends ArchivosMapper<ServerDTO>{

    protected TurnoMapper turnoMapper;

    public ServerDTO toDto(Server server){
        return null;
    }

    public Server toDominio(ServerDTO serverDTO){
        return null;
    }


}
