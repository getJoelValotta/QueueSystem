package puesto.persistencia;

import shared.persistencia.*;

public class PuestoConfigTXTMapper extends AbstractFileMapper<String> {
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
    protected String serialize(String config) {
        return config;
    }

    @Override
    protected String deserialize(String data) {
        return data;
    }

}
