package totem.persistencia;

import shared.persistencia.*;

public class TotemConfigTXTMapper extends AbstractFileMapper<ConfigTotem> {
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
    protected String serialize(ConfigTotem config) {
        return config.getID() + "," + config.getModoEncriptacion();
    }

    @Override
    protected ConfigTotem deserialize(String data) {
        String[] parts = data.split(",");
        return new ConfigTotem(parts[0], parts[1]);
    }

}
