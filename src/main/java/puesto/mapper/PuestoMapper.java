package puesto.mapper;

import shared.persistencia.ArchivosMapper;
import shared.turno.mapper.TurnoMapper;
import puesto.Puesto;

public abstract class PuestoMapper extends ArchivosMapper<PuestoDTO>{

    protected TurnoMapper turnoMapper;

    public PuestoDTO toDto(Puesto puesto){
        return null;
    }

    public Puesto toDominio(PuestoDTO puestoDto){
        return null;
    }

}
