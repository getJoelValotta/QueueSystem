package puesto.persistencia;

import shared.persistencia.*;

public class PuestoConfigTXTMapper extends AbstractFileMapper<ConfigPuesto> {
    private static PuestoConfigTXTMapper instance;
    String filePath = "puestoConfig.txt";

    private PuestoConfigTXTMapper(String filePath) {
        super(filePath);
    }

    public static PuestoConfigTXTMapper getInstance(String filePath) {
        if (instance == null) {
            instance = new PuestoConfigTXTMapper(filePath);
        }
        return instance;
    }

    @Override
    protected String serialize(ConfigPuesto config) {
        return config.getModoPersistencia() + "," + config.getModoEncriptacion();
    }

    @Override
    protected ConfigPuesto deserialize(String data) {
        String[] parts = data.split(",");
        return new ConfigPuesto(parts[0], parts[1]);
    }

}
