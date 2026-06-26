package server.mapper;


import org.bouncycastle.math.ec.ECCurve.Config;

import shared.persistencia.ArchivosMapper;


public abstract class ConfigMapper extends ArchivosMapper<ConfigDTO>{

    public ConfigDTO toDto(Config config){
        return null;
    }

    public Config toDominio(ConfigDTO configDTO){
        return null;
    }

}
