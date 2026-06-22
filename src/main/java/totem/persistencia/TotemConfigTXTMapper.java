package totem.persistencia;

import shared.persistencia.*;

public class TotemConfigTXTMapper extends AbstractFileMapper<String> {
    private static TotemConfigTXTMapper instance;
    String filePath = "totemConfig.txt";

    private TotemConfigTXTMapper(String filePath) {
        super(filePath);
    }

    public static TotemConfigTXTMapper getInstance(String filePath) {
        if (instance == null) {
            instance = new TotemConfigTXTMapper(filePath);
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
