package shared.turno.mapper;

import shared.persistencia.ArchivosMapper;
import shared.turno.Turno;
import totem.Totem;
import totem.mapper.TotemDTO;

public abstract class TurnoMapper extends ArchivosMapper<TurnoDTO>{
    public TurnoDTO toDto(Turno turno){
        return null;
    }

    public Turno toDominio(TurnoDTO turnoDTO){
        return null;
    }

}
