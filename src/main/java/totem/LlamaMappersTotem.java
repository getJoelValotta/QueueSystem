package totem;

import totem.persistencia.*;

public class LlamaMappersTotem {
    public static void persistir(ConfigTotem totem) {
        if (totem.getID() == null) {
            return;
        }
        TotemConfigTXTMapper.getInstance("totemConfig.txt").save(totem);
    }

    public static ConfigTotem recuperar() throws RuntimeException {
        ConfigTotem config = TotemConfigTXTMapper.getInstance("totemConfig.txt").load();
        return config;
    }

}
