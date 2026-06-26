package totem.mapper;

import shared.persistencia.ArchivosMapper;
import totem.Totem;

public abstract class TotemMapper extends ArchivosMapper<TotemDTO>{

    public TotemDTO toDto(Totem totem){
        return null;
    }

    public Totem toDominio(TotemDTO totemDto){
        return null;
    }

}
