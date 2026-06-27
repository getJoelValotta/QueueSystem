package server.mapper;

import server.Config;
import shared.persistencia.ArchivosMapper;

/**
 * Data Mapper de la configuracion del servidor. Mapea {@link Config} hacia/desde
 * {@link ConfigDTO}. Las subclases concretas implementan el formato.
 */
public abstract class ConfigMapper extends ArchivosMapper<ConfigDTO> {

    @Override
    protected String nombreBaseArchivo() {
        return "config";
    }

    public ConfigDTO toDto(Config config) {
        if (config == null) {
            return null;
        }
        return new ConfigDTO(config.getMetodoPersistencia(), config.getMetodoEncriptacion(),
                config.getClaveEncriptacion());
    }

    public Config toDominio(ConfigDTO dto) {
        if (dto == null) {
            return null;
        }
        return new Config(dto.getMetodoPersistencia(), dto.getMetodoEncriptacion(), dto.getClaveEncriptacion());
    }
}
